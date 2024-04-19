package project.notice.service.noticeService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import project.notice.domain.notice.dto.EditNotice;
import project.notice.domain.notice.dto.NoticeDto;
import project.notice.domain.notice.dto.RegisterNotice;

import java.util.List;

public interface NoticeService {
    Long register(String memberName, RegisterNotice registerNotice);

    Page<NoticeDto> viewAll(Pageable pageable);

    NoticeDto viewDetail(Long noticeId);

    void edit(Long noticeId, EditNotice editNotice);

    void deleteNotice(Long noticeId);
}
