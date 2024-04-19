package project.notice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DateSettingException extends RuntimeException{
    private String errorMessage;
}
