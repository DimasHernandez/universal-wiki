package co.com.app.consumer.movie.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder()
@ToString
public class MovieResponse {

    private Long id;

    private String title;

    @JsonProperty("original_title")
    private String originalTitle;

    private String overview;

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("media_type")
    private String mediaType;

    @JsonProperty("genre_ids")
    private List<Integer> genres;

    @JsonProperty("release_date")
    private LocalDate releaseDate;
}
