package co.com.app.model.ex;

public class ObjectErrorException extends RuntimeException
{
    public ObjectErrorException(String message) {
        super(message);
    }

    public ObjectErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
