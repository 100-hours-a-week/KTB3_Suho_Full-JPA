package kr.adapterz.jpa_practice.dto.like;

import lombok.Getter;

@Getter
public class CreateLikeResponse {

    private Long postId;

    private int likeCount;

    public CreateLikeResponse(Long postId, int likeCount){

        this.postId = postId;

        this.likeCount = likeCount;

    }

}
