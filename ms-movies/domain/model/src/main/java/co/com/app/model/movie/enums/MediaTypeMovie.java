package co.com.app.model.movie.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MediaTypeMovie {
    MOVIE("movie"),
    TV("tv");

    private final String type;
}
