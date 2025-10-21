package co.com.app.api.movie;

import co.com.app.api.movie.dto.InfoMovieResponse;
import co.com.app.api.movie.mapper.MapperMovie;
import co.com.app.api.utils.Utilities;
import co.com.app.model.ex.MovieApiResponseException;
import co.com.app.model.movie.InfoMovie;
import co.com.app.usecase.movie.MovieUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieHandlerTest {

    @Mock
    private MovieUseCase movieUseCase;

    @Mock
    private MapperMovie mapperMovie;

    @Mock
    private ServerRequest request;

    @InjectMocks
    private MovieHandler movieHandler;

    @Test
    void testGetMovies() {
        InfoMovie infoMovie = Utilities.infoMovieMock();
        InfoMovieResponse infoMovieResponse = Utilities.infoMovieResponseMock();

        when(request.queryParam("page")).thenReturn(Optional.of("1"));
        when(request.queryParam("pageSize")).thenReturn(Optional.of("2"));
        when(movieUseCase.getMovies(anyInt(), anyInt())).thenReturn(Mono.just(infoMovie));
        when(mapperMovie.toDto(any(InfoMovie.class))).thenReturn(infoMovieResponse);

        Mono<ServerResponse> response = movieHandler.getMovies(request);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse ->
                        serverResponse.statusCode() == HttpStatus.OK)
                .verifyComplete();
    }

    @Test
    void testGetMoviesFailed() {
        when(request.queryParam("page")).thenReturn(Optional.of("1"));
        when(request.queryParam("pageSize")).thenReturn(Optional.of("2"));
        when(movieUseCase.getMovies(anyInt(), anyInt())).thenReturn(Mono.error(new MovieApiResponseException("Error server")));

        Mono<ServerResponse> response = movieHandler.getMovies(request);

        StepVerifier.create(response)
                .expectErrorMatches(error ->
                        error instanceof MovieApiResponseException &&
                                error.getMessage().equalsIgnoreCase("Error server"))
                .verify();
    }


}