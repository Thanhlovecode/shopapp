package shop.example.dto.response.user;

import lombok.*;
import shop.example.enums.Gender;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String fullname;
    private String email;
    private String firstname;
    private String lastname;
    private Gender gender;
    private String address;
    private LocalDate dateOfBirth;
}
