package co.com.bancolombia.api.auth.jwt;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTAuthenticationManager implements ReactiveAuthenticationManager {

    private static final String ROLES = "roles";

    private final JWTUtil jwtUtil;

    @SuppressWarnings("unchecked")
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        String username = jwtUtil.extractUsername(token);
        return Mono.fromCallable(() -> jwtUtil.validateToken(token, username))
                .filter(Boolean::booleanValue)
                .switchIfEmpty(Mono.defer(Mono::empty))
                .map(ignored -> {
                    Claims claims = jwtUtil.extractAllClaims(token);
                    List<String> roles = claims.get(ROLES, List.class);
                    return new UsernamePasswordAuthenticationToken(username,
                            null,
                            roles.stream().map(SimpleGrantedAuthority::new).toList());
                });
    }
}
