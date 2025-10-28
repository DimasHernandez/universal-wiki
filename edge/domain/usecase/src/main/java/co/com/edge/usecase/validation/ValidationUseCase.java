package co.com.edge.usecase.validation;

import co.com.edge.model.validation.Validation;
import co.com.edge.model.validation.gateways.ValidationRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ValidationUseCase {

    private final ValidationRepository validationRepository;

    public Mono<Boolean> validateToken(String token) {
        return validationRepository.validate(Validation.builder()
                .token(token)
                .build());
    }
}
