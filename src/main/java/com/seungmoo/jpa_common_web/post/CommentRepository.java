package com.seungmoo.jpa_common_web.post;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // EntityGraphType 은 FETCH / LOAD 모드가 있다.
    // FETCH : 설정한 엔티티 애튜리뷰트는 EAGER 패치, 나머지는 LAZY
    // LOAD : 설정한 엔티티 애튜리뷰트는 EAGER 패치, 나머지는 기본 패치 전략 따름.
    @EntityGraph(value = "Comment.post", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Comment> getById(Long id);

    // 모든 컬럼 (*)이 아닌 Select 절 Projection을 적용한다.
    <T> List<T> findByPost_Id(Long id, Class<T> type);

}
