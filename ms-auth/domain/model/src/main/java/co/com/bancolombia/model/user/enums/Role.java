package co.com.bancolombia.model.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Role {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_GUEST("ROLE_GUEST");

    private final String value;

    public static Role fromValue(String value) {
        return Arrays.stream(values())
                .filter(role -> role.getValue().equalsIgnoreCase(value))
                .findFirst()
                .orElse(ROLE_USER);
    }
}
