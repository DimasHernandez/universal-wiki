package co.com.app.consumer.movie;

import co.com.app.consumer.movie.dto.InfoMovieResponse;
import co.com.app.consumer.movie.errorclient.ErrorClientMovie;
import co.com.app.consumer.movie.mapper.MovieMapper;
import co.com.app.model.movie.InfoMovie;
import co.com.app.model.movie.Movie;
import co.com.app.model.movie.gateways.MovieRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Service
//@RequiredArgsConstructor
@Slf4j
public class MovieRestConsumer implements MovieRepository {

    public static final int PAGE_SIZE_API_EX = 20;
    public static final String QUERY_PARAM_API_KEY = "api_key";
    public static final String QUERY_PARAM_PAGE = "page";

    private final WebClient client;
    private final String apiKey;
    private final MovieMapper movieMapper;

    public MovieRestConsumer(WebClient client, @Value("${app.apikey}") String apiKey, MovieMapper movieMapper) {
        this.client = client;
        this.apiKey = apiKey;
        this.movieMapper = movieMapper;
    }

    private static InfoMovie getInfoMovie(double page, double pageSize, InfoMovie infoMovie, int pageApi) {
        double offset = ((PAGE_SIZE_API_EX - pageSize) - ((pageApi * PAGE_SIZE_API_EX) - (page * pageSize)));
        List<Movie> movies = infoMovie.getMovies().stream()
                .skip((long) offset)
                .limit((long) pageSize)
                .toList();

        return InfoMovie.builder()
                .page((int) page)
                .movies(movies)
                .totalPages((int) (infoMovie.getTotalMovies() / pageSize))
                .totalMovies(infoMovie.getTotalMovies())
                .total(movies.size())
                .build();
    }

    @CircuitBreaker(name = "testGet", fallbackMethod = "moviesFallback")
    @Override
    public Mono<InfoMovie> getMovies(double page, double pageSize) {

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add(QUERY_PARAM_API_KEY, apiKey);

        int pageApi = calculationPage(page, pageSize, queryParams);
        return client
                .get()
                .uri(uriBuilder -> uriBuilder.path("/trending/movie/week")
                        .queryParams(queryParams).build())
                .retrieve()
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals,
                        clientResponse -> clientResponse.bodyToMono(ErrorClientMovie.class)
                                .flatMap(errorClientMovie ->
                                        Mono.error(new RuntimeException(errorClientMovie.status_message()))))
                .bodyToMono(InfoMovieResponse.class)
                .doOnNext(infoMovieResponse -> log.info("InfoMovieResponse: {}", infoMovieResponse.toString()))
                .map(movieMapper::toEntity)
                .doOnNext(infoMovie -> log.info("InfoMovie {}", infoMovie))
                .map(infoMovie -> getInfoMovie(page, pageSize, infoMovie, pageApi));
    }

    private int calculationPage(double page, double pageSize, MultiValueMap<String, String> queryParams) {
        double expPageNext = (page * pageSize) / PAGE_SIZE_API_EX;
        int newPage = (int) Math.ceil(expPageNext);
        queryParams.set(QUERY_PARAM_PAGE, String.valueOf(newPage));
        return newPage;
    }

    // Resilience
    public Mono<InfoMovie> moviesFallback(double code, double value, Exception ex) {
        log.info("Fallback movie code {}, value {}", code, value);
        log.error("Fallback Movies {}", ex.getMessage());
        return Mono.just(InfoMovie.builder()
                .page(0)
                .movies(Collections.emptyList())
                .totalPages(0)
                .totalMovies(0)
                .total(0)
                .build());
    }


}
