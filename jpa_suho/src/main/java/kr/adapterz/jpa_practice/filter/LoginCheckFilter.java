package kr.adapterz.jpa_practice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.adapterz.jpa_practice.dto.common.CommonResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class LoginCheckFilter implements Filter {

    // 로그인 체크 제외 URL 목록
    private static final List<String> EXCLUDE_URLS = Arrays.asList(
            "/api/v1/users",           // 회원가입
            "/api/v1/login",           // 로그인
            "/api/v1/posts",           // 게시물 (임시 - 개발용)
            "/swagger-ui",             // Swagger
            "/v3/api-docs"             // Swagger API 문서
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();

        // 제외 URL 체크
        if (isExcludedUrl(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        // 세션 체크
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            sendUnauthorizedResponse(httpResponse);
            return;
        }

        // 인증 성공 - 다음 필터로 진행
        chain.doFilter(request, response);
    }

    private boolean isExcludedUrl(String requestURI) {
        return EXCLUDE_URLS.stream().anyMatch(requestURI::startsWith);
    }

    private void sendUnauthorizedResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        CommonResponse<Void> errorResponse = new CommonResponse<>(
                "401",
                "로그인이 필요합니다.",
                null
        );

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }
}