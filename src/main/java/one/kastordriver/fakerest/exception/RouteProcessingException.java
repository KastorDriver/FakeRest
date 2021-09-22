package one.kastordriver.fakerest.exception;

public class RouteProcessingException extends RuntimeException {

    public RouteProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
