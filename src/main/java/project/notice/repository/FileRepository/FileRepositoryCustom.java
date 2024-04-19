package project.notice.repository.FileRepository;

import project.notice.domain.file.dto.UploadFile;

import java.util.List;

public interface FileRepositoryCustom {

    void bulkSave(Long noticeId, List<UploadFile> uploadFiles);

}
