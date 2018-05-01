package com.troy.uaa.web.rest.filter;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.config.CloudProperties;
import com.troy.keeper.core.utils.JsonUtils;
import com.troy.uaa.domain.User;
import com.troy.uaa.repository.UserRepository;
import com.troy.uaa.service.TenantService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * author yjm
 */
public class AuthorizationLoginFilter extends OncePerRequestFilter {

    @Autowired
    private TenantService tenantService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if ("/login".equals(httpServletRequest.getServletPath()) && httpServletRequest.getMethod().equals("POST")){//是登录
            String username = httpServletRequest.getParameter("username");
            String password = httpServletRequest.getParameter("password");
            User user = userRepository.findUserByLogin(username);
            ResponseDTO responseDTO = null;
            httpServletResponse.setHeader("Content-type", "text/html;charset=UTF-8");
            httpServletResponse.setCharacterEncoding("UTF-8");
            if (user == null){
                responseDTO = new ResponseDTO("203","用户不存在",false);
                httpServletResponse.getWriter().println(JsonUtils.toJson(responseDTO));
            }else if (!passwordEncoder.matches(password,user.getPassword())){
                responseDTO = new ResponseDTO("203","密码错误",false);
                httpServletResponse.getWriter().println(JsonUtils.toJson(responseDTO));
            }else if(user.getTenantId() != null){
                String status = tenantService.queryStatusById(user.getTenantId());
                if (StringUtils.isEmpty(status) || status.equals("0")){
                    responseDTO = new ResponseDTO("203","租户已停用",false);
                    httpServletResponse.getWriter().println(JsonUtils.toJson(responseDTO));
                }
            }
            if (responseDTO == null) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            }
        }else{
            filterChain.doFilter(httpServletRequest,httpServletResponse);
        }
    }
}
