package project.notice.controller.noticeController;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.notice.domain.notice.dto.RegisterNotice;

@Controller
@RequiredArgsConstructor
public class NoticeViewController {

    @GetMapping("/registerNotice")
    public String registerNoticePage(Model model) {
        model.addAttribute("registerNotice", new RegisterNotice());
        return "registerNotice";
    }

}
