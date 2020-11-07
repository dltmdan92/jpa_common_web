package com.seungmoo.jpa_common_web.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// @Repository를 달면 좋은점. SQLException 또는 JPA 관련 예외를 스프링의 DataAccessException으로 변환 해준다. (Exception 세분화)
@Repository // 이거는 굳이 달지 않아도 된다. JpaRepository의 구현체(프록시로 구현) SimpleJpaRepository가 @Repository를 달고 있음
public interface PostRepository extends JpaRepository<Post, Long> {
}
