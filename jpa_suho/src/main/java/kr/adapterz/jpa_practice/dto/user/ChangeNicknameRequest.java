package kr.adapterz.jpa_practice.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ChangeNicknameRequest {
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 2, max = 20, message = "닉네임의 길이는 최소 2자, 최대 20자까지 입니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]+$",
            message = "닉네임은 한글, 영문, 숫자만 사용 가능합니다.")
    @Schema(description = "닉네임", example = "suho")
    private String nickname;

    public ChangeNicknameRequest(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

}
