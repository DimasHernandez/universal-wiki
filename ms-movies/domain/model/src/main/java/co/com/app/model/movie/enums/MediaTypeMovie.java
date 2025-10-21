package co.com.app.model.movie.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
@Getter
public enum MediaTypeMovie {
    MOVIE("movie"),
    TV("tv");

    private final String type;

    public static MediaTypeMovie getByTypeFromString(String type) {
        return Stream.of(MediaTypeMovie.values())
                .filter(value -> value.getType().equals(type))
                .findFirst()
                .orElse(MOVIE);
    }
}
