package shop.example.service;

import shop.example.dto.response.order.OrderDetailResponse;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetailResponse> getOrdersDetail(Long orderId);

}
