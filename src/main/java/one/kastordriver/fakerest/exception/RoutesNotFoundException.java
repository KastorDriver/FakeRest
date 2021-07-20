package one.kastordriver.fakerest.exception;

/**
 * Created by Kastor on 13.03.2017.
 */
public class RoutesNotFoundException extends RuntimeException {
    public RoutesNotFoundException(String message) {
        super(message);
    }
}
