package co.com.bancolombia.api.auth.dto;

public record LoginRequest(
        String username,
        String password
) {
}
