package kr.adapterz.jpa_practice.service;

import kr.adapterz.jpa_practice.dto.comment.CommentItem;
import kr.adapterz.jpa_practice.dto.post.*;
import kr.adapterz.jpa_practice.dto.user.WriterInfo;
import kr.adapterz.jpa_practice.entity.Comment;
import kr.adapterz.jpa_practice.entity.Post;
import kr.adapterz.jpa_practice.entity.User;
import kr.adapterz.jpa_practice.exception.ForbiddenException;
import kr.adapterz.jpa_practice.repository.CommentRepository;
import kr.adapterz.jpa_practice.repository.LikeRepository;
import kr.adapterz.jpa_practice.repository.PostRepository;
import kr.adapterz.jpa_practice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    @Transactional
    public Long create(Long userId, String title, String content, String postImageUrl) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 ID입니다."));

        if (user.isDeleted()) {
            throw new IllegalArgumentException("탈퇴한 사용자입니다.");
        }

        Post post = new Post(user, title, content, postImageUrl);

        return postRepository.save(post).getId();

    }

    @Transactional
    public GetPostsResponse findWithSize(int size, Long cursor) {

        List<PostInfo> postInfos = new ArrayList<>();

        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));
        List<Post> posts = postRepository.findPostsWithSize(cursor,pageable);

        // 빈 리스트 처리
        if (posts.isEmpty()) {
            return new GetPostsResponse(new ArrayList<>(), new Pagination(size, null));
        }

        // Post -> PostInfo 변환
        for (Post post : posts) {
            User user = post.getUser();
            WriterInfo writerInfo = new WriterInfo(user.getId(), user.getNickname(), user.getProfileImageUrl());
            postInfos.add(new PostInfo(post, writerInfo));
        }

        Pagination pagination = new Pagination(size,posts.getLast().getId());

        return new GetPostsResponse(postInfos, pagination);
    }

    @Transactional
    public Long update(Long postId, Long userId, String title, String content, String postImageUrl ) {

        // 포스트 존재하는지 체크
        Post post = findPostById(postId);

        // 포스트 작성자가 맞는지 체크
        if (hasRightToEdit(post, userId)) {

            post.changePost(title, content, postImageUrl);

        }

        return postId;

    }

    @Transactional
    public void delete(Long postId, Long userId) {

        Post post = findPostById(postId);

        // 포스트 작성자가 맞는지 체크
        if (hasRightToEdit(post, userId)) {

            post.delete();

        }

    }

    @Transactional
    public DetailPostResponse findById(Long postId, Long userId) {

        Post post = findPostById(postId);

        // 조회수 증가
        post.increaseViewCount();

        User user = post.getUser();

        // 댓글 조회
        List<Comment> comments = commentRepository.findByPostAndIsDeletedFalse(post);
        List<CommentItem> commentItems = comments.stream()
                .map(comment -> {
                    User commentUser = comment.getUser();
                    WriterInfo commentWriter = new WriterInfo(
                            commentUser.getId(),
                            commentUser.getNickname(),
                            commentUser.getProfileImageUrl()
                    );
                    return new CommentItem(
                            comment.getId(),
                            comment.getContent(),
                            comment.getCreatedAt(),
                            comment.getUpdatedAt(),
                            commentWriter
                    );
                })
                .toList();

        // 통계 정보 생성
        boolean isLiked = likeRepository.existsByIdPostIdAndIdUserId(postId, userId);
        DetailStatistic detailStatistics = new DetailStatistic(
                post.getViewCount(),
                commentItems.size(),
                post.getLikeCount(),
                isLiked
        );

        return DetailPostResponse.builder()
                .postId(postId)
                .title(post.getTitle())
                .content(post.getContent())
                .postImageUrl(post.getPostImageUrl())
                .createdAt(post.getCreatedAt())
                .writerInfo(new WriterInfo(user.getId(), user.getNickname(), user.getProfileImageUrl()))
                .detailStatistics(detailStatistics)
                .comments(commentItems)
                .build();
    }

    // - Helper Method

    private boolean hasRightToEdit(Post post, Long userId) {

        if (!post.getUser().getId().equals(userId)) {
            throw new ForbiddenException("게시물 수정 권한이 없는 ID입니다.");
        }

        return true;
    }

    private Post findPostById(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물ID 입니다."));

        if (post.isDeleted()) {
            throw new IllegalArgumentException("삭제된 게시물입니다.");
        }

        return post;

    }

    // Public method for other services to validate post
    public Post getValidPost(Long postId) {
        return findPostById(postId);
    }


}
