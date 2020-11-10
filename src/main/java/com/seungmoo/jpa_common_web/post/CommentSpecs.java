package com.seungmoo.jpa_common_web.post;

import org.springframework.data.jpa.domain.Specification;

/**
 * 쿼리 메소드는 메소드명이 길어진다는 단점이 있음.
 * QueryDSL 또는 Specification을 사용하는 것이 좋다.
 * 특히 반복적인 조건이 생긴다면 Specification으로 만들어서 재사용 및 여러 Specification과 조합 가능하다.
 */
public class CommentSpecs {
    public static Specification<Comment> isBest() {
        return (Specification<Comment>)
                (root, criteriaQuery, criteriaBuilder)
                        -> criteriaBuilder.isTrue(root.get(Comment_.best));
    }

    public static Specification<Comment> isGood() {
        return (Specification<Comment>)
                (root, criteriaQuery, criteriaBuilder)
                        -> criteriaBuilder.greaterThanOrEqualTo(root.get(Comment_.up), 10);
    }
}
