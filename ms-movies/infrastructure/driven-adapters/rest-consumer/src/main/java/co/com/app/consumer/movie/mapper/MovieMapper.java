package co.com.app.consumer.movie.mapper;

import co.com.app.consumer.movie.dto.InfoMovieResponse;
import co.com.app.consumer.movie.dto.MovieResponse;
import co.com.app.model.movie.InfoMovie;
import co.com.app.model.movie.Movie;
import co.com.app.model.movie.enums.MediaTypeMovie;
import co.com.app.model.movie.enums.MovieGenre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    String IMAGE_URL = "https://image.tmdb.org/t/p/w342";

    @Mapping(target = "movies", source = "results", qualifiedByName = "toMovies")
    @Mapping(target = "totalMovies", source = "totalResults")
    InfoMovie toEntity(InfoMovieResponse response);

    @Named("toMovies")
    default List<Movie> toMovies(List<MovieResponse> results) {
        return results.stream().map(result -> {
                    List<MovieGenre> movieGenres = result.getGenres().stream()
                            .map(MovieGenre::fromId).toList();
                    return Movie.builder()
                            .id(result.getId())
                            .title(result.getTitle())
                            .originalTitle(result.getOriginalTitle())
                            .overview(result.getOverview())
                            .posterPath(IMAGE_URL + result.getPosterPath())
                            .mediaType(MediaTypeMovie.getByTypeFromString(result.getMediaType()))
                            .genres(movieGenres)
                            .releaseDate(result.getReleaseDate())
                            .build();
                })
                .toList();
    }
}
