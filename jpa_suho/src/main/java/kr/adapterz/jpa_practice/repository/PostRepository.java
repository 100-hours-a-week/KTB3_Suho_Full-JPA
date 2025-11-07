package kr.adapterz.jpa_practice.repository;

import kr.adapterz.jpa_practice.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT p FROM Post p WHERE p.id < :postId AND p.isDeleted = false")
    List<Post> findPostsWithSize(@Param("postId") Long postId, Pageable pageable);

}
