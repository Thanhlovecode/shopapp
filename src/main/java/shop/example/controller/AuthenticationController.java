package shop.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.example.dto.request.auth.AuthenticationRequest;
import shop.example.dto.request.auth.RefreshTokenRequest;
import shop.example.dto.response.auth.AuthenticationResponse;
import shop.example.dto.response.common.ResponseData;
import shop.example.service.AuthenticationService;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Authenticated Controller")
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/log-in")
    @Operation(summary = "log in", description = "API for log in")
    public ResponseEntity<ResponseData<AuthenticationResponse>> logIn(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authenticationService.logIn(request);
        log.info("Log in successfully");
        return ResponseEntity.ok(ResponseData.<AuthenticationResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Log in successfully")
                .data(response)
                .build());
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "refresh token", description = "API for refresh token")
    public ResponseEntity<ResponseData<AuthenticationResponse>> refreshToken(@RequestBody RefreshTokenRequest request){
        AuthenticationResponse response = authenticationService.refreshToken(request);
        log.info("Refresh token successfully");
        return ResponseEntity.ok(ResponseData.<AuthenticationResponse>builder()
                .status(HttpStatus.OK.value())
                .message("refresh-token successfully")
                .data(response)
                .build());
    }

    @PostMapping("/log-out")
    @Operation(summary = "log out", description = "API for log out")
    public ResponseEntity<ResponseData<?>> logOut(@RequestBody RefreshTokenRequest request){
        authenticationService.logOut(request);
        log.info("Log out successfully");
        return ResponseEntity.ok(ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Log out successfully")
                .build());
    }
}
