package co.com.bancolombia.api.auth.config;

import co.com.bancolombia.api.auth.jwt.JWTAuthenticationManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityContextRepository implements ServerSecurityContextRepository {

    private static final String TOKEN_PREFIX = "Bearer ";

    private final JWTAuthenticationManager manager;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        log.info("Attempting to save security context {}", context);
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(header -> header.startsWith(TOKEN_PREFIX))
                .flatMap(token -> {
                    log.info("Attempting to load security context {}", token);
                    String tokenValue = token.substring(TOKEN_PREFIX.length());
                    UsernamePasswordAuthenticationToken userAuth =
                            new UsernamePasswordAuthenticationToken(tokenValue, tokenValue, null);
                    return manager.authenticate(userAuth)
                            .map(SecurityContextImpl::new);
                });
    }
}

