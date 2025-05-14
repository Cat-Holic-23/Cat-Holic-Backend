package moodi.backend.exception;

public class RetryableServerException extends RuntimeException {
    public RetryableServerException(String message) {
        super(message);
    }

    public RetryableServerException(String message, Throwable cause) {
        super(message, cause);
    }

}