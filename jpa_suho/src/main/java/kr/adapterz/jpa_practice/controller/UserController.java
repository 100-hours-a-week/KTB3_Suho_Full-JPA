package kr.adapterz.jpa_practice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import kr.adapterz.jpa_practice.dto.common.CommonResponse;
import kr.adapterz.jpa_practice.dto.user.*;
import kr.adapterz.jpa_practice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "회원가입", description = "새로운 사용자 정보를 등록합니다.")
    @ApiResponse(responseCode = "201", description = "Created")
    public ResponseEntity<CommonResponse<CreateUserResponse>> create(@RequestBody CreateUserRequest request) {

        Long id = userService.create(request.getEmail(), request.getPassword(), request.getNickname(), request.getProfileImageUrl());

        CommonResponse<CreateUserResponse> response = new CommonResponse<>(
                "201",
                "Created",
                new CreateUserResponse(id)
        );

        return ResponseEntity.status(201).body(response);

    }

    @GetMapping("/{id}")
    @Operation(summary = "사용자 정보 조회", description = "ID를 통해 특정 사용자 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<CommonResponse<GetUserResponse>> getUserInfo(
            @PathVariable Long id,
            @SessionAttribute("userId") Long sessionUserId) {

        if (!id.equals(sessionUserId)) {
            throw new IllegalArgumentException("자신의 정보만 조회할 수 있습니다.");
        }

        UserInfo userInfo = userService.findById(id);

        CommonResponse<GetUserResponse> response = new CommonResponse<>(
                "200",
                "OK",
                new GetUserResponse(userInfo.getEmail(), userInfo.getNickname(), userInfo.getProfileImageUrl())
                );

        return ResponseEntity.status(200).body(response);

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "회원탈퇴", description = "ID를 통해 특정 사용자의 정보를 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "No Content")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @SessionAttribute("userId") Long sessionUserId) {

        if (!id.equals(sessionUserId)) {
            throw new IllegalArgumentException("자신의 계정만 탈퇴할 수 있습니다.");
        }

        userService.deleteById(id);

        return ResponseEntity.status(204).build();

    }

    @PatchMapping("/{id}/nickname")
    @Operation(summary = "닉네임 변경", description = "ID를 통해 특정 사용자의 닉네임을 변경합니다.")
    @ApiResponse(responseCode = "204", description = "No Content")
    public ResponseEntity<Void> changeNickname(
            @PathVariable Long id,
            @Valid @RequestBody ChangeNicknameRequest request,
            @SessionAttribute("userId") Long sessionUserId) {

        if (!id.equals(sessionUserId)) {
            throw new IllegalArgumentException("자신의 닉네임만 변경할 수 있습니다.");
        }

        userService.changeNickname(id, request.getNickname());

        return ResponseEntity.status(204).build();

    }

    @PatchMapping("/{id}/password")
    @Operation(summary = "비밀번호 변경", description = "ID를 통해 특정 사용자의 비밀번호를 변경합니다.")
    @ApiResponse(responseCode = "204", description = "No Content")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordRequest request,
            @SessionAttribute("userId") Long sessionUserId) {

        if (!id.equals(sessionUserId)) {
            throw new IllegalArgumentException("자신의 비밀번호만 변경할 수 있습니다.");
        }

        userService.changePassword(id, request.getPassword());

        return ResponseEntity.status(204).build();

    }

}
