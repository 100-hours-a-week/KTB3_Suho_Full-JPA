package kr.adapterz.jpa_practice.dto.post;

import lombok.Getter;

@Getter
public class CreatePostResponse {

    private Long postId;

    public CreatePostResponse(Long postId) {

        this.postId = postId;

    }

}
