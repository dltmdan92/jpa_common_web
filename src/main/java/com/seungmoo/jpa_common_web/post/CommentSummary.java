package com.seungmoo.jpa_common_web.post;

import org.springframework.beans.factory.annotation.Value;

/**
 * SQL의 select 절 Project (우리가 필요로 하는 Column들만 갖고 오도록 한다.)
 */
public interface CommentSummary {

    String getComment();

    int getUp();

    int getDown();

    // 이거는 Open Projection임. --> 모든 column을 다 갖고 오게 된다.
    // 문자열 처리 등을 수행할 수 있다.
    /*
    @Value("#{target.up + ' ' + target.down}")
    String getVotes();
     */

    /**
     * 위의 방식 (Open Projection)이 아닌, java8 default method를 통해서
     * closed Projection 을 사용할 수 있다.
     * 쿼리 최적화 + 커스터마이징 --> 이 방법이 가장 좋음
     * interface가 아닌 별도 class로 만들 수 있다. (but interface에서 하는 걸 추천)
     * @return
     */
    default String getVotes2() {
        return getUp() + " " + getDown();
    }
}
