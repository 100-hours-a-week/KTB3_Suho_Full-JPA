package kr.adapterz.jpa_practice.repository;

import kr.adapterz.jpa_practice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    User findByEmail(String email);

}



