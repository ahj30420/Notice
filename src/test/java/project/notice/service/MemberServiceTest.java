package project.notice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.notice.domain.member.Role;
import project.notice.domain.member.dto.JoinDto;
import project.notice.domain.member.dto.LoginDto;
import project.notice.domain.member.dto.MemberDto;
import project.notice.exception.DuplicatedNameException;
import project.notice.exception.LoginException;
import project.notice.service.memberService.MemberService;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @BeforeEach
    void init(){
        JoinDto joinDto = new JoinDto("testName", "testPassword@", Role.USER);
        memberService.register(joinDto);
    }

    @Test
    @DisplayName("회원가입 성공")
    void signupSuccess(){
        JoinDto joinDto = new JoinDto("testName2", "testPassword@", Role.USER);

        memberService.register(joinDto);
        MemberDto member = memberService.findByName(joinDto.getName());

        assertThat(member.getName()).isEqualTo("testName2");
    }

    @Test
    @DisplayName("회원가입 실패(이름 중복 오류)")
    void signupFail(){
        JoinDto joinDto = new JoinDto("testName", "testPassword@", Role.USER);

        assertThrows(DuplicatedNameException.class, () ->
                memberService.register(joinDto)
                );
    }

    @Test
    @DisplayName("로그인 성공")
    void loginSuccess(){
        LoginDto loginDto = new LoginDto("testName", "testPassword@");

        MemberDto login = memberService.login(loginDto);
        String name = login.getName();

        assertThat(name).isEqualTo("testName");
    }

    @Test
    @DisplayName("로그인 실패(이름, 비밀번호 불일치)")
    void loginFail(){
        LoginDto loginDto = new LoginDto("testFail", "testFail@");

        assertThrows(LoginException.class, () ->
            memberService.login(loginDto)
        );
    }


}
