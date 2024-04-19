package project.notice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DuplicatedNameException extends RuntimeException{
    private String errorMessage;
}
