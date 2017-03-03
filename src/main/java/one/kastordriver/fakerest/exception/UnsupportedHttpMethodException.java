package one.kastordriver.fakerest.exception;

/**
 * Created by Kastor on 19.12.2016.
 */
public class UnsupportedHttpMethodException extends RuntimeException {
    public UnsupportedHttpMethodException(String message) {
        super(message);
    }
}
