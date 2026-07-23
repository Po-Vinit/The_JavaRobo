package exception;

public class RoboException extends Exception {

    public RoboException(String message) {
        super(message);
    }

    public RoboException(String message, Throwable cause) {
        super(message, cause);
    }
}