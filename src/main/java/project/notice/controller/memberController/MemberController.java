package project.notice.controller.memberController;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.notice.domain.member.dto.JoinDto;
import project.notice.domain.member.dto.LoginDto;
import project.notice.domain.member.dto.MemberDto;
import project.notice.exception.DuplicatedNameException;
import project.notice.exception.LoginException;
import project.notice.service.memberService.MemberService;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입
     * 1. 입력 값 검증
     * 2. 회원가입 진행
     *
     * - DuplicatedNameException: 입력 한 이름이 기존 회원의 이름과 중복될 경우
     */
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute JoinDto joinDto, BindingResult bindingResult, Model model){

        log.info("joinDto={}",joinDto);

        //회원가입 시 입력값 검증
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        //이름이 중복 값일 경우 다시 입력할 수 있도록 회원가입 페이지로 이동
        try {
            memberService.register(joinDto);
            return "redirect:/login";
        } catch (DuplicatedNameException e) {
            model.addAttribute("errorMessage", e.getErrorMessage());
            return "signup";
        }
    }


    /**
     * 로그인
     * 1. 입력 값 검증
     * 2. 로그인 진행
     * 3. 사용자 정보 sessioo에 저장
     *
     * - LoginException: 이름과 비밀번호가 일치 하지 않을 경우
     */
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginDto loginDto, BindingResult bindingResult, Model model, HttpSession session){

        //로그인 시 입력값 검증
        if(bindingResult.hasErrors()){
            return "login";
        }

        //이름과 비밀번호가 일치하지 않을 경우 다시 입력할 수 있도록 로그인 페이지로 이동
        try {
            MemberDto memberDto = memberService.login(loginDto);
            session.setAttribute("member", memberDto);
            return "redirect:/api/notice/all";
        } catch (LoginException e){
            model.addAttribute("errorMessage", e.getErrorMessage());
            return "login";
        }
    }

}
