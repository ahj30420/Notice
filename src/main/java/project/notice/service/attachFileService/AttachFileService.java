package project.notice.service.attachFileService;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import project.notice.domain.file.dto.FileDto;
import project.notice.domain.file.dto.UploadFile;

import java.net.MalformedURLException;
import java.util.List;

public interface AttachFileService {
    void register(Long noticeId, List<UploadFile> uploadFiles);

    List<FileDto> findByNoticeId(Long noticeId);

    void deleteFile(Long fileId, String storeFileName);

    ResponseEntity<Resource> downloadAttach(Long fileId) throws MalformedURLException;
}
