package shop.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.example.domain.Role;
import shop.example.dto.request.role.RoleRequest;
import shop.example.dto.response.role.RoleResponse;
import shop.example.repository.RoleRepository;
import shop.example.service.RoleService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void createRole(RoleRequest request) {
        Role role = modelMapper.map(request,Role.class);
        roleRepository.save(role);
    }

    @Override
    public List<RoleResponse> getAllRole() {
        return roleRepository.findAll()
                .stream().map((element) -> modelMapper.map(element, RoleResponse.class))
                .toList();
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
