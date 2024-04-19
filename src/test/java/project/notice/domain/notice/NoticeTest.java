package project.notice.domain.notice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.notice.domain.member.Member;
import project.notice.domain.member.Role;
import project.notice.exception.DateSettingException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class NoticeTest {

    private Notice notice;
    private Member member;

    @BeforeEach
    void init(){
        member = Member.createMember("testName", "testPw", Role.ADMIN);

        notice = Notice.createNotice(
                member,
                "title",
                "content",
                LocalDateTime.of(2024, 04, 17, 12, 0, 0),
                LocalDateTime.of(2024, 04, 23, 11, 0, 0)
        );
    }

    @Test
    @DisplayName("조회수 증가 테스트")
    void increaseViewCountTest(){
        notice.increaseViewCount();
        assertThat(notice.getViewCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("시작일과 마감일 설정 테스트")
    void DateSettingExceptionTest(){
        assertThrows(DateSettingException.class, () -> Notice.createNotice(
                member,
                "title",
                "content",
                LocalDateTime.of(2024, 04, 17, 12, 0, 0),
                LocalDateTime.of(2024, 04, 16, 11, 0, 0)
        ));
    }

}
