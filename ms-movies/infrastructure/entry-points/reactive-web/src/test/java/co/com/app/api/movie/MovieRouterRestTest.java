package co.com.app.api.movie;

import co.com.app.api.movie.dto.InfoMovieResponse;
import co.com.app.model.movie.InfoMovie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import static co.com.app.api.utils.Utilities.infoMovieMock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieRouterRestTest {

    @InjectMocks
    MovieRouterRest movieRouterRest;

    private WebTestClient webTestClient;

    @Mock
    private MovieHandler handler;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToRouterFunction(movieRouterRest.movierouterFunction(handler)).build();
    }

    @Test
    void testGetMovieUseCase() {
        InfoMovie infoMovie = infoMovieMock();

        when(handler.getMovies(any(ServerRequest.class))).thenReturn(ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(infoMovie));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/movies").build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(InfoMovieResponse.class)
                .value(infoMovieRes -> {
                    Assertions.assertNotNull(infoMovieRes);
                    Assertions.assertEquals(1, infoMovieRes.getPage());
                    Assertions.assertFalse(infoMovieRes.getMovies().isEmpty());
                    Assertions.assertEquals(2000, infoMovieRes.getTotalPages());
                    Assertions.assertEquals(10000, infoMovieRes.getTotalMovies());
                    Assertions.assertEquals(5, infoMovieRes.getTotal());
                });
    }

    @Test
    void testGetMovieUseCaseFailed() {
        String errorMessage = """
                {
                   "error": "Internal Server Error",
                   "status": "500"
                }
                """;
        when(handler.getMovies(any(ServerRequest.class))).thenReturn(ServerResponse
                .status(500)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorMessage));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/movies").build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(String.class)
                .value(response -> {
                    Assertions.assertNotNull(response);
                    Assertions.assertEquals(errorMessage, response);
                });
    }

}