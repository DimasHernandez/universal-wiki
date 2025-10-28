package co.com.edge.routefilter;

import co.com.edge.usecase.validation.ValidationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilterRequest implements GlobalFilter {

    private static final String CUSTOM_HEADER = "X-MS-TYPE";

    private static final String BEARER_PREFIX = "Bearer ";

    private final ValidationUseCase validationUseCase;

    private static boolean getCustomHeader(ServerWebExchange exchange) {
        String customHeader = exchange.getRequest().getHeaders().getFirst(CUSTOM_HEADER);

        return Objects.nonNull(customHeader) && !customHeader.trim().isEmpty();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        boolean isValidCustomHeader = getCustomHeader(exchange);

        if (!isValidCustomHeader) {
            return Mono.error(new IllegalArgumentException("Custom header is empty"));
        }

        String customHeader = exchange.getRequest().getHeaders().getFirst(CUSTOM_HEADER);

        return Mono.justOrEmpty(customHeader)
                .flatMap(value ->
                        switch (value.toLowerCase()) {
                            case "auth,movie" -> {
                                Optional<String> tokenOpt = Optional.ofNullable(exchange.getRequest().getHeaders()
                                        .getFirst("Authorization"));

                                if (tokenOpt.isEmpty()) {
                                    yield Mono.error(new IllegalArgumentException("Authorization header is empty"));
                                }

                                String token = tokenOpt.get().substring(BEARER_PREFIX.length());
                                yield validationUseCase.validateToken(token)
                                        .flatMap(isValid -> {
                                            if (!isValid) {
                                                return Mono.error(new IllegalArgumentException("No Authorized"));
                                            }
                                            return chain.filter(exchange);
                                        });
                            }
                            case "auth" -> chain.filter(exchange);
                            default -> Mono.error(new IllegalArgumentException("Unsupported header"));
                        });
    }
}
