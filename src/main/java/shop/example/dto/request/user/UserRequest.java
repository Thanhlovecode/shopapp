package shop.example.dto.request.user;

import jakarta.validation.constraints.*;
import lombok.*;
import shop.example.enums.Gender;
import shop.example.validator.BirthConstraint;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class UserRequest {

    @Size(min = 2,message = "first name must be at least {min} characters")
    private String firstname;

    @Size(min = 2,message = "last name must be at least {min} characters")
    private String lastname;

    @NotBlank(message = "fullname is required")
    private String fullname;


    @Email(message = "Invalid email format")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$", message = "Invalid email format")
    private String email;


    @Size(min = 6, message = "Password must be at least {min} characters long")
    @Pattern(regexp="^\\S+$",message = "Password must not contain spaces")
    private String password;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotBlank(message = "address is required")
    private String address;

    @BirthConstraint(min=18,message = "your age must be at least {min} ages")
    private LocalDate dateOfBirth;
}
