package shop.example.service;

import shop.example.domain.User;

public interface TokenService {
    String generateAccessToken(User user,Long duration);
    String generateRefreshToken(User user,Long duration);
    Long getUserIdFromRefreshToken(String refreshToken);
    String getUUIDFromRefreshToken(String refreshToken);

}
