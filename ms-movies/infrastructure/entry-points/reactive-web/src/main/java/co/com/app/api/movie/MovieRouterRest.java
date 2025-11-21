package co.com.app.api.movie;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class MovieRouterRest {

    @Bean
    public RouterFunction<ServerResponse> movierouterFunction(MovieHandler movieHandler) {
        return route(GET("/api/v1/movies"), movieHandler::getMovies)
                .andRoute(GET("/api/v1/movies/search"), movieHandler::getMoviesByName);
    }
}
