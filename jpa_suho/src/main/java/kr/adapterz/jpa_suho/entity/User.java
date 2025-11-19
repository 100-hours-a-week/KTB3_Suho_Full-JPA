package kr.adapterz.jpa_suho.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false, unique = true, length = 10)
    private String nickname;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "pw_updated_at")
    private LocalDateTime pwUpdatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;



    protected User() {
        // JPA용 기본 생성자
    }

    public User(String email, String password, String nickname) {

        this.email = email;

        this.password = password;

        this.nickname = nickname;

        this.createdAt = LocalDateTime.now();

        this.isDeleted = false;

    }

    public User(String email, String password, String nickname, String profileImageUrl) {

        this.email = email;

        this.password = password;

        this.nickname = nickname;

        this.profileImageUrl = profileImageUrl;

        this.createdAt = LocalDateTime.now();

        this.isDeleted = false;

    }

    public void changePassword(String newPassword) {

        this.password = newPassword;

        pwUpdatedAt = LocalDateTime.now();

    }

    public void changeNickname(String newNickname) {

        this.nickname = newNickname;

        updatedAt = LocalDateTime.now();

    }

    public void delete() {

        this.isDeleted = true;

        this.deletedAt = LocalDateTime.now();

    }

}
