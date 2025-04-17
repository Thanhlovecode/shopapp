package shop.example.exceptions;

public class AuthenticatedException extends RuntimeException{
    public AuthenticatedException(String message) {
        super(message);
    }
}
