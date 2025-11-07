package kr.adapterz.jpa_practice.service;

import kr.adapterz.jpa_practice.entity.Comment;
import kr.adapterz.jpa_practice.entity.Post;
import kr.adapterz.jpa_practice.entity.User;
import kr.adapterz.jpa_practice.exception.ForbiddenException;
import kr.adapterz.jpa_practice.repository.CommentRepository;
import kr.adapterz.jpa_practice.repository.PostRepository;
import kr.adapterz.jpa_practice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final UserRepository userRepository;
    private final PostService postService;
    private final CommentRepository commentRepository;

    @Transactional
    public Long create(Long postId, Long userId, String content) {

        Post post = postService.getValidPost(postId);

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 사용자ID 입니다."));

        if (user.isDeleted()) {
            throw new IllegalArgumentException("탈퇴한 사용자입니다.");
        }

        Comment comment = Comment.builder().post(post).user(user).content(content).build();

        commentRepository.save(comment);

        post.increaseCommentCount();

        return comment.getId();
    }

    @Transactional
    public Long update(Long userId, Long postId, Long commentId, String content) {

        postService.getValidPost(postId);

        Comment comment = findById(commentId);

        if(!comment.getUser().getId().equals(userId)) {
            throw new ForbiddenException("댓글을 수정할 권한이 없습니다.");
        }

        comment.changeContent(content);

        return comment.getId();
    }

    @Transactional
    public void delete(Long userId, Long postId, Long commentId) {

        postService.getValidPost(postId);

        Comment comment = findById(commentId);

        if(!comment.getUser().getId().equals(userId)) {
            throw new ForbiddenException("댓글을 삭제할 권한이 없습니다.");
        }

        comment.delete();

        comment.getPost().decreaseCommentCount();
    }


    // MARK: Helper Method

    private Comment findById(Long commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 댓글ID 입니다."));

        if (comment.isDeleted()) {
            throw new IllegalArgumentException("삭제된 댓글입니다.");
        }

        return comment;

    }
}
