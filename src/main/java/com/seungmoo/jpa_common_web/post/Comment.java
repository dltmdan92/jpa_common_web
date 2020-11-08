package com.seungmoo.jpa_common_web.post;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

// 연관 관계 설정, 다양한 Entity Group을 정의할 수 있으며, 각 그룹에 따른 각각의 Fetching 전략을 적용할 수 있다.
@NamedEntityGraph(name = "Comment.post", attributeNodes = @NamedAttributeNode("post"))
@Entity
@Getter
@Setter
public class Comment {
    @Id @GeneratedValue
    private Long id;

    private String comment;

    private int up;

    private int down;

    private boolean best;

    /**
     * @ManyToOne 의 경우 FetchType의 DEFAULT는 EAGER이다.
     * 끝이 One으로 끝나는 경우 FetchType.EAGER 가 Default 이며
     * 끝이 Many로 끝나는 경우 FetchType.LAZY 가 Default 이다.
     *
     * 본 예제에서는 Fetching 전략을 LAZY로 강제 셋팅해보겠음
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
}
