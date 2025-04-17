package shop.example.dto.request.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentRequest {
    private Long amount;
    private String orderInformation;
    private String requestId;
}
