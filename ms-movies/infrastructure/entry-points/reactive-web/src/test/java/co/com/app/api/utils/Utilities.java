package co.com.app.api.utils;

import co.com.app.api.movie.dto.InfoMovieResponse;
import co.com.app.api.movie.dto.MovieResponse;
import co.com.app.model.movie.InfoMovie;
import co.com.app.model.movie.Movie;
import co.com.app.model.movie.enums.MediaTypeMovie;
import co.com.app.model.movie.enums.MovieGenre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Utilities {

    private static final List<String> MOVIE_GENRES_STRING = new ArrayList<>();
    private static final List<MovieGenre> MOVIE_GENRES = new ArrayList<>();

    public static InfoMovieResponse infoMovieResponseMock() {
        return InfoMovieResponse.builder()
                .page(1)
                .movies(moviesResponseMock())
                .totalPages(2000)
                .totalMovies(10000)
                .total(5)
                .build();
    }

    public static List<MovieResponse> moviesResponseMock() {
        MOVIE_GENRES_STRING.add(MovieGenre.ADVENTURE.getGenre());
        MOVIE_GENRES_STRING.add(MovieGenre.ACTION.getGenre());
        MOVIE_GENRES_STRING.add(MovieGenre.DRAMA.getGenre());
        MOVIE_GENRES_STRING.add(MovieGenre.MYSTERY.getGenre());

        return List.of(
                new MovieResponse(1L, "Batman", "Batman", "Batman summary", "urlImageBatman",
                        MediaTypeMovie.MOVIE.getType(), MOVIE_GENRES_STRING, LocalDate.now().plusDays(7)),
                new MovieResponse(2L, "Superman", "Superman", "Superman summary", "urlImageSuperman",
                        MediaTypeMovie.MOVIE.getType(), MOVIE_GENRES_STRING, LocalDate.now().plusDays(3)),
                new MovieResponse(3L, "Cars", "Cars", "Cars summary", "urlImageCars",
                        MediaTypeMovie.MOVIE.getType(), MOVIE_GENRES_STRING, LocalDate.now().plusDays(4)),
                new MovieResponse(4L, "Rambo", "Rambo", "Rambo summary", "urlImageRambo",
                        MediaTypeMovie.MOVIE.getType(), MOVIE_GENRES_STRING, LocalDate.now().plusDays(5)),
                new MovieResponse(5L, "Love", "Love", "Love summary", "urlImageLove",
                        MediaTypeMovie.MOVIE.getType(), MOVIE_GENRES_STRING, LocalDate.now().plusDays(8)));
    }

    public static InfoMovie infoMovieMock() {
        return InfoMovie.builder()
                .page(1)
                .movies(moviesMock())
                .totalPages(2000)
                .totalMovies(10000)
                .total(5)
                .build();
    }

    public static List<Movie> moviesMock() {
        MOVIE_GENRES.add(MovieGenre.ADVENTURE);
        MOVIE_GENRES.add(MovieGenre.ACTION);
        MOVIE_GENRES.add(MovieGenre.DRAMA);
        MOVIE_GENRES.add(MovieGenre.MYSTERY);

        return List.of(
                new Movie(1L, "Batman", "Batman", "Batman summary", "urlImageBatman",
                        MediaTypeMovie.MOVIE, MOVIE_GENRES, LocalDate.now().plusDays(7)),
                new Movie(2L, "Superman", "Superman", "Superman summary", "urlImageSuperman",
                        MediaTypeMovie.MOVIE, MOVIE_GENRES, LocalDate.now().plusDays(3)),
                new Movie(3L, "Cars", "Cars", "Cars summary", "urlImageCars",
                        MediaTypeMovie.MOVIE, MOVIE_GENRES, LocalDate.now().plusDays(4)),
                new Movie(4L, "Rambo", "Rambo", "Rambo summary", "urlImageRambo",
                        MediaTypeMovie.MOVIE, MOVIE_GENRES, LocalDate.now().plusDays(5)),
                new Movie(5L, "Love", "Love", "Love summary", "urlImageLove",
                        MediaTypeMovie.MOVIE, MOVIE_GENRES, LocalDate.now().plusDays(8)));
    }
}
