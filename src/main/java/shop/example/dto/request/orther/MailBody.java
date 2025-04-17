package shop.example.dto.request.orther;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailBody {
    private String emailTo;
    private String subject;
    private String fullName;
    private String content;
}
