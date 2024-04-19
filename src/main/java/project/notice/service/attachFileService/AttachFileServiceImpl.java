package project.notice.service.attachFileService;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriUtils;
import project.notice.domain.file.AttachFile;
import project.notice.domain.file.dto.FileDto;
import project.notice.domain.file.dto.UploadFile;
import project.notice.domain.notice.Notice;
import project.notice.repository.FileRepository.FileRepository;
import project.notice.util.FileStore;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AttachFileServiceImpl implements AttachFileService{

    private final FileRepository fileRepository;
    private final FileStore fileStore;

    /**
     * 첨부파일 등록
     * - 여러 첨부 파일을 하나씩 저장하면 다중 insert 발생
     * - 성능 최적화를 위해 write behind(쓰기 지연) & 벌크 연산
     * - 현재 엔티티 Id값이 Auto_Increment이므로 JPA 쓰기 지연 불가능
     * - 따라서, JdbcTemplate batchUpdate 활용하여 최적화
     */
    @Override
    public void register(Long noticeId, List<UploadFile> uploadFiles) {
        fileRepository.bulkSave(noticeId, uploadFiles);
    }


    /**
     * 첨부 파일 조회
     */
    @Override
    public List<FileDto> findByNoticeId(Long noticeId) {
        List<AttachFile> fileList = fileRepository.findByNoticeId(noticeId);
        List<FileDto> files = new ArrayList<>();

        if(fileList != null) {
            for (AttachFile file : fileList) {
                Long id = file.getId();
                String realFileName = file.getRealFileName();
                String storeFileName = file.getStoreFileName();

                files.add(new FileDto(id, realFileName, storeFileName));
            }
        }

        return files;
    }

    /**
     * 첨부 파일 삭제
     */
    public void deleteFile(Long fileId, String storeFileName){
        fileStore.deleteFile(storeFileName);
        fileRepository.deleteById(fileId);
    }

    /**
     * 첨부 파일 다운로드
     */
    @Override
    public ResponseEntity<Resource> downloadAttach(Long fileId) throws MalformedURLException {
        Optional<AttachFile> file = fileRepository.findById(fileId);
        String uploadFileName = file.get().getRealFileName();
        String storeFileName = file.get().getStoreFileName();

        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));

        String encodedUploadFileName = UriUtils.encode(uploadFileName,
                StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" +
                encodedUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}
