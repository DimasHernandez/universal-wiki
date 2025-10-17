package co.com.app.api.movie.dto;

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
public class MovieResponse {

    private Long id;

    private String title;

    private String originalTitle;

    private String summary;

    private String urlImage;

    private String type;

    private List<String> genres;

    private LocalDate releaseDate;

}
