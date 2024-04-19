package project.notice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import project.notice.config.SecurityConfig;
import project.notice.controller.memberController.MemberController;
import project.notice.domain.member.Role;
import project.notice.domain.member.dto.JoinDto;
import project.notice.domain.member.dto.LoginDto;
import project.notice.domain.member.dto.MemberDto;
import project.notice.exception.DuplicatedNameException;
import project.notice.exception.LoginException;
import project.notice.service.memberService.MemberService;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MemberController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class, // 추가
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MemberService memberService;

    @Test
    @DisplayName("회원가입 성공")
    void signUpSuccess() throws Exception {

        JoinDto joinDto = new JoinDto("test1","test1234!", Role.USER);
        String json = objectMapper.writeValueAsString(joinDto);

        mockMvc.perform(
                        post("/api/member/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(memberService).register(joinDto);
    }

    @Test
    @DisplayName("회원가입 실패(입력값 검증 오류)")
    void signUpFailInvalidJoinDto() throws Exception {

        JoinDto joinDto = new JoinDto("","zxcv", Role.USER);
        String json = objectMapper.writeValueAsString(joinDto);

        mockMvc.perform(
                post("/api/member/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"));

        verifyNoInteractions(memberService);
    }

    @Test
    @DisplayName("회원가입 실패(이름 중복 오류)")
    void signUpFailDuplicatedName() throws Exception {

        JoinDto joinDto = new JoinDto("test", "test1234@", Role.USER);
        String json = objectMapper.writeValueAsString(joinDto);

        doThrow(DuplicatedNameException.class).when(memberService).register(any(JoinDto.class));

        mockMvc.perform(
                        post("/api/member/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"));

        verify(memberService).register(joinDto);
    }

    @Test
    @DisplayName("로그인 성공")
    void loginSuccess() throws Exception {

        LoginDto loginDto = new LoginDto("test", "test1234@");
        String json = objectMapper.writeValueAsString(loginDto);

        given(memberService.login(loginDto)).willReturn(
                new MemberDto("test", Role.USER));

        mockMvc.perform(
                post("/api/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notices"));

        verify(memberService).login(loginDto);
    }

    @Test
    @DisplayName("로그인 실패(입력값 검증 오류)")
    void loginFailInvalidLoginDto() throws Exception {

        LoginDto loginDto = new LoginDto("", "");
        String json = objectMapper.writeValueAsString(loginDto);

        mockMvc.perform(
                        post("/api/member/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));

        verifyNoInteractions(memberService);
    }

    @Test
    @DisplayName("로그인 실패(이름,비밀번호 불일치)")
    void loginFailException() throws Exception {

        LoginDto loginDto = new LoginDto("test1", "testtest@");
        String json = objectMapper.writeValueAsString(loginDto);

        doThrow(LoginException.class)
                .when(memberService).login(loginDto);

        mockMvc.perform(
                post("/api/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));

        verify(memberService).login(loginDto);
    }

}
