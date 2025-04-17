package shop.example.exceptions;

public class GenderInvalidException extends RuntimeException{
    public GenderInvalidException(String message) {
        super(message);
    }
}
