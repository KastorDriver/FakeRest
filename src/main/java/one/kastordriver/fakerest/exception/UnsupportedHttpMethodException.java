package one.kastordriver.fakerest.exception;

public class UnsupportedHttpMethodException extends RuntimeException {
    public UnsupportedHttpMethodException(String message) {
        super(message);
    }
}
