package co.com.app.model.ex;

public class MovieApiResponseException extends RuntimeException {

    public MovieApiResponseException(String message) {
        super(message);
    }
}
