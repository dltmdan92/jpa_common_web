package com.seungmoo.jpa_common_web.post;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

// 연관 관계 설정, 다양한 Entity Group을 정의할 수 있으며, 각 그룹에 따른 각각의 Fetching 전략을 적용할 수 있다.
@NamedEntityGraph(name = "Comment.post", attributeNodes = @NamedAttributeNode("post"))
@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Comment {
    @Id @GeneratedValue
    private Long id;

    private String comment;

    private int up;

    private int down;

    private boolean best;

    /**
     * Auditing 기능
     * 엔티티의 변경 시점에 언제, 누가 변경했는지에 대한 정보를 기록하는 기능
     *
     * 아쉽게도 이 기능은 스프링 부트가 자동 설정 해주지 않는다.
     * 1. 메인 애플리케이션 위에 @EnableJpaAuditing 추가
     * 2. 엔티티 클래스 위에 @EntityListeners(AuditingEntityListener.class)추가
     * 3. AuditorAware 구현체 만들기
     * @EnableJpaAuditing에 AuditorAware 빈 이름 설정하기.
     *
     * 현재 이 프로젝트에서는 스프링 시큐리티를 설정하기 않았으므로
     * createdBy와 updatedBy의 Account 정보를 알 수 없음...
     * But 방법이 있음 AuditorAware interface의 구현체를 만들자.
     */
    @CreatedDate
    private Date created;

    @CreatedBy
    @ManyToOne
    private Account createdBy;

    @LastModifiedDate
    private Date updated;

    @LastModifiedBy
    @ManyToOne
    private Account updatedBy;

    /**
     * @ManyToOne 의 경우 FetchType의 DEFAULT는 EAGER이다.
     * 끝이 One으로 끝나는 경우 FetchType.EAGER 가 Default 이며
     * 끝이 Many로 끝나는 경우 FetchType.LAZY 가 Default 이다.
     *
     * 본 예제에서는 Fetching 전략을 LAZY로 강제 셋팅해보겠음
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    // Callback eventListener를 활용해보는 것도 좋다.
    @PrePersist
    public void prePersist() {
        System.out.println("JPA가 제공하는 Callback LifeCycle Event를 사용할 수 있다. Auditing에도 응용 가능함.");
    }
}
