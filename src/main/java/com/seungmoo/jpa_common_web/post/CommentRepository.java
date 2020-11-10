package com.seungmoo.jpa_common_web.post;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 스프링 데이터 JPA가 제공하는 Repository의 모든 "메소드"에는 기본적으로 @Transaction이 적용되어 있음.
 * --> 보통 find성 메소드에는 readOnly = true가 적용되어 있음.
 *
 * 스프링 @Transactional
 * - 클래스, 인터페이스, 메소드에 사용할 수 있으며, 메소드에 가장 가까운 애노테이션이 우선 순위가 높다.
 * - RuntimeException, Error --> Rollback 수행
 * - CheckedException --> Rollback 미수행
 *
 * 스프링 트랜잭션 ISOLATION 전략 (대부분의 DB는 READ_COMMITTED가 default 이다.)
 * DEFAULT, READ_COMMITTED, READ_UNCOMMITTED, REPEATABLE_READ, SERIALIZABLE
 * READ_COMMITTED --> dirty read 방지, phantom read / non-repeatable read 발생 가능, 대부분의 DEFAULT 임.
 * READ_UNCOMMITTED --> phantom read, dirty read, non-repeatable read 발생 가능
 * REPEATABLE_READ --> dirty read / non-repeatable read 방지, phantom read 발생 가능
 * SERIALIZABLE --> 성능이 가장 안좋다.. (직렬화 처리 하기 때문에 트랜잭션 하나씩 처리 해줌)  dirty read / non-repeatable read / phantom read 모두 방지
 *
 * https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/transaction/annotation/Transactional.html (반드시 읽어볼 것)
 */
// JpaSpecificationExecutor<Comment> --> JPA Specification 설정
public interface CommentRepository
        extends JpaRepository<Comment, Long>,
                JpaSpecificationExecutor<Comment>,
                QueryByExampleExecutor<Comment> {

    // EntityGraphType 은 FETCH / LOAD 모드가 있다.
    // FETCH : 설정한 엔티티 애튜리뷰트는 EAGER 패치, 나머지는 LAZY
    // LOAD : 설정한 엔티티 애튜리뷰트는 EAGER 패치, 나머지는 기본 패치 전략 따름.
    @EntityGraph(value = "Comment.post", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Comment> getById(Long id);

    // 모든 컬럼 (*)이 아닌 Select 절 Projection을 적용한다.
    // readOnly를 쓰는게 좋다. (flush 모드를 NEVER로 설정하여 Dirty Checking이 발생하지 않는다.)
    // Dirty Checking이 발생하지 않아야 성능에 최적화가 된다.
    @Transactional(readOnly = true) // 이렇게 해주는게 좋음
    <T> List<T> findByPost_Id(Long id, Class<T> type);

}
