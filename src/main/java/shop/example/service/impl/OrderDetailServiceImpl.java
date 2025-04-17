package shop.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.example.converter.DataConverter;
import shop.example.dto.response.order.OrderDetailResponse;
import shop.example.repository.OrderDetailRepository;
import shop.example.service.OrderDetailService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final DataConverter orderDetailConvert;

    @Override
    public List<OrderDetailResponse> getOrdersDetail(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId)
                .stream()
                .map(orderDetailConvert::convertOrderDetailEntity)
                .toList();
    }
}
