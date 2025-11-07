package kr.adapterz.jpa_practice.dto.post;

import lombok.Getter;

import java.util.List;

@Getter
public class GetPostsResponse {

    private List<PostInfo> posts;

    private Pagination pagination;

    public GetPostsResponse(List<PostInfo> posts, Pagination pagination) {
        this.posts = posts;
        this.pagination = pagination;
    }

}
