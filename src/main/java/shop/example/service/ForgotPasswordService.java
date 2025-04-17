package shop.example.service;

import shop.example.dto.request.orther.ForgotPasswordRequest;
import shop.example.dto.request.orther.VerifyOTPRequest;

public interface ForgotPasswordService {
    void verifyEmail(String email);
    void verifyOTP(String email, VerifyOTPRequest otpRequest);
    void changePassword(String email, ForgotPasswordRequest request);
}
