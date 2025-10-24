package co.com.bancolombia.api.auth.mapper;

import co.com.bancolombia.api.auth.dto.UserAuth;
import co.com.bancolombia.api.auth.dto.UserRequest;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.enums.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", source = "roles", qualifiedByName = "toRoles")
    User toEntity(UserRequest userRequest);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserAuth userAuth);

    UserAuth toAuth(User user);

    @Named("toRoles")
    default Set<Role> toRoles(Set<String> roles) {
        return roles.stream()
                .map(Role::fromValue)
                .collect(Collectors.toSet());
    }
}