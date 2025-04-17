package shop.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.example.dto.request.order.OrderRequest;
import shop.example.dto.response.order.OrderResponse;
import shop.example.dto.response.common.PageResponse;
import shop.example.dto.response.common.ResponseData;
import shop.example.service.OrderService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Order Controller")
@Slf4j
@RequestMapping("${api.prefix}/orders")
public class OrderController {

    private final OrderService orderService;


    @PostMapping
    @Operation(summary = "create new order ", description = "API for create new order")
    public ResponseEntity<ResponseData<OrderResponse>> createOrder(@RequestBody OrderRequest orderRequest,
                                                                   @RequestHeader(value = "X-request-ID")
                                                                   String requestID) {
        OrderResponse orderResponse = orderService.createOrder(orderRequest,requestID);
        log.info("order created successfully with userID: {}", orderRequest.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseData.<OrderResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("order successfully")
                .data(orderResponse)
                .build());
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "get all orders by userId", description = "API for get all orders by userId")
    public ResponseEntity<ResponseData<PageResponse<List<OrderResponse>>>> getAllOrders(@PathVariable("userId")
                                                                                        Long userId,
                                                                                        Pageable pageable) {
        return ResponseEntity.ok(ResponseData.<PageResponse<List<OrderResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .data(orderService.getOrders(userId, pageable))
                .build());
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "get order by orderId", description = "API for get order by orderId")
    public ResponseEntity<ResponseData<OrderResponse>> getOrderById(@PathVariable("orderId") Long orderId) {
        return ResponseEntity.ok(ResponseData.<OrderResponse>builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .data(orderService.getOrder(orderId))
                .build());
    }

    @DeleteMapping("/{orderId}")
    @Operation(summary = "delete order by orderId", description = "API for delete order by orderId")
    public ResponseEntity<ResponseData<?>> deleteOrder(@PathVariable("orderId") Long orderId) {
        orderService.deleteOrder(orderId);
        log.info("delete order with '{}' successfully", orderId);
        return ResponseEntity.ok(ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("delete order successfully")
                .build());
    }

    @PutMapping("/cancel/{orderId}")
    @Operation(summary = "cancel order by orderId", description = "API for cancel order by orderId")
    public ResponseEntity<ResponseData<?>> cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        log.info("Cancel order with '{}' successfully", orderId);
        return ResponseEntity.ok(ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Order is canceled successfully")
                .build());
    }

}
