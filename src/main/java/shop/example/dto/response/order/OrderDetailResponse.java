package shop.example.dto.response.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderDetailResponse {
    private String productName;
    private String price;
    private Integer quantity;
    private String totalPrice;
}
