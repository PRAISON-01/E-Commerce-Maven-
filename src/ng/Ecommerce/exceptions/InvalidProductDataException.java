package ng.Ecommerce.exceptions;

public class InvalidProductDataException extends RuntimeException {
    public InvalidProductDataException(String message) {
        super(message);
    }

    public InvalidProductDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidProductDataException(Throwable cause) {
        super(cause);
    }

    public InvalidProductDataException() {
        super();
    }
}