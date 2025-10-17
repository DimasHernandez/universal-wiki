package co.com.app.api.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder()
public class InfoMovieResponse {

    private Integer page;

    private List<MovieResponse> movies;

    private Integer totalPages;

    private Integer totalMovies;

    private Integer total;
}
