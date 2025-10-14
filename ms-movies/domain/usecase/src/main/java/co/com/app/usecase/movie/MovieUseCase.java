package co.com.app.usecase.movie;

import co.com.app.model.movie.InfoMovie;
import co.com.app.model.movie.gateways.MovieRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class MovieUseCase {

    private final MovieRepository movieRepository;

    public Mono<InfoMovie> getMovies(int page, int pageSize) {
        return movieRepository.getMovies(page, pageSize);
    }
}
