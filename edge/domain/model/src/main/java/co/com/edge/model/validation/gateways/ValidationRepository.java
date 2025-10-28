package co.com.edge.model.validation.gateways;

import co.com.edge.model.validation.Validation;
import reactor.core.publisher.Mono;

public interface ValidationRepository {

    Mono<Boolean> validate(Validation validate);
}
