package kr.adapterz.jpa_practice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @Builder
    public Comment(Post post, User user, String content) {

        this.post = post;

        this.user = user;

        this.content = content;

        this.createdAt = LocalDateTime.now();

        this.isDeleted = false;

    }

    public void changeContent(String newContent) {

        this.content = newContent;

        this.updatedAt = LocalDateTime.now();

    }

    public void delete() {

        this.isDeleted = true;

        this.deletedAt = LocalDateTime.now();

    }

}
