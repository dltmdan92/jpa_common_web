package com.seungmoo.jpa_common_web;

import com.seungmoo.jpa_common_web.post.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Audit은 이론적으로 Spring Security에서 Authentication을 받아와서 처리하는 것임
 * 본 예제에서는 별도의 AuditorAware 구현체를 Bean으로 만들어서 호출되는지만 확인해본다.
 */
// 참고로 bean의 이름은 accountAuditAware 이렇게 됨
@Slf4j
@Service // @DataJpaTest 로 Test하면 이 Bean이 테스트될까??? --> @DataJpaTest는 데이터 계층의 Bean만 테스트한다. (이거는 어쩔수 없이 Slicing test를 할 수 없다.)
public class AccountAuditAware implements AuditorAware<Account> {
    @Override
    public Optional<Account> getCurrentAuditor() {
        log.info("looking for current user");
        return Optional.empty();
    }
}
