package com.troy.keeper.core.security;

import com.troy.keeper.core.Constants;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 * Implementation of AuditorAware based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<Long> {

    @Override
    public Long getCurrentAuditor() {
//        String userName = SecurityUtils.getCurrentUserLogin();
//        return userName != null ? userName : Constants.SYSTEM_ACCOUNT;
        Long userId = SecurityUtils.getCurrentUserId();
        return userId != null ? userId: Constants.SYSTEM_ACCOUNT;
    }
}
