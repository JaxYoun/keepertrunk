package com.troy.keeper.core.filter;


import com.troy.keeper.core.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: xiongzhan
 * Date: 17-4-21
 * Time: 下午4:48
 * To change this template use File | Settings | File Templates.
 */
public class MutableHttpServletFilter extends OncePerRequestFilter {

    private final Logger LOGGER = LoggerFactory.getLogger(MutableHttpServletFilter.class);

//    private RedisTemplate<String, String> tokenRedisTemplate;

//    public MutableHttpServletFilter(RedisTemplate<String, String> tokenRedisTemplate) {
//        this.tokenRedisTemplate = tokenRedisTemplate;
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(request);

        OAuth2AccessToken oAuth2AccessToken = (OAuth2AccessToken) request.getSession().getAttribute(Constants.OAUTH2_ACCESS_TOKEN);
        if (null!=oAuth2AccessToken) {
            mutableRequest.putHeader("Authorization", OAuth2AccessToken.BEARER_TYPE+" "+oAuth2AccessToken.getValue());
        }
//        Cookie[] cookies = request.getCookies();
//        final String[] token = new String[1];
//        if (ArrayUtils.isNotEmpty(request.getCookies())) {
//            Stream.of(cookies).filter(c->c.getName().equals(Constants.SESSION)).findFirst().ifPresent(c->{
//                String key = Constants.OAUTH2_ACCESS_TOKEN + Constants.COLON + c.getValue();
//                token[0] = tokenRedisTemplate.opsForValue().get(key);
//                LOGGER.debug(String.format("get from redis --> key:%s, value:%s",key, token[0]));
//                if (StringUtils.isNotEmpty(token[0])) {
//                    mutableRequest.putHeader("Authorization", OAuth2AccessToken.BEARER_TYPE+" "+token);
//                }
//            });
//        }
//        if (StringUtils.isEmpty(token[0])) {
//            OAuth2AccessToken oAuth2AccessToken = (OAuth2AccessToken) request.getSession().getAttribute(Constants.OAUTH2_ACCESS_TOKEN);
//            if (null!=oAuth2AccessToken) {
//                mutableRequest.putHeader("Authorization", OAuth2AccessToken.BEARER_TYPE+" "+oAuth2AccessToken.getValue());
//            }
//        }

        filterChain.doFilter(mutableRequest, response);
    }

    final class MutableHttpServletRequest extends HttpServletRequestWrapper {

        private final Map<String, String> customHeaders;

        public MutableHttpServletRequest(HttpServletRequest request){
            super(request);
            this.customHeaders = new HashMap<>();
        }

        public void putHeader(String name, String value){
            this.customHeaders.put(name, value);
        }

        public String getHeader(String name) {
            String headerValue = customHeaders.get(name);
            if (headerValue != null){
                return headerValue;
            }
            return ((HttpServletRequest) getRequest()).getHeader(name);
        }

        public Enumeration<String> getHeaders(String name) {
            Set<String> set = new HashSet<>(4);
            if (customHeaders.containsKey(name)) {
                set.add(customHeaders.get(name));
            }
            Enumeration<String> headers = super.getHeaders(name);
            while (headers.hasMoreElements()) {
                // add the names of the request headers into the list
                String n = headers.nextElement();
                set.add(n);
            }
            return Collections.enumeration(set);
        }
        public Enumeration<String> getHeaderNames() {
            // create a set of the custom header names
            Set<String> set = new HashSet<String>(customHeaders.keySet());

            // now add the headers from the wrapped request object
            @SuppressWarnings("unchecked")
            Enumeration<String> e = ((HttpServletRequest) getRequest()).getHeaderNames();
            while (e.hasMoreElements()) {
                // add the names of the request headers into the list
                String n = e.nextElement();
                set.add(n);
            }

            // create an enumeration from the set and return
            return Collections.enumeration(set);
        }
    }
}
