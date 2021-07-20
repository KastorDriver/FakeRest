package one.kastordriver.fakerest.exception;

/**
 * Created by Kastor on 13.03.2017.
 */
public class RouteFileNotFoundException extends RuntimeException {
    public RouteFileNotFoundException(String message) {
        super(message);
    }
}
