package shop.example.enums;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum ErrorCode {
    USER_EXISTED("User existed",HttpStatus.CONFLICT),
    USER_NOT_FOUND("User not found",HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND("category not found",HttpStatus.NOT_FOUND),
    CATEGORY_EXISTED("Category existed",HttpStatus.CONFLICT),
    PRODUCT_NOT_FOUND("category not found",HttpStatus.NOT_FOUND),
    PRODUCT_EXISTED("product existed",HttpStatus.CONFLICT),
    PRODUCT_NOT_ENOUGH("Not enough stock for product",HttpStatus.BAD_REQUEST),
    ORDER_NOT_FOUND("order not found",HttpStatus.NOT_FOUND),
    UNAUTHENTICATED("Unauthenticated",HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("You don't have permission",HttpStatus.FORBIDDEN),
    TOKEN_INVALID("Token invalid",HttpStatus.UNAUTHORIZED),
    GENDER_INVALID("Gender invalid",HttpStatus.BAD_REQUEST);



    private final String message;
    private final HttpStatus status;

    ErrorCode(String message,HttpStatus status){
        this.message=message;
        this.status=status;
    }

}
