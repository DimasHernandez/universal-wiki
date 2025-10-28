package co.com.bancolombia.api.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AuthRouterRest {

    @Bean
    public RouterFunction<ServerResponse> routerAuthFunction(AuthHandler authHandler) {
        return route(POST("/signup"), authHandler::signup)
                .andRoute(POST("/login"), authHandler::login)
                .andRoute(POST("/validate"), authHandler::validateToken);
    }
}
