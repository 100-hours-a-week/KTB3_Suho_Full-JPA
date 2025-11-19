package kr.adapterz.jpa_suho.dto.user;

import lombok.Getter;

@Getter
public class GetUserResponse {

    private String email;

    private String nickname;

    private String profileImageUrl;

    public GetUserResponse(String email, String nickname, String profileImageUrl) {

        this.email = email;

        this.nickname = nickname;

        this.profileImageUrl = profileImageUrl;

    }

}
