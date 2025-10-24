package co.com.bancolombia.api.auth;


import co.com.bancolombia.api.auth.dto.LoginRequest;
import co.com.bancolombia.api.auth.dto.SignupResponse;
import co.com.bancolombia.api.auth.dto.TokenResponse;
import co.com.bancolombia.api.auth.dto.UserRequest;
import co.com.bancolombia.api.auth.jwt.JWTUtil;
import co.com.bancolombia.api.auth.jwt.PBKDF2Encoder;
import co.com.bancolombia.api.auth.mapper.UserMapper;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.ex.AuthGeneralException;
import co.com.bancolombia.model.user.ex.JWTInvalidCredential;
import co.com.bancolombia.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthHandler {

    private final UserUseCase userUseCase;
    private final JWTUtil jwtUtil;
    private final PBKDF2Encoder passwordEncoder;
    private final UserMapper userMapper;

    public Mono<ServerResponse> signup(ServerRequest request) {
        log.info("signup request received");
        return request
                .bodyToMono(UserRequest.class)
                .flatMap(this::processSignupRequest)
                .flatMap(message -> ServerResponse
                        .created(URI.create("/signup"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(message));
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        return request
                .bodyToMono(LoginRequest.class)
                .flatMap(this::processRequestLogin)
                .flatMap(tokenResponse -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(tokenResponse));
    }

    private Mono<SignupResponse> processSignupRequest(UserRequest userRequest) {
        return userUseCase.existsByUsername(userRequest.username())
                .flatMap(userExist -> {
                    log.info("Existing user: {}", userExist);
                    if (userExist) {
                        return Mono.error(new AuthGeneralException("Invalid User"));
                    }
                    return Mono.just(userRequest)
                            .flatMap(userReq -> {
                                        User userEntity = userMapper.toEntity(userReq);
                                        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
                                        return userUseCase.saveUser(userEntity)
                                                .doOnNext(u -> log.info("User saved successfully. User_id: {}", u.getId()));
                                    }
                            )
                            .thenReturn(new SignupResponse("User signed up successfully"));
                });
    }

    private Mono<TokenResponse> processRequestLogin(LoginRequest auth) {
        return userUseCase
                .findByUsername(auth.username())
                .map(userMapper::toAuth)
                .filter(userAuth -> passwordEncoder.matches(auth.password(), userAuth.getPassword()))
                .map(userAuth -> {
                    User user = userMapper.toEntity(userAuth);
                    return new TokenResponse(jwtUtil.generateAccessToken(user), jwtUtil.generateRefreshToken(user));
                })
                .doOnNext(l -> log.info("Generate access and refresh token successfully."))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new JWTInvalidCredential("Invalid username or password"))));
    }
}
