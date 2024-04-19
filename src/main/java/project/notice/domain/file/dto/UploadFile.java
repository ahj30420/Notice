package project.notice.domain.file.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadFile {

    private String realFileName;
    private String storeFileName;

}
