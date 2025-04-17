package shop.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import shop.example.dto.response.common.ErrorResponse;
import shop.example.enums.ErrorCode;

import java.io.IOException;
import java.util.Date;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException{
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse.builder()
                .timestamp(new Date())
                .status(ErrorCode.UNAUTHENTICATED.getStatus().value())
                .error(ErrorCode.UNAUTHENTICATED.getMessage())
                .message(ErrorCode.UNAUTHENTICATED.getMessage())
                .build()));
        response.flushBuffer();
    }
}
