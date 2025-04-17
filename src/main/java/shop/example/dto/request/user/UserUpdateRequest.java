package shop.example.dto.request.user;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import shop.example.enums.Gender;
import shop.example.validator.BirthConstraint;

import java.time.LocalDate;

@Getter
@Setter
public class UserUpdateRequest {
    @Size(min = 2,message = "first name must be at least {min} characters")
    private String firstname;

    @Size(min = 2,message = "last name must be at least {min} characters")
    private String lastname;

    @NotBlank(message = "fullname is required")
    private String fullname;


    @Email(message = "Invalid email format")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$", message = "Invalid email format")
    private String email;


    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotBlank(message = "address is required")
    private String address;

    @BirthConstraint(min=18,message = "your age must be at least {min} ages")
    private LocalDate dateOfBirth;
}
