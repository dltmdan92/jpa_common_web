package com.seungmoo.jpa_common_web.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

// @Repository의 기능 SQLException 또는 JPA 관련 예외를 스프링의 DataAccessException으로 변환 해준다. (Exception 세분화)
//@Repository // 이거는 굳이 달지 않아도 된다. JpaRepository의 구현체(프록시로 구현) SimpleJpaRepository가 @Repository를 달고 있음
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitleStartsWith(String title);
    List<Post> findByTitle(String title); // Entity에 설정한 Named Query를 찾아서 사용한다.

    // 근데 위처럼 NamedQuery 쓸 바에는 그냥 이렇게 @Query 애노테이션 쓰는게 편하다.
    @Query(value = "SELECT p FROM Post AS p WHERE p.title = ?1", nativeQuery = false)
    List<Post> findByTitle2(String title);
}
