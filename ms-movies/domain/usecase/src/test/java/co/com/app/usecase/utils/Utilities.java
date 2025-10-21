package co.com.app.usecase.utils;

import co.com.app.model.movie.InfoMovie;
import co.com.app.model.movie.Movie;
import co.com.app.model.movie.enums.MediaTypeMovie;
import co.com.app.model.movie.enums.MovieGenre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Utilities {

    private static final List<MovieGenre> MOVIE_GENRES = new ArrayList<>();

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
