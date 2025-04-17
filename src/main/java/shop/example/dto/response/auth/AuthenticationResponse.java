package shop.example.dto.response.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class AuthenticationResponse {
    private String accessToken;
    private String refreshToken;
}
