package co.com.bancolombia.model.user.ex;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String username) {
        super(username);
    }
}
