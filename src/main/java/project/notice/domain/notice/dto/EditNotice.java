package project.notice.domain.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EditNotice {

    private String title;

    private String content;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

}
