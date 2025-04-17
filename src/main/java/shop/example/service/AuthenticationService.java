package shop.example.service;

import shop.example.dto.request.auth.AuthenticationRequest;
import shop.example.dto.request.auth.RefreshTokenRequest;
import shop.example.dto.response.auth.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse logIn(AuthenticationRequest authenticationRequest);
    AuthenticationResponse refreshToken(RefreshTokenRequest refreshToken);
    void logOut(RefreshTokenRequest request);
}
