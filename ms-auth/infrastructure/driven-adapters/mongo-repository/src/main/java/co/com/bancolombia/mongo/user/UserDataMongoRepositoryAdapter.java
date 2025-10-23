package co.com.bancolombia.mongo.user;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.enums.Role;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.mongo.helper.AdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class UserDataMongoRepositoryAdapter extends AdapterOperations<User, UserData, String, UserDataMongoDBRepository>
        implements UserRepository {

    public UserDataMongoRepositoryAdapter(UserDataMongoDBRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, User.class));
    }

    @Override
    public Mono<User> saveUser(User user) {
        List<String> roles = user.getRoles().stream()
                .map(Role::getValue)
                .toList();
        UserData data = this.toData(user);
        data.setRoles(roles);
        return repository.save(data)
                .map(this::getUser);
    }

    @Override
    public Mono<User> findByUsername(String username) {
        return repository.findByUsername(username)
                .map(this::getUser);
    }

    @Override
    public Mono<Boolean> existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    private User getUser(UserData userData) {
        Set<Role> rolesEntity = userData.getRoles().stream()
                .map(Role::fromValue)
                .collect(Collectors.toSet());
        User entity = toEntity(userData);
        entity.setRoles(rolesEntity);
        log.info("Entity from database: {}", entity);
        return entity;
    }

}
