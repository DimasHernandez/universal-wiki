package co.com.app.model.movie;

import co.com.app.model.movie.enums.MediaTypeMovie;
import co.com.app.model.movie.enums.MovieGenre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder()
public class Movie {

    private Long id;

    private String title;

    private String originalTitle;

    private String overview;

    private String posterPath;

    private MediaTypeMovie mediaType;

    private List<MovieGenre> genres;

    private LocalDate releaseDate;

}
