package kr.adapterz.jpa_practice.dto.post;

import kr.adapterz.jpa_practice.dto.user.WriterInfo;
import kr.adapterz.jpa_practice.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostInfo {

    private Long postId;

    private WriterInfo writerInfo;

    private String title;

    private String content;

    private String postImageUrl;

    private int viewCount;

    private int likeCount;

    private int commentCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public PostInfo(Post post, WriterInfo writerInfo) {

        this.postId = post.getId();

        this.writerInfo = writerInfo;

        this.title = post.getTitle();

        this.content = post.getContent();

        this.postImageUrl = post.getPostImageUrl();

        this.viewCount = post.getViewCount();

        this.likeCount = post.getLikeCount();

        this.commentCount = post.getCommentCount();

        this.createdAt = post.getCreatedAt();

        this.updatedAt = post.getUpdatedAt();

    }

}
