package kr.adapterz.jpa_suho.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.adapterz.jpa_suho.annotation.swagger.AuthenticatedApi;
import kr.adapterz.jpa_suho.annotation.swagger.PublicApi;
import kr.adapterz.jpa_suho.dto.common.CommonResponse;
import kr.adapterz.jpa_suho.dto.user.LoginRequest;
import kr.adapterz.jpa_suho.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LoginController {

    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다.")
    @ApiResponse(responseCode = "200", description = "OK")
    @PublicApi
    public ResponseEntity<CommonResponse<Long>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest, HttpSession httpSession) {

        Long userId = userService.login(request.getEmail(), request.getPassword());

        // 세션에 userId 저장
        HttpSession session = httpRequest.getSession(true);
        session.setAttribute("userId", userId);

        // Spring Security 인증 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userId,
                null,
                Collections.emptyList()
        );

        // SecurityContext에 객체 저장
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // session에 명시적으로 저장
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        CommonResponse<Long> response = new CommonResponse<>(
                "LOGIN_SUCCESS",
                "로그인에 성공했습니다.",
                userId
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "로그아웃합니다.")
    @ApiResponse(responseCode = "200", description = "OK")
    @AuthenticatedApi
    public ResponseEntity<CommonResponse<Void>> logout(HttpSession session) {

        // 세션 무효화
        session.invalidate();

        CommonResponse<Void> response = new CommonResponse<>(
                "LOGOUT_SUCCESS",
                "로그아웃에 성공했습니다.",
                null
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}