package shop.example.dto.request.orther;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ForgotPasswordRequest {

    @Size(min = 6, message = "Password must be at least {min} characters long")
    @Pattern(regexp="^\\S+$",message = "Password must not contain spaces")
    private String newPassword;

    private String confirmPassword;

}
