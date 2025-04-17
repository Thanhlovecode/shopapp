package shop.example.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import shop.example.dto.request.payment.PaymentRequest;
import shop.example.service.VnPayService;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class VnPayServiceImpl implements VnPayService {

    @Value("${vnPay.tmnCode}")
    private String vnpTmnCode;

    @Value("${vnPay.hashSecret}")
    private String vnpHashSecret;

    @Value("${vnPay.url}")
    private String vnpUrl;

    @Value("${vnPay.returnUrl}")
    private String vnpReturnUrl;



    @Override
    public String createPaymentUrl(HttpServletRequest request, PaymentRequest paymentRequest) {

        Map<String, String> params = new TreeMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", vnpTmnCode);
        params.put("vnp_Amount", String.valueOf(paymentRequest.getAmount() * 100));
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", paymentRequest.getRequestId());
        params.put("vnp_OrderInfo", paymentRequest.getOrderInformation());
        params.put("vnp_OrderType", "250000");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", vnpReturnUrl);
        params.put("vnp_IpAddr", getIpAddress(request));
        params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

        String query = buildQuery(params);
        String secureHash = hmacSHA512(vnpHashSecret, query);
        return vnpUrl + "?" + query + "&vnp_SecureHash=" + secureHash;
    }

    public boolean verifySecureHash(HttpServletRequest request) {
        Map<String, String> fields = new TreeMap<>();
        request.getParameterMap().forEach((key, values) -> {
            if (values != null && values.length > 0 && !values[0].isEmpty()) {
                fields.put(key, values[0]);
            }
        });

        String secureHash = fields.remove("vnp_SecureHash");
        String signValue = hmacSHA512(vnpHashSecret, buildQuery(fields));
        return secureHash.equals(signValue);
    }

    private String buildQuery(Map<String, String> params) {
        return params.entrySet().stream()
                .map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
                .collect(Collectors.joining("&"));
    }

    private String hmacSHA512(String key, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512"));
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error creating HMAC SHA512", e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }

    // Lấy địa chỉ IP
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-FORWARDED-FOR");
        return (ip != null && !ip.isEmpty()) ? ip : request.getRemoteAddr();
    }

    // Mã hóa URL
    private String encode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error encoding URL", e);
        }
    }

}
