package project.notice.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import project.notice.domain.file.dto.UploadFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    /**
     * 이미지 파일에서 실제 이미지 이름과 DB에 저장할 이미지 이름 추출
     * 저장소에 같은 이미지 이름이 있다면 덮어 씌어 질 수 있기 때문에 실제 이름과 저장할 때 이름을 분리해야 된다.
     */
    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {

        if(multipartFile.isEmpty()){
            return null;
        }

        String realFileName = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(realFileName);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        return new UploadFile(realFileName, storeFileName);
    }

    /**
     * 다중 첨부파일 변환 작업
     */
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for(MultipartFile multipartFile : multipartFiles){
            if(!multipartFile.isEmpty()){
                storeFileResult.add(storeFile(multipartFile));
            }
        }
        return storeFileResult;
    }

    /**
     * 파일 삭제
     */
    public void deleteFile(String storeFileName){
        File file = new File((getFullPath(storeFileName)));
        file.delete();
    }

    /**
     * 파일을 저장할 경로 설정
     */
    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    //--------------------------------------Private Method-----------------------------------------------------

    /**
     * 원본 파일의 확장자로 저장할 파일 이름 생성
     * 저장될 파일 이름은 중복되지 않게 UUID로 생성하였습니다.
     */
    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    /**
     * 원본 파일의 확장자명 추출
     */
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos+1);
    }

}
