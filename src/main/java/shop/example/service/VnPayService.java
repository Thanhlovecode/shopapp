package shop.example.service;

import jakarta.servlet.http.HttpServletRequest;
import shop.example.dto.request.payment.PaymentRequest;

public interface VnPayService {
    String createPaymentUrl(HttpServletRequest request, PaymentRequest paymentRequest);
    boolean verifySecureHash(HttpServletRequest request);
}
