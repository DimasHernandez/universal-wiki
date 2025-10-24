package co.com.bancolombia.mongo.user;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import reactor.core.publisher.Mono;

public interface UserDataMongoDBRepository extends ReactiveMongoRepository<UserData, String>, ReactiveQueryByExampleExecutor<UserData> {

    Mono<UserData> findByUsername(String username);

    Mono<Boolean> existsByUsername(String username);
}
