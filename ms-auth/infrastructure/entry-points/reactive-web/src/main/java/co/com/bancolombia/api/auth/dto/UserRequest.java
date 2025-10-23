package co.com.bancolombia.api.auth.dto;

import java.util.Set;

public record UserRequest(
        String username,

        String password,

        boolean active,

        Set<String> roles
) {
}