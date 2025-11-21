package co.com.app.model.movie.gateways;

import co.com.app.model.movie.InfoMovie;
import reactor.core.publisher.Mono;

public interface MovieRepository {

    // pageable 1, 5, 10, 20
    Mono<InfoMovie> getMovies(double page, double pageSize, String query);
}
