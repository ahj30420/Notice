package project.notice.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import project.notice.config.SecurityConfig;
import project.notice.controller.noticeController.NoticeController;
import project.notice.domain.member.Role;
import project.notice.domain.member.dto.MemberDto;
import project.notice.domain.notice.dto.RegisterNotice;
import project.notice.service.attachFileService.AttachFileService;
import project.notice.service.noticeService.NoticeService;
import project.notice.util.FileStore;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = NoticeController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class, // 추가
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
@AutoConfigureMockMvc
public class NoticeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoticeService noticeService;

    @MockBean
    private AttachFileService attachFileService;

    @MockBean
    private FileStore fileStore;

    @Test
    @DisplayName("공지사항 + 2개 첨부 파일 등록 성공")
    void RegisterNoticeFail() throws Exception {

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("member", new MemberDto("test", Role.ADMIN));

        MockMultipartFile file1 = new MockMultipartFile("multipartFiles", "filename1.txt", "text/plain", "content1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("multipartFiles", "filename2.txt", "text/plain", "content2".getBytes());

        RegisterNotice registerNotice = new RegisterNotice(
                "testTitle",
                "testContent",
                LocalDateTime.of(2024, 04, 17, 12, 00, 00),
                LocalDateTime.of(2024, 05, 17, 12, 00, 00)
        );

        mockMvc.perform(
                MockMvcRequestBuilders.multipart("/api/notice/new")
                        .file(file1)
                        .file(file2)
                        .session(session)
                        .flashAttr("registerNotice", registerNotice))
                .andExpect(status().isOk())
                .andExpect(view().name("notices"));

        verify(noticeService).register(anyString(), any(RegisterNotice.class));
        verify(attachFileService).register(anyLong(), anyList());
    }

    @Test
    @DisplayName("공지사항 + 첨부 파일이 없을 경우 성공")
    void RegisterNoticeTest() throws Exception {

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("member", new MemberDto("test", Role.ADMIN));

        RegisterNotice registerNotice = new RegisterNotice(
                "testTitle",
                "testContent",
                LocalDateTime.of(2024, 04, 17, 12, 00, 00),
                LocalDateTime.of(2024, 05, 17, 12, 00, 00)
        );

        mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/api/notice/new")
                                .session(session)
                                .flashAttr("registerNotice", registerNotice))
                .andExpect(status().isOk())
                .andExpect(view().name("notices"));

        verify(noticeService).register(anyString(), any(RegisterNotice.class));
        verify(attachFileService).register(anyLong(), anyList());
    }

    @Test
    @DisplayName("공지사항 등록 실패(입력값 검증 실패)")
    void RegisterNoticeWithFileTest() throws Exception {

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("member", new MemberDto("test", Role.ADMIN));

        MockMultipartFile file1 = new MockMultipartFile("multipartFiles", "filename1.txt", "text/plain", "content1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("multipartFiles", "filename2.txt", "text/plain", "content2".getBytes());

        RegisterNotice registerNotice = new RegisterNotice(
                null,
                null,
                LocalDateTime.of(2024, 04, 17, 12, 00, 00),
                LocalDateTime.of(2024, 05, 17, 12, 00, 00)
        );

        mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/api/notice/new")
                                .file(file1)
                                .file(file2)
                                .session(session)
                                .flashAttr("registerNotice", registerNotice))
                .andExpect(status().isOk())
                .andExpect(view().name("registerNotice"));

        verifyNoInteractions(noticeService);
        verifyNoInteractions(attachFileService);
    }

}
