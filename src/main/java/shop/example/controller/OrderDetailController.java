package shop.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.example.dto.response.order.OrderDetailResponse;
import shop.example.dto.response.common.ResponseData;
import shop.example.service.OrderDetailService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "OrderDetail Controller")
@RequestMapping("${api.prefix}/orderDetails")
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    @GetMapping("/order/{orderId}")
    @Operation(summary = "get orderDetails by orderId",description = "API for get orderDetails by orderId")
    public ResponseEntity<ResponseData<List<OrderDetailResponse>>> getOrderDetails(@PathVariable("orderId")
                                                                                     Long orderId){
        return ResponseEntity.ok(ResponseData.<List<OrderDetailResponse>>builder()
                        .status(HttpStatus.OK.value())
                        .message("success")
                        .data(orderDetailService.getOrdersDetail(orderId))
                .build());
    }



}
