package co.com.app.consumer.movie.errorclient;

public record ErrorClientMovie(
        Boolean success,
        int status_code,
        String status_message
) {
}
