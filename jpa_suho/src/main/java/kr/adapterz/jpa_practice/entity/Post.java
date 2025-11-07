package kr.adapterz.jpa_practice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name="posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "title", nullable = false, length = 26)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    private String postImageUrl;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @Column(name = "like_count", nullable = false)
    private int likeCount;

    @Column(name = "comment_count", nullable = false)
    private int commentCount;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    protected Post() {
        // JPA용 기본 생성자
    }

    public Post(User user, String title, String content, String postImageUrl) {

        this.user = user;

        this.title = title;

        this.content = content;

        this.postImageUrl = postImageUrl;

        this.viewCount = 0;

        this.likeCount = 0;

        this.commentCount = 0;

        this.createdAt = LocalDateTime.now();

        isDeleted = false;

    }

    public void changePost(String newTitle, String newContent, String newPostImageUrl){

        boolean changed = false;

        if (!this.title.equals(newTitle)) {
            this.title = newTitle;
            changed = true;
        }

        if (!this.content.equals(newContent)) {
            this.content = newContent;
            changed = true;
        }

        if (!java.util.Objects.equals(this.postImageUrl, newPostImageUrl)) {
            this.postImageUrl = newPostImageUrl;
            changed = true;
        }

        if (changed) {

            this.updatedAt = LocalDateTime.now();

        }
    }

    public void delete() {

        this.isDeleted = true;

        this.deletedAt = LocalDateTime.now();

    }

    public void increaseViewCount() {

        this.viewCount++;

    }

    public void increaseCommentCount() {

        this.commentCount++;

    }

    public void decreaseCommentCount() {

        this.commentCount--;

    }

    public void increaseLikeCount() {

        this.likeCount++;

    }

    public void decreaseLikeCount() {

        this.likeCount--;

    }
}
