package kr.adapterz.jpa_practice.dto.post;

import lombok.Getter;

@Getter
public class DetailStatistic {

    private int viewCount;

    private int commentCount;

    private int likeCount;

    private boolean isLiked;

    public DetailStatistic(int viewCount, int commentCount, int likeCount, boolean isLiked) {

        this.viewCount = viewCount;

        this.commentCount = commentCount;

        this.likeCount = likeCount;

        this.isLiked = isLiked;

    }
}
