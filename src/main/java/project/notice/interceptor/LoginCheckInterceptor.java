package project.notice.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    /**
     * 로그인 인증
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

      log.info("로그인 인증 인터셉터 실행 {}");

      String requestURI = request.getRequestURI();

      HttpSession session = request.getSession(false);

      if(session == null || session.getAttribute("member") == null){
          log.info("미인증 사용자 요청");

          response.sendRedirect("/login?redirectURL=" + requestURI);
          return false;
      }
      return true;
    }
}
