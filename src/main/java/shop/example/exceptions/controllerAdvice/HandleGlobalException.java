package shop.example.exceptions.controllerAdvice;

import jakarta.validation.ConstraintViolation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import shop.example.dto.response.common.ErrorResponse;
import shop.example.enums.ErrorCode;
import shop.example.exceptions.*;

import java.util.Date;
import java.util.Objects;

@ControllerAdvice
public class HandleGlobalException {

    private static final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(ResourceExistedException.class)
    ResponseEntity<ErrorResponse> handleUserExistedException(ResourceExistedException exception,
                                                             WebRequest webRequest) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.CONFLICT.value())
                .path(webRequest.getDescription(false).replace("uri=", ""))
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,
                                                                        WebRequest webRequest) {
        String message = Objects.requireNonNull(exception.getFieldError()).getDefaultMessage();
        var attributes = exception.getBindingResult().getAllErrors()
                .stream()
                .findFirst()
                .map(objectError -> objectError.unwrap(ConstraintViolation.class))
                .map(violation -> violation.getConstraintDescriptor().getAttributes())
                .orElse(null);

        String resultMessage = attributes != null
                ? Objects.requireNonNull(message)
                .replace("{" + MIN_ATTRIBUTE + "}", String.valueOf(attributes.get(MIN_ATTRIBUTE)))
                : message;

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(webRequest.getDescription(false).replace("uri=", ""))
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(resultMessage)
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler({GenderInvalidException.class})
    ResponseEntity<ErrorResponse> handleGenderInvalidException(GenderInvalidException exception,
                                                               WebRequest webRequest) {
        ErrorCode errorCode = ErrorCode.GENDER_INVALID;

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(new Date())
                .status(errorCode.getStatus().value())
                .path(webRequest.getDescription(false).replace("uri=", ""))
                .error(errorCode.getMessage())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                  WebRequest webRequest) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.NOT_FOUND.value())
                .path(webRequest.getDescription(false).replace("uri=", ""))
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ProductNotEnoughException.class)
    ResponseEntity<ErrorResponse> handleUserExistedException(ProductNotEnoughException exception,
                                                             WebRequest webRequest) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(webRequest.getDescription(false).replace("uri=", ""))
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(AuthenticatedException.class)
    ResponseEntity<ErrorResponse> handleUserExistedException(AuthenticatedException exception,
                                                             WebRequest webRequest) {
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(new Date())
                .status(errorCode.getStatus().value())
                .path(webRequest.getDescription(false).replace("uri=", ""))
                .error(errorCode.getMessage())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ErrorResponse> handleAccessDeniedExceptions(AccessDeniedException exception,
                                                               WebRequest webRequest){
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(new Date())
                .status(errorCode.getStatus().value())
                .path(webRequest.getDescription(false).replace("uri=",""))
                .error(errorCode.getMessage())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

}
