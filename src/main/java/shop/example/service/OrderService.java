package shop.example.service;

import org.springframework.data.domain.Pageable;
import shop.example.dto.request.order.OrderRequest;
import shop.example.dto.response.order.OrderResponse;
import shop.example.dto.response.common.PageResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest orderRequest,String requestID);
    PageResponse<List<OrderResponse>> getOrders(Long userId,Pageable pageable);
    OrderResponse getOrder(Long orderId);
    void deleteOrder(Long orderId);
    void cancelOrder(Long orderId);
}
