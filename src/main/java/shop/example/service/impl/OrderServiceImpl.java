package shop.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.example.converter.DataConverter;
import shop.example.domain.Order;
import shop.example.domain.OrderDetail;
import shop.example.domain.Product;
import shop.example.dto.request.order.OrderRequest;
import shop.example.dto.request.order.ProductOrderRequest;
import shop.example.dto.response.order.OrderDetailResponse;
import shop.example.dto.response.order.OrderResponse;
import shop.example.dto.response.common.PageResponse;
import shop.example.enums.ErrorCode;
import shop.example.enums.PaymentStatus;
import shop.example.exceptions.ProductNotEnoughException;
import shop.example.exceptions.ResourceNotFoundException;
import shop.example.exceptions.UserNotFoundException;
import shop.example.repository.OrderRepository;
import shop.example.repository.ProductRepository;
import shop.example.repository.UserRepository;
import shop.example.service.OrderService;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final DataConverter orderConverter;


    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest,String requestID) {
        if (!userRepository.existsById(orderRequest.getUserId())) {
            throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND.getMessage());
        }
        var userProxy = userRepository.getReferenceById(orderRequest.getUserId());

        Order order = Order.builder()
                .user(userProxy)
                .orderDate(new Date())
                .requestId(requestID)
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        Map<Long, Product> productMap = productRepository.findAllById(
                orderRequest.getProducts().stream().map(ProductOrderRequest::getProductId).toList()
        ).stream().collect(Collectors.toMap(Product::getId, product -> product));

        List<OrderDetail> orderDetails = orderRequest.getProducts()
                .stream()
                .map(productOrderRequest -> createOrderDetail(productOrderRequest, order,productMap))
                .toList();

        order.setOrderDetails(orderDetails);
        order.setTotalAmount(orderDetails.stream().mapToDouble(OrderDetail::getTotalPrice).sum());
        orderRepository.save(order);
        return mapToOrderResponse(order, orderDetails);
    }

    private OrderDetail createOrderDetail(ProductOrderRequest request, Order order,Map<Long,Product> productMap){
        Product product = productMap.get(request.getProductId());

        if(product==null){
            throw new ResourceNotFoundException(ErrorCode.PRODUCT_NOT_FOUND.getMessage());
        }
        // check stockQuantity have enough for order
        int stockQuantity = product.getInventory().getStockQuantity();
        if (stockQuantity < request.getQuantity()) {
            throw new ProductNotEnoughException(ErrorCode.PRODUCT_NOT_ENOUGH.getMessage());
        }
        //update stockQuantity
        product.getInventory().setStockQuantity(stockQuantity - request.getQuantity());
        return OrderDetail.builder()
                .order(order)
                .product(product)
                .price(product.getPrice())
                .totalPrice(product.getPrice() * request.getQuantity())
                .quantity(request.getQuantity())
                .build();
    }

    @Override
    public PageResponse<List<OrderResponse>> getOrders(Long userId, Pageable pageable) {
        Page<Order> orders = orderRepository.findByUserId(userId, pageable);
        return PageResponse.<List<OrderResponse>>builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage(orders.getTotalPages())
                .totalElement(orders.getTotalElements())
                .items(orders.getContent().stream()
                        .map(order -> OrderResponse.builder()
                                .orderId(order.getId())
                                .orderDate(order.getOrderDate())
                                .totalAmount(orderConverter.formatTotalAmount(order.getTotalAmount()))
                                .build())
                        .toList())
                .build();
    }

    @Override
    public OrderResponse getOrder(Long orderId) {
        return orderConverter.convertOrderEntity(getOrderById(orderId));
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = getOrderById(orderId);
        order.getOrderDetails().forEach(orderDetail -> {
            Product product = orderDetail.getProduct();
            product.getInventory()
                    .setStockQuantity(product.getInventory().getStockQuantity() + orderDetail.getQuantity());
        });
        orderRepository.deleteById(orderId);
    }

    private Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ORDER_NOT_FOUND.getMessage()));
    }

    private OrderResponse mapToOrderResponse(Order order, List<OrderDetail> orderDetails) {
        List<OrderDetailResponse> detailResponses = orderDetails.stream()
                .map(orderDetail -> OrderDetailResponse.builder()
                        .productName(orderDetail.getProduct().getName())
                        .price(orderConverter.formatTotalAmount(orderDetail.getPrice()))
                        .quantity(orderDetail.getQuantity())
                        .totalPrice(orderConverter.formatTotalAmount(orderDetail.getTotalPrice()))
                        .build())
                .toList();

        return OrderResponse.builder()
                .orderId(order.getId())
                .orderDate(order.getOrderDate())
                .requestId(order.getRequestId())
                .paymentStatus(order.getPaymentStatus())
                .totalAmount(orderConverter.formatTotalAmount(order.getTotalAmount()))
                .orderDetails(detailResponses)
                .build();

    }

}
