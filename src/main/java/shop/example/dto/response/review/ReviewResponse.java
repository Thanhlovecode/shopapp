package shop.example.dto.response.review;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private String comment;
    private Integer rating;
}
