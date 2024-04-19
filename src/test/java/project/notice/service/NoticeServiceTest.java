package project.notice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import project.notice.domain.member.Member;
import project.notice.domain.member.Role;
import project.notice.domain.notice.Notice;
import project.notice.domain.notice.dto.EditNotice;
import project.notice.domain.notice.dto.NoticeDto;
import project.notice.domain.notice.dto.RegisterNotice;
import project.notice.repository.MemberRepository;
import project.notice.repository.noticeRepository.NoticeRepository;
import project.notice.service.noticeService.NoticeService;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest
public class NoticeServiceTest {

    @Autowired
    NoticeService noticeService;

    @Autowired
    NoticeRepository noticeRepository;

    @Autowired
    MemberRepository memberRepository;

    Member member;

    Notice notice;

    @BeforeEach
    void init(){
        member = Member.createMember("test", "testpw@", Role.ADMIN);
        memberRepository.save(member);

        notice = Notice.createNotice(
                member,
                "title",
                "content",
                LocalDateTime.of(2024,04,16,12,00,00),
                LocalDateTime.of(2024,04,23,12,00,00)
        );
        noticeRepository.save(notice);
    }

    @Test
    @DisplayName("공지사항 등록 성공")
    void registerSuccess(){
        RegisterNotice registerNotice = new RegisterNotice(
                "title",
                "content",
                LocalDateTime.of(2024,04,17,12,00,00),
                LocalDateTime.of(2024,04,18,12,00,00)
        );

        Long noticeId = noticeService.register(member.getName(), registerNotice);

        assertNotNull(noticeId);
    }

    @Test
    @DisplayName("공지사항 목록 조회")
    void viewAll(){
        PageRequest pageRequest = PageRequest.of(0,5);
        Page<NoticeDto> notices = noticeService.viewAll(pageRequest);

        assertThat(notices.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("공지 사항 상세 보기 테스트")
    void viewDetail(){
        NoticeDto noticeDto = noticeService.viewDetail(notice.getId());

        assertNotNull(noticeDto);
        assertEquals("title", noticeDto.getTitle());
        assertEquals("content", noticeDto.getContent());
        assertEquals(1, noticeDto.getViewCount());
    }

    @Test
    @DisplayName("공지사항 수정")
    void editNotice(){
        EditNotice editNotice = new EditNotice(
                "test_title",
                "test_content",
                LocalDateTime.of(2024,05,11,00,00,00),
                LocalDateTime.of(2024,06,11,00,00,00)
        );

        noticeService.edit(notice.getId(), editNotice);

        assertEquals("test_title", notice.getTitle());
        assertEquals("test_content", notice.getContent());
        assertEquals(LocalDateTime.of(2024,05,11,00,00,00), notice.getStartDateTime());
        assertEquals(LocalDateTime.of(2024,06,11,00,00,00), notice.getEndDateTime());
    }

}
