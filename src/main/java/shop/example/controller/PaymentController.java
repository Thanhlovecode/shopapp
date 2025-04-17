package shop.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.example.dto.response.payment.PaymentResponse;
import shop.example.dto.response.payment.VnPayResponse;
import shop.example.service.PaymentService;

@RestController
@Slf4j
@RequestMapping("api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create/{orderId}")
    public ResponseEntity<VnPayResponse> createPaymentUrl(@PathVariable("orderId") Long orderId,
                                                          HttpServletRequest request){
        return ResponseEntity.ok(VnPayResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message("Success")
                        .paymentUrl(paymentService.createPaymentUrl(orderId,request))
                .build());
    }

    @GetMapping("/vnPay-payment-return")
    public ResponseEntity<PaymentResponse> verifyPayment(HttpServletRequest request){
        return ResponseEntity.ok(paymentService.processPaymentResult(request));
    }
}
