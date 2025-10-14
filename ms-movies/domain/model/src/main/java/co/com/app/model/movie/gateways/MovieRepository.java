package co.com.app.model.movie.gateways;

import co.com.app.model.movie.InfoMovie;
import reactor.core.publisher.Mono;

public interface MovieRepository {

    Mono<InfoMovie> getMovies(int page, int pageSize);
}
