package com.seungmoo.jpa_common_web.post;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// @Repository의 기능 SQLException 또는 JPA 관련 예외를 스프링의 DataAccessException으로 변환 해준다. (Exception 세분화)
//@Repository // 이거는 굳이 달지 않아도 된다. JpaRepository의 구현체(프록시로 구현) SimpleJpaRepository가 @Repository를 달고 있음
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitleStartsWith(String title);
    List<Post> findByTitle(String title); // Entity에 설정한 Named Query를 찾아서 사용한다.

    // 근데 위처럼 NamedQuery 쓸 바에는 그냥 이렇게 @Query 애노테이션 쓰는게 편하다.
    @Query(value = "SELECT p FROM Post AS p WHERE p.title = ?1", nativeQuery = false)
    List<Post> findByTitle2(String title, Sort sort); // Sort에는 Property 또는 alias가 들어가야 한다.

    // Named Parameter
    // #{#entityName} - EntityName을 받아서 사용할 수 있다. (Entity 객체에서 별도로 Entity name을 셋팅했을 경우 유용)
    @Query(value = "SELECT p FROM #{#entityName} AS p WHERE p.title = :title", nativeQuery = false)
    List<Post> findByTitleWithNamedParam(@Param("title") String keyword, Sort sort);

    // Update 쿼리
    // clearAutomatically - persistent 상태인 Cache들을 clear한다.
    // flushAutomatically - 현재 모든 액션들을 DB에 sync한다.
    // update는 이렇게 @Query 하는 것을 권장하지 않는다. jpa의 API를 사용하도록 하자...
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Post p SET p.title = ?1 WHERE p.id = ?2")
    int updateTitle(String title, Long id);
}
