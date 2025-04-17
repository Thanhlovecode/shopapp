package shop.example.service;

import jakarta.servlet.http.HttpServletRequest;
import shop.example.dto.response.payment.PaymentResponse;


public interface PaymentService {
    String createPaymentUrl(Long orderId, HttpServletRequest request);
    PaymentResponse processPaymentResult(HttpServletRequest request);
}
