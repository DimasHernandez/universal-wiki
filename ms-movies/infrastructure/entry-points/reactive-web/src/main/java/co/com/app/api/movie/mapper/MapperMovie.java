package co.com.app.api.movie.mapper;

import co.com.app.api.movie.dto.InfoMovieResponse;
import co.com.app.api.movie.dto.MovieResponse;
import co.com.app.model.movie.InfoMovie;
import co.com.app.model.movie.Movie;
import co.com.app.model.movie.enums.MovieGenre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MapperMovie {

    @Mapping(target = "movies", source = "movies", qualifiedByName = "toMoviesResponse")
    InfoMovieResponse toDto(InfoMovie infoMovie);

    @Named("toMoviesResponse")
    default List<MovieResponse> toMoviesResponse(List<Movie> movies) {
        return movies.stream()
                .map(movie -> {
                    List<String> moviesGenres = movie.getGenres().stream()
                            .map(MovieGenre::getGenre)
                            .toList();

                    return MovieResponse.builder()
                            .id(movie.getId())
                            .title(movie.getTitle())
                            .originalTitle(movie.getOriginalTitle())
                            .summary(movie.getOverview())
                            .urlImage(movie.getPosterPath())
                            .type(movie.getMediaType().getType())
                            .genres(moviesGenres)
                            .releaseDate(movie.getReleaseDate())
                            .build();
                }).toList();
    }
}
