package kr.adapterz.jpa_practice.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserTest {

    @PersistenceContext
    EntityManager em;

    @Test
    @Rollback(false)
    void idTest() {

        User user = new User("tester@adaperterz.kr", "123aS!", "Adapterz");
        em.persist(user);

    }

}