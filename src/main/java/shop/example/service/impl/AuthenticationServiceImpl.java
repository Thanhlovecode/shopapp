package shop.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shop.example.domain.User;
import shop.example.dto.request.auth.AuthenticationRequest;
import shop.example.dto.request.auth.RefreshTokenRequest;
import shop.example.dto.response.auth.AuthenticationResponse;
import shop.example.enums.ErrorCode;
import shop.example.exceptions.AuthenticatedException;
import shop.example.exceptions.TokenValidationException;
import shop.example.exceptions.UserNotFoundException;
import shop.example.repository.UserRepository;
import shop.example.service.AuthenticationService;
import shop.example.service.RedisService;
import shop.example.service.TokenService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RedisService redisService;
    private final UserRepository userRepository;

    private static final String prefixAccessToken = "access_token_userId:";
    private static final String prefixRefreshToken = "refresh_token_userId:";

    @NonFinal
    @Value("${ACCESS_TOKEN_DURATION}")
    protected Long accessTokenExpiration;

    @NonFinal
    @Value("${REFRESH_TOKEN_DURATION}")
    protected Long refreshTokenExpiration;

    @Override
    public AuthenticationResponse logIn(AuthenticationRequest authenticationRequest) {
        User user = userRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new AuthenticatedException("Wrong email or password"));

        if (!passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword())) {
            throw new AuthenticatedException("Wrong email or password");
        }
        log.info("User {} authenticated successfully", user.getFullName());

        return generateAccessAndReFreshToken(user);
    }

    @Override
    public void logOut(RefreshTokenRequest request) {
        Long userId = tokenService.getUserIdFromRefreshToken(request.getRefreshToken());
        validatedRefreshToken(userId, request.getRefreshToken());
        // Tăng version accessToken để vô hiệu hóa tất cả accessToken cũ
        redisService.incrementAccessTokenVersion(prefixAccessToken + userId);
        log.info("User '{}' logged out successfully. Tokens invalidated.", userId);
    }


    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        Long userId = tokenService.getUserIdFromRefreshToken(refreshTokenRequest.getRefreshToken());
        validatedRefreshToken(userId, refreshTokenRequest.getRefreshToken());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND.getMessage()));

        log.info("Generating new tokens for userId '{}'.", userId);
        return generateAccessAndReFreshToken(user);
    }

    private AuthenticationResponse generateAccessAndReFreshToken(User user){
        String accessToken = tokenService.generateAccessToken(user, accessTokenExpiration);
        String refreshToken = tokenService.generateRefreshToken(user, refreshTokenExpiration);
        // save redis
        redisService.setVersionAccessToken(prefixAccessToken + user.getId(),1L, accessTokenExpiration);
        redisService.setString(prefixRefreshToken + user.getId(),
                tokenService.getUUIDFromRefreshToken(refreshToken), refreshTokenExpiration);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void validatedRefreshToken(Long userId, String refreshToken) {
        String refreshTokenKey = prefixRefreshToken + userId;
        Object storedUUID = redisService.getString(refreshTokenKey);

        if (storedUUID == null || !storedUUID.equals(tokenService.getUUIDFromRefreshToken(refreshToken))) {
            // TH refreshToken ko tồn tại trong redis (bị xóa) hacker lấy và access continue
            log.warn("Suspicious refresh token usage detected for userId {}. Invalidating tokens.", userId);
            redisService.incrementAccessTokenVersion(prefixAccessToken + userId);
            redisService.deleteKey(refreshTokenKey);
            throw new TokenValidationException(ErrorCode.TOKEN_INVALID.getMessage());
        }
        // refreshToken hợp lệ
        log.info("Refresh token validated successfully for userId {}", userId);
        redisService.deleteKey(refreshTokenKey);
    }

}
