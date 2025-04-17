package shop.example.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.example.domain.Order;
import shop.example.dto.request.payment.PaymentRequest;
import shop.example.dto.response.payment.PaymentResponse;
import shop.example.enums.ErrorCode;
import shop.example.enums.PaymentStatus;
import shop.example.exceptions.ResourceNotFoundException;
import shop.example.repository.OrderRepository;
import shop.example.service.PaymentService;
import shop.example.service.VnPayService;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final VnPayService vnPayService;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public PaymentResponse processPaymentResult(HttpServletRequest request) {
        boolean isValid = vnPayService.verifySecureHash(request);
        String responseCode= request.getParameter("vnp_ResponseCode");
        String transactionNo = request.getParameter("vnp_TransactionNo");
        String requestId= request.getParameter("vnp_TxnRef");
        Order order=orderRepository.findByRequestId(requestId)
                .orElseThrow(()-> new ResourceNotFoundException(ErrorCode.ORDER_NOT_FOUND.getMessage()));

        boolean check = false;
        if(isValid && "00".equals(responseCode)){
            check=true;
            order.setPaymentStatus(PaymentStatus.SUCCESS);
            order.setTransactionNo(transactionNo);
        }
        else {
            order.setPaymentStatus(PaymentStatus.FAILED);
        }
        orderRepository.save(order);

        return PaymentResponse.builder()
                .requestId(requestId)
                .message(check ? "Thanh toan thanh cong" : "Thanh toan that bai")
                .transactionNo(transactionNo)
                .amount(Long.parseLong(request.getParameter("vnp_Amount")) / 100)
                .build();
    }


    @Override
    public String createPaymentUrl(Long orderId, HttpServletRequest request) {
        Order order= orderRepository.findById(orderId).
                orElseThrow(()-> new ResourceNotFoundException(ErrorCode.ORDER_NOT_FOUND.getMessage()));

        if(order.getPaymentStatus()!= PaymentStatus.PENDING){
            throw new RuntimeException("Order is not eligible for payment :"+orderId);
        }

        return vnPayService.createPaymentUrl(request,PaymentRequest.builder()
                        .amount((long)order.getTotalAmount())
                        .orderInformation("Thanh toan don hang :"+orderId)
                        .requestId(order.getRequestId())
                .build());
    }
}
