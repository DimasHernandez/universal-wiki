package co.com.app.consumer.movie.utils;

import co.com.app.model.movie.InfoMovie;
import co.com.app.model.movie.Movie;
import co.com.app.model.movie.enums.MediaTypeMovie;
import co.com.app.model.movie.enums.MovieGenre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Utilities {

    private static final List<MovieGenre> movieGenres = new ArrayList<>();

    public static InfoMovie infoMovieMock() {
        return InfoMovie.builder()
                .page(1)
                .movies(moviesMock())
                .totalPages(200)
                .totalMovies(1000)
                .total(5)
                .build();
    }

    public static List<Movie> moviesMock() {
        movieGenres.add(MovieGenre.ADVENTURE);
        movieGenres.add(MovieGenre.ACTION);
        movieGenres.add(MovieGenre.DRAMA);
        movieGenres.add(MovieGenre.MYSTERY);

        return List.of(
                new Movie(1L, "Batman", "Batman", "Batman summary", "urlImageBatman",
                        MediaTypeMovie.MOVIE, movieGenres, LocalDate.now().plusDays(7)),
                new Movie(2L, "Superman", "Superman", "Superman summary", "urlImageSuperman",
                        MediaTypeMovie.MOVIE, movieGenres, LocalDate.now().plusDays(3)),
                new Movie(3L, "Cars", "Cars", "Cars summary", "urlImageCars",
                        MediaTypeMovie.MOVIE, movieGenres, LocalDate.now().plusDays(4)),
                new Movie(4L, "Rambo", "Rambo", "Rambo summary", "urlImageRambo",
                        MediaTypeMovie.MOVIE, movieGenres, LocalDate.now().plusDays(5)),
                new Movie(5L, "Love", "Love", "Love summary", "urlImageLove",
                        MediaTypeMovie.MOVIE, movieGenres, LocalDate.now().plusDays(8)));
    }

    public static InfoMovie infoMovieErrorMock() {
        return InfoMovie.builder()
                .page(0)
                .movies(Collections.emptyList())
                .totalPages(0)
                .totalMovies(0)
                .total(0)
                .build();
    }
}
