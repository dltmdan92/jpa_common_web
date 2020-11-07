package com.seungmoo.jpa_common_web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories // 스프링 부트에서는 생략 가능하다, 스프링부트 미사용일 떄는 @Configuration과 같이 사용
public class JpaCommonWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaCommonWebApplication.class, args);
    }

}
