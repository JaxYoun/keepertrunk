package com.troy.keeper.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: xiongzhan
 * Date: 17-4-24
 * Time: 下午2:41
 * To change this template use File | Settings | File Templates.
 */
public class WebUtils {

    private static final String XML_HTTP_REQUEST = "XMLHttpRequest";
    private static final String X_REQUESTED_WITH = "X-Requested-With";


    public static boolean isAjax(HttpServletRequest request) {
        return XML_HTTP_REQUEST.equals(request.getHeader(X_REQUESTED_WITH));
    }


}
