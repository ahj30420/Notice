package project.notice.controller.noticeController;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.notice.domain.file.dto.FileDto;
import project.notice.domain.file.dto.UploadFile;
import project.notice.domain.member.Role;
import project.notice.domain.member.dto.MemberDto;
import project.notice.domain.notice.dto.EditNotice;
import project.notice.domain.notice.dto.NoticeDto;
import project.notice.domain.notice.dto.RegisterNotice;
import project.notice.exception.DateSettingException;
import project.notice.service.attachFileService.AttachFileService;
import project.notice.service.noticeService.NoticeService;
import project.notice.util.FileStore;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {

    private final NoticeService noticeService;
    private final AttachFileService attachFileService;
    private final FileStore fileStore;

    /**
     * 공지사항 등록
     * 1. 입력값 검증
     * 2. session 데이터를 통해 작성자 정보 불러오기
     * 3. 입력 값 + 작성자 정보로 공지사항 등록
     * 4. 여러 개의 첨부 파일 등록
     *
     * - DateSettingException: 공지사항 시작 일시, 마감 일시 변경 할 때 마감 일시가 시작 일시 보다 빠르면 발생하는 Exception
     */
    @PostMapping("/new")
    public String registerNotice(@Valid @ModelAttribute RegisterNotice registerNotice,
                                 BindingResult bindingResult,
                                 @RequestParam(required = false) List<MultipartFile> multipartFiles,
                                 HttpSession session,
                                 Model model) throws IOException {

        log.info("registerNotice={}", registerNotice);

        if(bindingResult.hasErrors()){
            return "registerNotice";
        }

        try{
            MemberDto member = (MemberDto) session.getAttribute("member");
            Long noticeId = noticeService.register(member.getName(), registerNotice);

            List<UploadFile> uploadFiles = fileStore.storeFiles(multipartFiles);
            attachFileService.register(noticeId, uploadFiles);

            return "redirect:/api/notice/all";
        } catch (DateSettingException e){
            model.addAttribute("errorMessage", e.getErrorMessage());
            return "registerNotice";
        }
    }

    /**
     * 공지사항 목록 조회
     * - 페이지 당 5개씩 조회
     * - 유저의 공지사항 등록 권한 판별 변수 permissions(관리자 = true / 일반 사용자 = false)
     */
    @GetMapping("/all")
    public String viewAllNotice(@RequestParam(name="page", defaultValue = "0") String page,
                                HttpSession session,
                                Model model){

        Pageable pageable = PageRequest.of(Integer.parseInt(page), 5);
        Page<NoticeDto> noticeList = noticeService.viewAll(pageable);

        boolean permissions = false;
        MemberDto member = (MemberDto) session.getAttribute("member");

        if(member != null && member.getRole() == Role.ADMIN){
            permissions = true;
        }

        model.addAttribute("totalCount", noticeList.getTotalPages());
        model.addAttribute("noticeList", noticeList.getContent());
        model.addAttribute("permissions", permissions);

        return "notices";
    }

    /**
     * 공지사항 상세 보기
     * 1. 공지사항 정보 조회
     * 2. 해당 공지사항의 첨부 파일 조회
     * 3. 공지사항 수정, 삭제 권한 판별 변수 permissions(공지사항 등록자 = true / 그 외 유저 = false)
     */
    @GetMapping("/detail/{noticeId}")
    public String viewDetailNotice(@PathVariable Long noticeId, HttpSession session, Model model){

        NoticeDto noticeDto = noticeService.viewDetail(noticeId);
        List<FileDto> files = attachFileService.findByNoticeId(noticeId);
        boolean permissions = false;

        MemberDto member = (MemberDto) session.getAttribute("member");
        if(member != null && member.getName().equals(noticeDto.getMemberDto().getName())){
            permissions = true;
        }

        model.addAttribute("notice", noticeDto);
        model.addAttribute("files", files);
        model.addAttribute("permissions", permissions);

        log.info("permissions={}", permissions);

        return "DetailNotice";
    }

    /**
     * 공지사항 모든 정보 보기 (공지사항 수정 페이지)
     */
    @GetMapping("/info/{noticeId}")
    public String viewNoticeInfo(@PathVariable Long noticeId, Model model){
        NoticeDto noticeDto = noticeService.viewDetail(noticeId);
        List<FileDto> files = attachFileService.findByNoticeId(noticeId);

        model.addAttribute("notice", noticeDto);
        model.addAttribute("files", files);

        return "Edit";
    }

    /**
     * 공지사항 수정
     * - EditNotice: 수정 된 공지사항 정보 객체
     * - multipartFiles: 새롭게 추간된 첨부 파일 목록
     * - DateSettingException: 공지사항 시작 일시, 마감 일시 변경 할 때 마감 일시가 시작 일시 보다 빠르면 발생하는 Exception
     */
    @PatchMapping("/{noticeId}")
    public String Edit(@PathVariable Long noticeId,
                       @ModelAttribute EditNotice editNotice,
                       @RequestParam(required = false) List<MultipartFile> multipartFiles,
                       Model model) throws IOException {

        try {
            noticeService.edit(noticeId, editNotice);

            List<UploadFile> uploadFiles = fileStore.storeFiles(multipartFiles);
            attachFileService.register(noticeId, uploadFiles);
            return "redirect:/api/notice/detail/"+noticeId;
        } catch (DateSettingException e){
            model.addAttribute("errorMessage", e.getErrorMessage());
            return "registerNotice";

        }
    }

    /**
     * 공지사항 삭제
     */
    @DeleteMapping("/{noticeId}")
    public String deleteNotice(@PathVariable Long noticeId){
        noticeService.deleteNotice(noticeId);
        return "redirect:/api/notice/all";
    }

}
