package kr.adapterz.jpa_practice.dto.post;

import lombok.Getter;

@Getter
public class CreatePostRequest {

    private String title;

    private String content;

    private String imageUrl;

    public CreatePostRequest(String title, String content, String imageUrl) {

        this.title = title;

        this.content = content;

        this.imageUrl = imageUrl;

    }

}
