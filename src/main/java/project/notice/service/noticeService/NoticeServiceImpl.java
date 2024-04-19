package project.notice.service.noticeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.notice.domain.file.AttachFile;
import project.notice.domain.member.Member;
import project.notice.domain.member.Role;
import project.notice.domain.member.dto.MemberDto;
import project.notice.domain.notice.Notice;
import project.notice.domain.notice.dto.EditNotice;
import project.notice.domain.notice.dto.NoticeDto;
import project.notice.domain.notice.dto.RegisterNotice;
import project.notice.repository.FileRepository.FileRepository;
import project.notice.repository.MemberRepository;
import project.notice.repository.noticeRepository.NoticeRepository;
import project.notice.util.FileStore;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{

    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;
    private final FileRepository fileRepository;
    private final FileStore fileStore;

    /**
     * 공지사항 등록
     * 1. 작성자 이름을 통해 작성자 정보 조회
     * 2. 입력 값 + 작성자 정보로 공지사항 등록
     * 3. 첨부 파일 등록을 위해 공지사항 Id값 반환
     */
    @Override
    public Long register(String memberName, RegisterNotice registerNotice) {

        Member member = memberRepository.findByName(memberName);

        Notice notice = Notice.createNotice(
                member,
                registerNotice.getTitle(),
                registerNotice.getContent(),
                registerNotice.getStartDateTime(),
                registerNotice.getEndDateTime()
        );

        noticeRepository.save(notice);

        return notice.getId();
    }

    /**
     * 공지 사항 목록 조회
     */
    @Override
    public Page<NoticeDto> viewAll(Pageable pageable) {
        return noticeRepository.getNoticeList(pageable);
    }

    /**
     * 공지 사항 상세 보기
     */
    @Override
    public NoticeDto viewDetail(Long noticeId) {
        Optional<Notice> notice = noticeRepository.findById(noticeId);
        notice.get().increaseViewCount();

        String title = notice.get().getTitle();
        String content = notice.get().getContent();
        LocalDateTime startDateTime = notice.get().getStartDateTime();
        LocalDateTime endDateTime = notice.get().getEndDateTime();
        LocalDateTime createdDateTime = notice.get().getCreateDateTime();
        int viewCount = notice.get().getViewCount();
        MemberDto memberDto = new MemberDto(notice.get().getMember().getName(), Role.ADMIN);

        NoticeDto noticeDto = new NoticeDto(noticeId, title, content, startDateTime, endDateTime, createdDateTime, viewCount, memberDto);

        return noticeDto;
    }

    /**
     * 공지사항 수정
     */
    @Override
    public void edit(Long noticeId, EditNotice editNotice) {
        Optional<Notice> noticeOptional = noticeRepository.findById(noticeId);
        noticeOptional.ifPresent(notice -> {
            notice.edit(editNotice);
        });
    }

    /**
     * 공지사항 삭제
     * 1. 첨부 파일이 있을 경우 해당 파일 삭제
     * 2. DB에 저장된 첨부 파일 정보 삭제
     * 2. DB에 저장된 공지 사항 정보 삭제
     */
    @Override
    public void deleteNotice(Long noticeId) {
        Notice notice = noticeRepository.findWithFileById(noticeId);
        List<AttachFile> files = notice.getFiles();

        if(files != null) {
            for (AttachFile file : files) {
                fileStore.deleteFile(file.getStoreFileName());
            }
        }

        fileRepository.deleteNotice(noticeId);
        noticeRepository.delete(notice);
    }
}
