package kr.adapterz.jpa_practice.entity;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class LikeId implements Serializable {

    private Long postId;
    private Long userId;

    public LikeId(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }

}
