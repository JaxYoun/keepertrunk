package com.troy.uaa.security.oauth2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * User: xiongzhan
 * Date: 17-4-21
 * Time: 下午3:20
 * To change this template use File | Settings | File Templates.
 */
public interface Oauth2TokenService {
    void associateOauth2Token(HttpServletRequest request, HttpServletResponse response);
}
