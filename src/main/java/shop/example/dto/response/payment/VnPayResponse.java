package shop.example.dto.response.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VnPayResponse {
    private int status;
    private String message;
    private String paymentUrl;
}
