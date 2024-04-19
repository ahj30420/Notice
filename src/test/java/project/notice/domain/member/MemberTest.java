package project.notice.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MemberTest {

    private Member member;

    @BeforeEach
    void init(){
        member = Member.createMember(
                "name",
                "password123@",
                Role.USER
        );
    }

    @Test
    @DisplayName("관리자 권한으로 변경 테스트")
    void changeRoleTest(){
        member.changeRole();
        Assertions.assertThat(member.getRole()).isEqualByComparingTo(Role.ADMIN);
    }

}
