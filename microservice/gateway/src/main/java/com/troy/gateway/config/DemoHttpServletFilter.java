package com.troy.gateway.config;

import com.troy.keeper.core.Constants;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: xiongzhan
 * Date: 17-4-26
 * Time: 下午1:52
 * To change this template use File | Settings | File Templates.
 */
public class DemoHttpServletFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        System.out.printf("session:%s", session);
        filterChain.doFilter(request, response);
    }

}
