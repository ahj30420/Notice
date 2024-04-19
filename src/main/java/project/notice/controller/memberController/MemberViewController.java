package project.notice.controller.memberController;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.notice.domain.member.dto.JoinDto;
import project.notice.domain.member.dto.LoginDto;

@Controller
@RequiredArgsConstructor
public class MemberViewController {

    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("joinDto", new JoinDto());
        return "signup";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "login";
    }

}
