package com.troy.uaa.security.oauth2.impl;

import com.troy.keeper.core.Constants;
import com.troy.uaa.security.oauth2.Oauth2TokenService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created with IntelliJ IDEA.
 * User: xiongzhan
 * Date: 17-4-27
 * Time: 上午10:02
 * To change this template use File | Settings | File Templates.
 */
@Service
public class Oauth2TokenServiceImpl implements Oauth2TokenService {

    private final Logger LOGGER = LoggerFactory.getLogger(Oauth2TokenServiceImpl.class);

    @Autowired
    private TokenEndpoint tokenEndpoint;

//    @Autowired
//    private RedisTemplate<String, String> tokenRedisTemplate;

    @Override
    public void associateOauth2Token(HttpServletRequest request, HttpServletResponse response) {
        try {
            OAuth2AccessToken oAuth2AccessToken = obtainOauth2Token(request);
            request.getSession().setAttribute(Constants.OAUTH2_ACCESS_TOKEN,oAuth2AccessToken);
//            Cookie[] cookies = request.getCookies();
//            if (ArrayUtils.isNotEmpty(request.getCookies())) {
//                Stream.of(cookies).filter(c->c.getName().equals(Constants.SESSION)).findFirst().ifPresent(c->{
//                    request.getSession().setAttribute(Constants.OAUTH2_ACCESS_TOKEN,oAuth2AccessToken);
//                    String key = Constants.OAUTH2_ACCESS_TOKEN + Constants.COLON + c.getValue();
//                    String value = oAuth2AccessToken.getValue();
//                    LOGGER.debug(String.format("store to redis --> key:%s, value:%s",key, value));
//                    tokenRedisTemplate.opsForValue().set(key, value);
//                });
//            }
        } catch (HttpRequestMethodNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public OAuth2AccessToken obtainOauth2Token(HttpServletRequest request) throws HttpRequestMethodNotSupportedException {
        String username = request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY);
        String password = request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY);
        Validate.notEmpty(username, "username can't be empty!");
        Validate.notEmpty(password,"password can't be empty!");
        PreAuthenticatedAuthenticationToken principal = new PreAuthenticatedAuthenticationToken("web_app",null);
        principal.setAuthenticated(true);
        Map<String, String> parameters = new HashMap<>();
        parameters.put(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY,username);
        parameters.put(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY,password);
        parameters.put(OAuth2Utils.GRANT_TYPE,"password");
        ResponseEntity<OAuth2AccessToken> oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters);
        Validate.notNull(oAuth2AccessToken,"oAuth2AccessToken can't be null!");
        return oAuth2AccessToken.getBody();
    }
}