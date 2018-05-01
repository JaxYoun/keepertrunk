package com.troy.uaa.security;

import com.hazelcast.util.StringUtil;
import com.troy.keeper.core.base.entity.Account;
import com.troy.uaa.domain.User;
import com.troy.uaa.domain.UserRole;
import com.troy.uaa.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;

    public DomainUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);
        String lowercaseLogin = login;
//            .toLowerCase(Locale.ENGLISH);
        Optional<User> userFromDatabase = userRepository.findOneWithAuthoritiesByLogin(lowercaseLogin);
        return userFromDatabase.map(user -> {
//            if (!user.getActivated()) {
//                throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
//            }
            List<GrantedAuthority> grantedAuthorities = user.getUserRoleList().stream()
                    .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getRoleCode()))
                .collect(Collectors.toList());
            if (user.getUserRoleList() != null && !user.getUserRoleList().isEmpty()){
                for (UserRole userRole : user.getUserRoleList()) {
                    String roleCode = userRole.getRole() != null?
                            userRole.getRole().getRoleCode():"";
                    if (StringUtils.isNotBlank(roleCode) && roleCode.equals("ROLE_AD")){
                        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                    }
                }
            }
            return new Account(lowercaseLogin,
                user.getPassword(),
                grantedAuthorities,user.getId());
        }).orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the " +
        "database"));
    }
}
