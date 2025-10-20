package co.com.app.consumer.movie;

import co.com.app.consumer.movie.dto.InfoMovieResponse;
import co.com.app.consumer.movie.mapper.MovieMapper;
import co.com.app.consumer.movie.utils.Utilities;
import co.com.app.model.ex.MovieApiResponseException;
import co.com.app.model.movie.InfoMovie;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieRestConsumerTest {

    @Mock
    private MovieMapper movieMapper;

    @Value("${app.appikey}")
    private String apiKey;

    @InjectMocks
    private MovieRestConsumer movieRestConsumer;

    private ObjectMapper mapper;

    private MockWebServer mockBackEnd;

    @BeforeEach
    void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
        var webClient = WebClient.builder()
                .baseUrl(mockBackEnd.url("/").toString())
                .build();
        movieRestConsumer = new MovieRestConsumer(webClient, apiKey, movieMapper);
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @AfterEach
    void tearDown() throws IOException {
        mockBackEnd.close();
    }

    @Test
    void shouldGetMoviesPaginateTest() throws JsonProcessingException {
        // Arrange
        double page = 1;
        double pageSize = 5;
        InfoMovie infoMovie = Utilities.infoMovieMock();
        String body = mapper.writeValueAsString(infoMovie);

        // Act
        when(movieMapper.toEntity(any(InfoMovieResponse.class))).thenReturn(infoMovie);

        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setResponseCode(HttpStatus.OK.value())
                .setBody(body));

        var response = movieRestConsumer.getMovies(page, pageSize);

        // Assert
        StepVerifier.create(response)
                .expectNextMatches(infMovie -> infMovie.getTotalMovies() == 1000
                        && !infMovie.getMovies().isEmpty()
                        && infMovie.getPage() == page)
                .verifyComplete();

    }

    @Test
    void shouldGetFieldEmptyWhenPageIsGreaterThanTwenty() throws JsonProcessingException {
        // Arrange
        double page = 1;
        double pageSize = 2001;
        InfoMovie infoMovieError = Utilities.infoMovieErrorMock();
        String body = mapper.writeValueAsString(infoMovieError);

        when(movieMapper.toEntity(any(InfoMovieResponse.class))).thenReturn(infoMovieError);

        // Act
        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setResponseCode(HttpStatus.OK.value())
                .setBody(body));
        var response = movieRestConsumer.getMovies(page, pageSize)
                .doOnNext(infoResponse -> System.out.println("Info Movie Response: " + infoResponse));

        // Assert
        StepVerifier.create(response)
                .expectErrorMatches(err -> err instanceof IllegalArgumentException
                        && err.getMessage().equalsIgnoreCase("-2000"))
                .verify();
    }

    @Test
    void shouldGetErrorExceptionServiceUnavailable() {
        // Arrange
        double page = 1;
        double pageSize = 2001;
        String body = """
                {
                "code": "12-SIE",
                "error": "unavailable"
                }
                """;

        // Act
        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setBody(body));
        var response = movieRestConsumer.getMovies(page, pageSize);

        // Assert
        StepVerifier.create(response)
                .expectErrorMatches(err -> err instanceof MovieApiResponseException)
                .verify();
    }

}