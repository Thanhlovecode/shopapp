package shop.example.dto.response.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import shop.example.enums.PaymentStatus;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderResponse {
    private Long orderId;
    private Date orderDate;
    private String totalAmount;
    private String requestId;
    private PaymentStatus paymentStatus;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<OrderDetailResponse> orderDetails;
}
