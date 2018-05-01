package com.troy.keeper.core.filter;

import com.troy.keeper.core.context.UserDetailContextHolder;
import com.troy.keeper.core.utils.string.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Harry on 2017/9/28.
 */
public class UserDetailDecideFilter extends OncePerRequestFilter  {

    private final static String PARAMETER = "detailType";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ("/api/system/login/doLogin".equals(request.getServletPath()) && request.getMethod().equals("POST")) {//是登录
            String type = request.getParameter(PARAMETER);
            if (StringUtils.isEmpty(type)) {
                type = (String) request.getAttribute(PARAMETER);
            }
            UserDetailContextHolder.setType(type);
            filterChain.doFilter(request,response);
        }else {
            filterChain.doFilter(request,response);
        }

    }
}
