package com.seungmoo.jpa_common_web;

import com.seungmoo.jpa_common_web.post.Post;
import com.seungmoo.jpa_common_web.post.PostRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc // Integration test
//@ActiveProfiles("test") // profile 설정, application-test.properties
@Transactional
public class PostControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    PostRepository postRepository;

    @Test
    public void getPost() throws Exception {
        Post post = new Post();
        post.setTitle("jpa");
        postRepository.save(post);

        mockMvc.perform(get("/posts/" + post.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("jpa"));
    }

    /**
     * Pageable과 Sort를 사용해보자
     * request의 파라미터로 pageable, sort 관련 파라미터를 넣을 수 있다.
     * @throws Exception
     */
    @Test
    public void getPosts() throws Exception {
        Post post = new Post();
        post.setTitle("jpa");
        postRepository.save(post);

        mockMvc.perform(get("/posts/")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "created,desc")
                        .param("sort", "title"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title", is("jpa")));
    }

    /**
     * spring data common의 hateoas 지원 (web mvc에서만 가능)
     * @throws Exception
     */
    @Test
    public void getPostsHateoas() throws Exception {
        createPosts();

        mockMvc.perform(get("/posts/hateoas")
                .param("page", "3")
                .param("size", "10")
                .param("sort", "created,desc")
                .param("sort", "title"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title", is("jpa")));
    }

    private void createPosts() {
        int postsCount = 100;
        while(postsCount > 0) {
            Post post = new Post();
            post.setTitle("jpa");
            postRepository.save(post);
            postsCount--;
        }
    }

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * JpaRepository.save() 메소드에 대해서 알아보자
     */
    @Test
    public void save() {

        // 스프링 데이터 jpa 판단 로직
        // id property의 값이 null이면 Transient
        // id 값이 있으면 Detached 상태이다. (Detached는 한번이라도 Persistent상태 였던 객체를 나타내기도 한다.)

        // 여기서 save()는 ID가 없으므로 persist()를 호출한다. (INSERT 쿼리)
        Post post = new Post();
        //post.setId(1l);
        post.setTitle("jpa");
        Post savedPost = postRepository.save(post); // EntityManager.persist() - insert Query 발생, Transient --> Persistent로 state 변경
        // persist() 메소드에 넘긴 그 엔티티 객체를 Persistent 상태로 변경한다.

        assertThat(entityManager.contains(post)).isTrue();
        assertThat(entityManager.contains(savedPost)).isTrue();
        assertThat(post == savedPost).isTrue(); // 이 둘은 동일한 객체인다.

        // 여기서 save()는 merge()를 호출한다. (ID가 있으므로, update를 호출함.)
        Post postUpdate = new Post();
        postUpdate.setId(post.getId());
        postUpdate.setTitle("hibernate");
        Post updatedPost = postRepository.save(postUpdate); // EntityManager.merge() - update Query 발생, Detached --> Persistent로 state 변경
        // merge() 메소드에 넘긴 그 엔티티의 복사본을 만들고, 그 복사본을 다시 Persistent상태로 변경하고 그 복사본을 반환 한다.
        // 여기서는 복사본이란 updatedPost 임.
        // merge() 메소드의 파라미터 postUpdate는 이제 더 이상 Persistent 상태가 아니다.

        assertThat(entityManager.contains(updatedPost)).isTrue();
        assertThat(entityManager.contains(postUpdate)).isFalse(); // merge()에 사용된 엔티티 객체는 더이상 persistent 상태가 아니게 된다.
        assertThat(updatedPost == postUpdate).isFalse();

        // 이렇게만 해줘도 위의 "hibernate"가 아닌, "seungmoo" title을 가진 상태로 DB에 반영 된다. (Persistent 객체이기 때문에 JPA가 추적)
        updatedPost.setTitle("seungmoo"); // JPA가 알아서 객체 상태의 변화를 감지하고 DB에 Sync한다.

        // 중요!! 객체를 이어서 쓸 때는 parameter로 들어간 객체가 아닌, Return된 객체를 사용하라.

        List<Post> all = postRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
    }

    @Test
    public void findByTitleStartsWith() {
        Post post = new Post();
        post.setTitle("Spring Data Jpa");
        Post savedPost = postRepository.save(post); // persist

        List<Post> all = postRepository.findByTitleStartsWith("Spring");
        assertThat(all.size()).isEqualTo(1);
    }

    private void savePost() {
        Post post = new Post();
        post.setTitle("Spring");
        postRepository.save(post); // persist
    }

    @Test
    public void findByTitle() {
        savePost();
        List<Post> all = postRepository.findByTitle("Spring");
        assertThat(all.size()).isEqualTo(1);
    }
}