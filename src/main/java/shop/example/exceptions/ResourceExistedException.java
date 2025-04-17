package shop.example.exceptions;


public class ResourceExistedException extends RuntimeException{
    public ResourceExistedException(String message) {
        super(message);
    }
}
