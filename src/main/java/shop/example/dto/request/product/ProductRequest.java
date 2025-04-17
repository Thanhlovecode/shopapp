package shop.example.dto.request.product;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductRequest {
    @NotBlank(message = "Product name cannot be empty")
    @Size(min = 3, max = 100, message = "Product name must be between 3 and 100 characters")
    private String name;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.1", message = "Price must be greater than 0")
    private double price;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private Long categoryId;
}
