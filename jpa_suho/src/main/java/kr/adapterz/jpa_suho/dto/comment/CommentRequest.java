package kr.adapterz.jpa_suho.dto.comment;

import lombok.Getter;

@Getter
public class CommentRequest {

    private String content;

    public CommentRequest(String content) {

        this.content = content;

    }
}
