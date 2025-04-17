package shop.example.dto.request.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductOrderRequest {
    private Long productId;
    private Integer quantity;
}
