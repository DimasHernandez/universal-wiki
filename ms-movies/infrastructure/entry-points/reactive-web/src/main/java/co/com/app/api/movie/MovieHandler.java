package co.com.app.api.movie;

import co.com.app.api.movie.mapper.MapperMovie;
import co.com.app.usecase.movie.MovieUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MovieHandler {

    private final MovieUseCase movieUseCase;

    private final MapperMovie mapperMovie;

    public Mono<ServerResponse> getMovies(ServerRequest request) {
        int page = request.queryParam("page").map(Integer::parseInt).orElse(1);
        int pageSize = request.queryParam("pageSize").map(Integer::parseInt).orElse(5);

        return movieUseCase.getMovies(page, pageSize)
                .flatMap(infoMovie -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(mapperMovie.toDto(infoMovie)));
    }

    public Mono<ServerResponse> getMoviesByName(ServerRequest request) {
        int page = request.queryParam("page").map(Integer::parseInt).orElse(1);
        int pageSize = request.queryParam("pageSize").map(Integer::parseInt).orElse(20);
        String query = request.queryParam("query").map(String::trim).orElse("ramxbo");

        return movieUseCase.getMoviesByName(page, pageSize, query)
                .flatMap(infoMovie -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(mapperMovie.toDto(infoMovie)));
    }
}
