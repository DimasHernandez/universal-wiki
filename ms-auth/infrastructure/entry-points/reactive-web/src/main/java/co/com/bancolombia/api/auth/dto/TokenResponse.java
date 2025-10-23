package co.com.bancolombia.api.auth.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}