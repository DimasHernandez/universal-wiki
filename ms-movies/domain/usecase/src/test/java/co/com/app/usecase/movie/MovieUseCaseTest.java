package co.com.app.usecase.movie;

import co.com.app.model.ex.MovieApiResponseException;
import co.com.app.model.movie.InfoMovie;
import co.com.app.model.movie.gateways.MovieRepository;
import co.com.app.usecase.utils.Utilities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieUseCaseTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieUseCase movieUseCase;

    @Test
    void getMovies() {
        // Arrange
        int page = 1;
        int size = 5;
        InfoMovie infoMovie = Utilities.infoMovieMock();

        when(movieRepository.getMovies(anyDouble(), anyDouble())).thenReturn(Mono.just(infoMovie));

        // Act
        Mono<InfoMovie> result = movieUseCase.getMovies(page, size);

        // Assert
        StepVerifier.create(result)
                .consumeNextWith(movie -> {
                    Assertions.assertNotNull(movie);
                    Assertions.assertEquals(infoMovie, movie);
                    Assertions.assertEquals(1000, movie.getTotalMovies());
                    Assertions.assertEquals(200, movie.getTotalPages());
                })
                .verifyComplete();
    }

    @Test
    void getMoviesFailed() {
        // Arrange
        int page = 1;
        int size = 5;

        when(movieRepository.getMovies(anyDouble(), anyDouble()))
                .thenReturn(Mono.error(new MovieApiResponseException("error")));

        // Act
        Mono<InfoMovie> result = movieUseCase.getMovies(page, size);

        // Assert
        StepVerifier.create(result)
                .expectErrorMessage("error")
                .verify();
    }
}