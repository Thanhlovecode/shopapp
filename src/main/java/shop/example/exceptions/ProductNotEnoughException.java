package shop.example.exceptions;

public class ProductNotEnoughException extends RuntimeException{
    public ProductNotEnoughException(String message) {
        super(message);
    }
}
