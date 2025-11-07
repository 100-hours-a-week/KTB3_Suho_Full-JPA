package kr.adapterz.jpa_practice.dto.post;

import lombok.Getter;

@Getter
public class UpdatePostRequest {

    private String title;

    private String content;

    private String postImageUrl;

    public UpdatePostRequest(String title, String content, String postImageUrl) {

        this.title = title;

        this.content = content;

        this.postImageUrl = postImageUrl;
    }

}
