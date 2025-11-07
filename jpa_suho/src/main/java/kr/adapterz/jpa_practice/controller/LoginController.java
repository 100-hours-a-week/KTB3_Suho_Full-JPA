package kr.adapterz.jpa_practice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.adapterz.jpa_practice.dto.common.CommonResponse;
import kr.adapterz.jpa_practice.dto.user.LoginRequest;
import kr.adapterz.jpa_practice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LoginController {

    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다.")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<CommonResponse<String>> login(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {

        Long userId = userService.login(request.getEmail(), request.getPassword());

        // 세션에 userId 저장
        HttpSession session = httpRequest.getSession(true);
        session.setAttribute("userId", userId);

        // 세션 ID 반환 (디버깅용)
        String sessionId = session.getId();

        CommonResponse<String> response = new CommonResponse<>(
                "200",
                "로그인에 성공했습니다. 세션ID: " + sessionId,
                sessionId
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "로그아웃합니다.")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<CommonResponse<Void>> logout(HttpSession session) {

        session.invalidate();

        CommonResponse<Void> response = new CommonResponse<>(
                "200",
                "로그아웃에 성공했습니다.",
                null
        );

        return ResponseEntity.ok(response);
    }
}