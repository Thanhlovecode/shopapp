package shop.example.dto.response.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentResponse {
    private String requestId; // ma hoa don
    private String message;
    private String transactionNo; // ma giao dich
    private Long amount;

}
