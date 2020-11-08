package com.seungmoo.jpa_common_web.post;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringRunner.class)
@DataJpaTest
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
}