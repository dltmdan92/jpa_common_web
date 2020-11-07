package com.seungmoo.jpa_common_web;

import com.seungmoo.jpa_common_web.post.Post;
import com.seungmoo.jpa_common_web.post.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.hateoas.EntityModel;

import java.util.Optional;

@RestController
public class PostController {
    @Autowired
    private PostRepository postRepository;

    /**
     * 스프링 데이터 DomainClassConverter
     * 스프링 데이터의 Domain Class Converting 기능
     * 주의 : 파라미터 명과 pathVariable명이 다르기 때문에 pathVariable name 명시 필요
     * Converter와 Formatter의 차이는 한번 알아볼 것
     * @param post
     * @return
     */
    @GetMapping("/posts/{id}")
    public String getPost(@PathVariable("id") Post post) {
        return post.getTitle();
    }

    /**
     * Pageable과 Sort를 매개변수로 받아보자
     * @param pageable
     * @return
     */
    @GetMapping("/posts")
    public Page<Post> getPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    /**
     * spring data common의 hateoas 기능
     * @param pageable
     * @param assembler
     * @return
     */
    @GetMapping("/posts/hateoas")
    public PagedModel<EntityModel<Post>> getPostsWithHateoas(Pageable pageable, PagedResourcesAssembler<Post> assembler) {
        return assembler.toModel(postRepository.findAll(pageable));
    }
}
