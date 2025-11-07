package kr.adapterz.jpa_practice.dto.comment;

import lombok.Getter;

@Getter
public class CommentRequest {

    private String content;

    public CommentRequest(String content) {

        this.content = content;

    }
}
