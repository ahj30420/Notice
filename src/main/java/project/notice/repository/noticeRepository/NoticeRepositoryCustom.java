package project.notice.repository.noticeRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.notice.domain.notice.dto.NoticeDto;

public interface NoticeRepositoryCustom {
    Page<NoticeDto> getNoticeList(Pageable pageable);
}
