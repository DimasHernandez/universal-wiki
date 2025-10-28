package co.com.edge.consumer.validate;

import co.com.edge.consumer.validate.dto.ValidateRequest;
import co.com.edge.consumer.validate.dto.ValidateResponse;
import co.com.edge.model.validation.Validation;
import co.com.edge.model.validation.gateways.ValidationRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class ValidateRestConsumer implements ValidationRepository {

    private final WebClient client;

    @Override
    @CircuitBreaker(name = "testGet")
    public Mono<Boolean> validate(Validation validate) {
        return client
                .post().uri("/validate")
                .bodyValue(ValidateRequest.builder()
                        .token(validate.getToken())
                        .build())
                .retrieve()
                .bodyToMono(ValidateResponse.class)
                // Se puede poner una excepcion personalizada en el retry
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
                        .filter(throwable -> throwable instanceof WebClientResponseException &&
                                ((WebClientResponseException) throwable).getStatusCode().is5xxServerError()))
                .map(ValidateResponse::getIsValid);
    }
}
