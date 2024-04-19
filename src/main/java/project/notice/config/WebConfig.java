package project.notice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import project.notice.interceptor.AdminCheckInterceptor;
import project.notice.interceptor.LoginCheckInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminCheckInterceptor())
                .order(1)
                .addPathPatterns("/api/notice/new", "/api/notice/info/{noticeId:\\d+}",
                        "/api/notice/{noticeId:\\d+}", "/api/attachFile/{noticeId:\\d+}/{fileId:\\d+}"
                );
    }

}
