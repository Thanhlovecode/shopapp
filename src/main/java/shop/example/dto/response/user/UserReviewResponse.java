package shop.example.dto.response.user;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReviewResponse {
    private Long productId;
    private String productName;
    private String comment;
    private Integer rating;
}
