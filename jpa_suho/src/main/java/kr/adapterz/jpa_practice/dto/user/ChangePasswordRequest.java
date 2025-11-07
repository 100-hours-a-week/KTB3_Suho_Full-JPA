package kr.adapterz.jpa_practice.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangePasswordRequest {
    @NotBlank(message = "비밀번호은 필수 입력입니다.")
    @Size(min = 8, max = 50, message = "비밀번호의 길이는 최소 8자, 최대 50자까지 입니다.")
    private String password;

    public ChangePasswordRequest(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

}
