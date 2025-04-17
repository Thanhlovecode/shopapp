package shop.example.dto.request.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchRequest {
    private String name;
    private Integer minPrice;
    // add different fields
}
