package project.notice.controller.attachFileController;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.notice.service.attachFileService.AttachFileService;

import java.net.MalformedURLException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/attachFile")
public class AttachFileController {

    private final AttachFileService attachFileService;

    /**
     * 첨부 파일 삭제
     */
    @DeleteMapping("/{noticeId}/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable Long noticeId, @PathVariable Long fileId, @RequestParam("storeFileName") String storeFileName){
        attachFileService.deleteFile(fileId,storeFileName);
        return ResponseEntity.ok().build();
    }

    /**
     * 파일 다운로드
     */
    @GetMapping("/downloadFile/{itemId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId)
            throws MalformedURLException {
        return attachFileService.downloadAttach(itemId);
    }
}
