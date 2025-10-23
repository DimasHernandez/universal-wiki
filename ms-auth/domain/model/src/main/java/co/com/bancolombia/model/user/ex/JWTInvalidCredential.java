package co.com.bancolombia.model.user.ex;

public class JWTInvalidCredential extends RuntimeException {

    public JWTInvalidCredential(String message) {
        super(message);
    }
}
