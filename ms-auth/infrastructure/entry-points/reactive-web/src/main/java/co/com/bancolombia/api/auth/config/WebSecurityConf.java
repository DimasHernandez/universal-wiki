package co.com.bancolombia.api.auth.config;

import co.com.bancolombia.api.auth.jwt.JWTAuthenticationManager;
import co.com.bancolombia.model.user.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@RequiredArgsConstructor
@EnableReactiveMethodSecurity()
@Configuration
public class WebSecurityConf {

    private final JWTAuthenticationManager authenticationManager;

    private final SecurityContextRepository securityContextRepository;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        return http
                .exceptionHandling(getExceptionHandlingSpecCustomizer())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .logout(ServerHttpSecurity.LogoutSpec::disable)
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange(getAuthorizeExchangeSpecCustomizer())
                .build();
    }

    private static Customizer<ServerHttpSecurity.AuthorizeExchangeSpec> getAuthorizeExchangeSpecCustomizer() {
        return authorizeExchangeSpec ->
                authorizeExchangeSpec
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll() //TODO: Revisar la dependencia con el front. (El front funciona sin esta linea?   )
                        .pathMatchers(HttpMethod.POST, "/login", "/signup").permitAll()// TODO: Mejorar pasar rutas desde el application en una List<String>
                        .pathMatchers(HttpMethod.POST, "/add-permissions", "/remove-permissions").hasRole(Role.ROLE_ADMIN.getValue())
                        .anyExchange().authenticated();
    }

    private static Customizer<ServerHttpSecurity.ExceptionHandlingSpec> getExceptionHandlingSpecCustomizer() {
        return exHandlingSpec ->
                exHandlingSpec.authenticationEntryPoint((exchange, denied) ->
                                Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
                        .accessDeniedHandler((exchange, denied) ->
                                Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN)));
    }
}