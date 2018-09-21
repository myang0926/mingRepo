package exceptions;

import org.springframework.core.NestedRuntimeException;

public class LoginFailedException extends NestedRuntimeException {

    public LoginFailedException(String msg) {
        super(msg);
    }

    public LoginFailedException(String msg, Throwable cause) {
        super(msg, cause);
    }

}