package antifraud.Exceptions.User;

import org.springframework.http.HttpStatus;

public class UserRoleException extends RuntimeException {

    public final HttpStatus status;

    public UserRoleException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
