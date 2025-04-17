package shop.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.example.dto.request.orther.ForgotPasswordRequest;
import shop.example.dto.request.orther.VerifyOTPRequest;
import shop.example.dto.response.common.ResponseData;
import shop.example.service.ForgotPasswordService;

@RestController
@Tag(name = "ForgotPassword Controller")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/forgot-password")
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    @PostMapping("/verifyMail/{email}")
    @Operation(summary = "Verify email", description = "API for verify email")
    public ResponseEntity<ResponseData<?>> verifyEmail(@PathVariable("email") String email) {
        forgotPasswordService.verifyEmail(email);
        return ResponseEntity.ok(ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Verify email successfully")
                .build());
    }

    @PostMapping("/verifyOTP/{email}")
    @Operation(summary = "Verify OTP", description = "API for verify OTP")
    public ResponseEntity<ResponseData<?>> verifyOTP(@PathVariable("email") String email,
                                                     @RequestBody VerifyOTPRequest otpRequest) {
        forgotPasswordService.verifyOTP(email, otpRequest);
        return ResponseEntity.ok(ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Verify OTP successfully")
                .build());
    }

    @PostMapping("/change-password/{email}")
    @Operation(summary = "change and confirm password", description = "API for change and confirm password")
    public ResponseEntity<ResponseData<?>> handleChangePassword(@PathVariable("email") String email,
                                                                @RequestBody @Valid ForgotPasswordRequest passwordRequest) {
        forgotPasswordService.changePassword(email, passwordRequest);
        return ResponseEntity.ok(ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Changed password successfully")
                .build());
    }


}
