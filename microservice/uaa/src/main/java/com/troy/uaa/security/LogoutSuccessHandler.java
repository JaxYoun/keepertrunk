package com.troy.uaa.security;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.uaa.service.LogsearchService;
import io.github.jhipster.security.AjaxLogoutSuccessHandler;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yjm on 2017/4/25.
 */
public class LogoutSuccessHandler extends AjaxLogoutSuccessHandler {

    private LogsearchService logsearchService;

    public LogoutSuccessHandler(LogsearchService logsearchService) {
        this.logsearchService = logsearchService;
    }

    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Cookie[] cookies = request.getCookies();
        String sessionId = "";
        if (cookies != null){
            for (Cookie cookie : cookies) {
                if ("SESSION".equals(cookie.getName())){
                    sessionId = cookie.getValue();
                }
            }
        }
        ResponseDTO re  = logsearchService.saveLogoutLog(sessionId);
        response.setStatus(200);
    }
}
