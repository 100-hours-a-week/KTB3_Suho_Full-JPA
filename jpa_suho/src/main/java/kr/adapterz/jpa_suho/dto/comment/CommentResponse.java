package kr.adapterz.jpa_suho.dto.comment;

import lombok.Getter;

@Getter
public class CommentResponse {

    private Long postId;

    private Long commentId;

    public CommentResponse(Long postId, Long commentId) {

        this.postId = postId;

        this.commentId = commentId;

    }

}
