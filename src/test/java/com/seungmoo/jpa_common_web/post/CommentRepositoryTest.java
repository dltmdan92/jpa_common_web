package com.seungmoo.jpa_common_web.post;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringRunner.class)
//@DataJpaTest
@SpringBootTest
public class CommentRepositoryTest {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostRepository postRepository;

    /**
     * @ManyToOne : fetch 전략이 EAGER이다.
     *
     */
    @Test
    public void getComment() {
        Post post = new Post();
        post.setTitle("jpa");
        Post savedPost = postRepository.save(post); // 엔티티 객체의 경우 웬만하면 Return 된 것을 받아서 쓰도록 한다.

        Comment comment = new Comment();
        comment.setComment("comment");
        comment.setPost(savedPost);

        Optional<Comment> byId = commentRepository.findById(1l);
        log.info((byId.get().getPost().toString()));
    }

    @Test
    public void loadTest() {
        commentRepository.getById(1l); // 엔티티그룹에서의 패칭 전략이 적용 (FETCH : 그룹에 셋팅된 속성들은 EAGER)

        System.out.println("==============================");

        commentRepository.findById(1l); // LAZY 전략으로 데이터를 읽어온다.
    }

    @Test
    public void getComment2() {
        commentRepository.findByPost_Id(1l, CommentSummary.class);
    }

    @Test
    public void getComment3() {
        Post post = new Post();
        post.setTitle("jps");
        Post savedPost = postRepository.save(post);

        Comment comment = new Comment();
        comment.setPost(savedPost);
        comment.setUp(10);
        comment.setDown(1);
        comment.setComment("spring data jpa projection");
        commentRepository.save(comment);

        commentRepository.findByPost_Id(savedPost.getId(), CommentSummary.class).forEach(s -> log.info(s.getVotes2()));
        commentRepository.findByPost_Id(savedPost.getId(), CommentOnly.class).forEach(s -> log.info(s.getComment()));
    }

    /**
     * Specification의 장점 : 클라이언트 소스가 간단해진다. (관련 조건들을 미리 정의)
     */
    @Test
    public void specs() {
        commentRepository.findAll(CommentSpecs.isBest().or(CommentSpecs.isGood()), PageRequest.of(0, 10));
    }

    /**
     * 스프링 데이터 Query By Example
     * -> 예제 객체를 만들어서 쿼리를 호출
     *
     * Example = Prove + ExampleMatcher
     * •	Prove는 필드에 어떤 값들을 가지고 있는 도메인 객체.
     * •	ExampleMatcher는 Prove에 들어있는 그 필드의 값들을 어떻게 쿼리할 데이터와 비교할지 정의한 것.
     * •	Example은 그 둘을 하나로 합친 것. 이걸로 쿼리를 함.
     *
     * 장점
     * •	별다른 코드 생성기나 애노테이션 처리기 필요 없음.
     * •	도메인 객체 리팩토링 해도 기존 쿼리가 깨질 걱정하지 않아도 됨.(뻥)
     * •	데이터 기술에 독립적인 API
     * 단점
     * •	nested 또는 프로퍼티 그룹 제약 조건을 못 만든다.
     * •	조건이 제한적이다. 문자열은 starts/contains/ends/regex 가 가능하고 그밖에 property는 값이 정확히 일치해야 한다.
     */
    @Test
    public void qbe() {
        // 예제 객체 생성, best = true, up = 0, down = 0
        Comment prove = new Comment();
        prove.setBest(true);

        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny();

        Example<Comment> example = Example.of(prove, exampleMatcher);

        commentRepository.findAll(example);
    }

}