package shop.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.example.config.KafkaProducer;
import shop.example.constraint.PreDeFindRole;
import shop.example.converter.DataConverter;
import shop.example.domain.Role;
import shop.example.domain.User;
import shop.example.dto.request.orther.MailBody;
import shop.example.dto.request.user.UserRequest;
import shop.example.dto.request.user.UserUpdateRequest;
import shop.example.dto.response.user.UserResponse;
import shop.example.enums.ErrorCode;
import shop.example.exceptions.ResourceExistedException;
import shop.example.exceptions.ResourceNotFoundException;
import shop.example.repository.RoleRepository;
import shop.example.repository.UserRepository;
import shop.example.service.UserService;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final DataConverter userConverter;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final KafkaProducer producer;
    private final RoleRepository roleRepository;

    @Value("${kafka.outbound-topic}")
    protected String topic;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUser() {
        return userRepository.findAll()
                .stream()
                .map(userConverter::convertUserResponse)
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("#id.toString()==authentication.name")
    public void updateUser(Long id, UserUpdateRequest userRequest) {
        User user = getUserById(id);
        modelMapper.map(userRequest, user);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


    @Override
    @PostAuthorize("returnObject.id.toString()==authentication.name")
    public UserResponse findById(Long id) {
        // log.info("Auth name:{}", SecurityContextHolder.getContext().getAuthentication().getName());
        return userConverter.convertUserResponse(getUserById(id));
    }

    @Override
    @Transactional
    public void createUser(UserRequest userRequest) {
        User user = modelMapper.map(userRequest, User.class);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        List<Role> roles = new ArrayList<>();
        roleRepository.findByName(PreDeFindRole.USER_ROLE).ifPresent(roles::add);
        user.setRoles(roles);
        try {
            userRepository.save(user);
            producer.send(topic, MailBody.builder()
                    .emailTo(user.getEmail())
                    .fullName(user.getFullName())
                    .build());
            log.info("Email event sent to Kafka topic: {}", topic);
        } catch (DataIntegrityViolationException exception) {
            throw new ResourceExistedException(ErrorCode.USER_EXISTED.getMessage());
        }
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND.getMessage()));
    }
}
