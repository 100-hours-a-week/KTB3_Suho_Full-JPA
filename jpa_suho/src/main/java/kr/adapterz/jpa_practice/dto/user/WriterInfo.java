package kr.adapterz.jpa_practice.dto.user;

import lombok.Getter;

@Getter
public class WriterInfo {

    private Long user_id;

    private String nickname;

    private String profileImageUrl;

    public WriterInfo(Long user_id, String nickname, String profileImageUrl) {

        this.user_id = user_id;

        this.nickname = nickname;

        this.profileImageUrl = profileImageUrl;

    }
}
