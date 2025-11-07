package kr.adapterz.jpa_practice.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateUserRequest {

    private String email;

    private String password;

    private String nickname;

    private String profileImageUrl;

    public CreateUserRequest(String email, String password, String nickname) {

        this.email = email;

        this.password = password;

        this.nickname = nickname;

    }

    public CreateUserRequest(String email, String password, String nickname, String profileImageUrl) {

        this.email = email;

        this.password = password;

        this.nickname = nickname;

        this.profileImageUrl = profileImageUrl;

    }

}
