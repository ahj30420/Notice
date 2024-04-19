package project.notice.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import project.notice.domain.member.Role;
import project.notice.domain.member.dto.MemberDto;

@Slf4j
public class AdminCheckInterceptor implements HandlerInterceptor {

    /**
     * 관리자 인증
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("관리자 인증 인터셉터 실행 {}");

        String requestURI = request.getRequestURI();

        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("member") == null || !((MemberDto) session.getAttribute("member")).getRole().equals(Role.ADMIN)){
            log.info("미인증 관리자 요청");

            response.sendRedirect("/login?redirectURL=" + requestURI);
            return false;
        }
        return true;
    }
}
