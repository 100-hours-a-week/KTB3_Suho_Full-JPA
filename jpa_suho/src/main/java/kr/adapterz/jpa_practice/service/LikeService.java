package kr.adapterz.jpa_practice.service;

import kr.adapterz.jpa_practice.entity.Like;
import kr.adapterz.jpa_practice.entity.Post;
import kr.adapterz.jpa_practice.repository.LikeRepository;
import kr.adapterz.jpa_practice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    @Transactional
    public int createLike(Long postId, Long userId) {

        // 게시물 존재 및 삭제 여부 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물ID 입니다."));

        if (post.isDeleted()) {
            throw new IllegalArgumentException("삭제된 게시물입니다.");
        }

        // 이미 좋아요했는지 확인
        if (likeRepository.existsByIdPostIdAndIdUserId(postId, userId)) {
            throw new IllegalArgumentException("이미 좋아요한 게시물입니다.");
        }

        // 좋아요 생성
        Like like = new Like(postId, userId);
        likeRepository.save(like);

        post.increaseLikeCount();

        // 해당 게시물의 좋아요 수 반환
        return post.getLikeCount();
    }

    @Transactional
    public void deleteLike(Long postId, Long userId) {

        // 게시물 존재 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물ID 입니다."));

        // 좋아요 존재 확인
        Like like = likeRepository.findByIdPostIdAndIdUserId(postId, userId)
                .orElseThrow(() -> new IllegalArgumentException("좋아요하지 않은 게시물입니다."));

        // 좋아요 삭제
        likeRepository.delete(like);

        post.decreaseLikeCount();
    }

}
