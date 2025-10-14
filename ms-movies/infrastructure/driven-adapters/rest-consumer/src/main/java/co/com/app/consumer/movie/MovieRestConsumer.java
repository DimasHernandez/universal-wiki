package co.com.app.consumer.movie;

import co.com.app.model.movie.InfoMovie;
import co.com.app.model.movie.Movie;
import co.com.app.model.movie.gateways.MovieRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
//@RequiredArgsConstructor
@Slf4j
public class MovieRestConsumer implements MovieRepository {

    private final WebClient client;
    private final String apiKey;

    public MovieRestConsumer(WebClient client, @Value("${app.apikey}") String apiKey) {
        this.client = client;
        this.apiKey = apiKey;
    }

    // pageable 1, 5, 10, 20
    @CircuitBreaker(name = "testGet", fallbackMethod = "moviesFallback")
    @Override
    public Mono<InfoMovie> getMovies(int page, int pageSize) {

        // page 4
        // pageSize = 10
        // offset = 10 * (4 - 1) = 30
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("api_key", apiKey);
        queryParams.add("page", "1"); // -> 20

        if (pageSize == 20) {
            queryParams.set("page", String.valueOf(pageSize));
            return getInfoMovie(queryParams);
        }

        return getInfoMovie(queryParams);
    }

    @NotNull
    private Mono<InfoMovie> getInfoMovie(MultiValueMap<String, String> queryParams) {
        return client
                .get()
                .uri(uriBuilder -> uriBuilder.path("/trending/movie/week")
                        .queryParams(queryParams).build())
                .retrieve()
                .bodyToMono(InfoMovie.class);
    }

    public Mono<InfoMovie> moviesFallback() {
        log.error("Fallback Movies");
        return Mono.just(new InfoMovie());
    }


}
