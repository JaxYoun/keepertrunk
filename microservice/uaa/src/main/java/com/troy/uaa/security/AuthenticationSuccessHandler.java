package com.troy.uaa.security;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.config.CloudProperties;
import com.troy.keeper.core.security.SecurityUtils;
import com.troy.keeper.core.utils.JsonUtils;
import com.troy.keeper.util.AddressUtils;
import com.troy.keeper.util.WebUtils;
import com.troy.uaa.security.oauth2.Oauth2TokenService;
import com.troy.uaa.service.LogsearchService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Spring Security success handler, specialized for Ajax requests.
 */
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


    private Oauth2TokenService oauth2TokenService;

    private CloudProperties cloudProperties;

    private LogsearchService logsearchService;


    public AuthenticationSuccessHandler(Oauth2TokenService oauth2TokenService, CloudProperties cloudProperties,LogsearchService logsearchService) {
        super(cloudProperties.getLogin().getSuccessLoginUrl());
        this.oauth2TokenService = oauth2TokenService;
        this.cloudProperties = cloudProperties;
        this.logsearchService = logsearchService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
        throws IOException, ServletException {
        oauth2TokenService.associateOauth2Token(request, response);
        String ip  = "0:0:0:0:0:0:0:1".equals(AddressUtils.getIpAddress(request))?"127.0.0.1":AddressUtils.getIpAddress(request);
        logsearchService.saveLoginLog(SecurityUtils.getCurrentUserId().toString(),request.getSession().getId(),ip);
        if (WebUtils.isAjax(request)) {
            response.setStatus(HttpServletResponse.SC_OK);
            ResponseDTO responseDTO = new ResponseDTO("200","ok",true);
            response.getWriter().println(JsonUtils.toJson(responseDTO));
        } else {
            this.handle(request, response, authentication);
        }
    }
}
