package project.notice.domain.file.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileDto {

    private Long id;

    private String realFileName;

    private String storeFileName;

}
