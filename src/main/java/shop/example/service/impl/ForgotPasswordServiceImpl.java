package shop.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.example.domain.User;
import shop.example.dto.request.orther.ForgotPasswordRequest;
import shop.example.dto.request.orther.MailBody;
import shop.example.dto.request.orther.VerifyOTPRequest;
import shop.example.enums.ErrorCode;
import shop.example.exceptions.AuthenticatedException;
import shop.example.exceptions.UserNotFoundException;
import shop.example.repository.UserRepository;
import shop.example.service.ForgotPasswordService;
import shop.example.service.MailService;
import shop.example.service.RedisService;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class ForgotPasswordServiceImpl  implements ForgotPasswordService {
    private static final String prefixEmail = "OTP_toEmail:";
    private final UserRepository userRepository;
    private final MailService mailService;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void verifyEmail(String email) {
        User user = getUserByEmail(email);
        int otp = generateOTP();
        MailBody mailBody = MailBody.builder()
                .emailTo(email)
                .subject("OTP for forgot password request")
                .content("Xin chao "+user.getFullName()+" This is the OTP for you: "+otp)
                .build();
        mailService.sendOTP(mailBody);
        redisService.setString(prefixEmail+email,String.valueOf(otp),600L);
    }

    private int generateOTP(){
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.nextInt(100_000,1_000_000);
    }

    @Override
    public void verifyOTP(String email, VerifyOTPRequest otpRequest) {
        String otpFromRedis= redisService.getString(prefixEmail+email);
        log.info("OTP:{}",otpFromRedis);
        if(otpFromRedis==null || !otpRequest.getOtp().equals(Integer.parseInt(otpFromRedis))){
            throw new AuthenticatedException(ErrorCode.UNAUTHENTICATED.getMessage());
        }
        log.info("OTP verify successfully");
    }

    @Override
    @Transactional
    public void changePassword(String email, ForgotPasswordRequest request) {
        User user = getUserByEmail(email);
        if(!request.getNewPassword().equals(request.getConfirmPassword())){
            throw new AuthenticatedException(ErrorCode.UNAUTHENTICATED.getMessage());
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    private User getUserByEmail(String email){
        return userRepository.findByEmail(email).
                orElseThrow(()-> new UserNotFoundException(ErrorCode.USER_NOT_FOUND.getMessage()));
    }
}
