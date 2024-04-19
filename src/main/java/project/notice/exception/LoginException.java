package project.notice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginException extends RuntimeException{
    private String errorMessage;
}
