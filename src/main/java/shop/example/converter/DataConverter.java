package shop.example.converter;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import shop.example.domain.*;
import shop.example.dto.request.category.CategoryRequest;
import shop.example.dto.response.category.CategoryResponse;
import shop.example.dto.response.order.OrderDetailResponse;
import shop.example.dto.response.order.OrderResponse;
import shop.example.dto.response.product.ProductResponse;
import shop.example.dto.response.review.ReviewResponse;
import shop.example.dto.response.user.UserResponse;

@Component
@RequiredArgsConstructor
public class DataConverter {
    private final ModelMapper modelMapper;

    public UserResponse convertUserResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }

    public CategoryResponse convertCategoryResponse(Category category) {
        return modelMapper.map(category, CategoryResponse.class);
    }

    public ProductResponse convertProductResponse(Product product) {
        return modelMapper.map(product, ProductResponse.class);
    }

    public Category convertCategoryEntity(CategoryRequest categoryRequest) {
        return modelMapper.map(categoryRequest, Category.class);
    }

    public ReviewResponse convertReviewResponse(Review review){
        return modelMapper.map(review, ReviewResponse.class);
    }

    public OrderResponse convertOrderEntity(Order order) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .orderDate(order.getOrderDate())
                .requestId(order.getRequestId())
                .paymentStatus(order.getPaymentStatus())
                .totalAmount(formatTotalAmount(order.getTotalAmount()))
                .build();
    }

    public OrderDetailResponse convertOrderDetailEntity(OrderDetail orderDetail){
        return OrderDetailResponse.builder()
                .productName(orderDetail.getProduct().getName())
                .price(formatTotalAmount(orderDetail.getPrice()))
                .quantity(orderDetail.getQuantity())
                .totalPrice(formatTotalAmount(orderDetail.getTotalPrice()))
                .build();
    }

    public String formatTotalAmount(double amount) {
        return String.format("%,.3f", amount).replace(',', '.').replaceAll("\\.000$", "");
    }
}
