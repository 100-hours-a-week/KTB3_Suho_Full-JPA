package kr.adapterz.jpa_practice.dto.post;

import kr.adapterz.jpa_practice.dto.comment.CommentItem;
import kr.adapterz.jpa_practice.dto.user.WriterInfo;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class DetailPostResponse {
    private Long postId;
    private String title;
    private String content;
    private String postImageUrl;
    private LocalDateTime createdAt;
    private WriterInfo writerInfo;
    private DetailStatistic detailStatistics;
    private List<CommentItem> comments;

    @Builder
    public DetailPostResponse(Long postId, String title, String content, String postImageUrl,
                              LocalDateTime createdAt, WriterInfo writerInfo,
                              DetailStatistic detailStatistics,
                              List<CommentItem> comments
    ) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.postImageUrl = postImageUrl;
        this.createdAt = createdAt;
        this.writerInfo = writerInfo;
        this.detailStatistics = detailStatistics;
        this.comments = comments;
    }

}

