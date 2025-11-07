package kr.adapterz.jpa_practice.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "likes")
public class Like {

    @EmbeddedId
    private LikeId id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected Like() {
        // JPA용 기본 생성자
    }

    public Like(Long postId, Long userId) {
        this.id = new LikeId(postId, userId);
        this.createdAt = LocalDateTime.now();
    }

}
