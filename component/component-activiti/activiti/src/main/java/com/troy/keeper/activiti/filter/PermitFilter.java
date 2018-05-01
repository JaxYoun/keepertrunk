package com.troy.keeper.activiti.filter;

import com.troy.keeper.core.base.service.RedisService;
import com.troy.keeper.util.AddressUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.io.IOException;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * description:请求验证Filter <br/>
 * Date:     2017/6/30 11:51<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public class PermitFilter implements Filter {
    private static final String ACTIVITI_PERMIT_ADDRESS = "activiti.permit_all_address.address";

    @Autowired
    private RedisService redisService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                filterConfig.getServletContext());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String ipAddress = AddressUtils.getIpAddress((HttpServletRequest)servletRequest);
        Map permitMap = redisService.hMGet(ACTIVITI_PERMIT_ADDRESS);
        if(!CollectionUtils.isEmpty(permitMap.values()) && !StringUtils.isEmpty(ipAddress)){
            for (Object permitAddr : permitMap.values()) {
                if(!StringUtils.isEmpty(permitAddr) && ipAddress.matches(String.valueOf(permitAddr))){
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                }
            }
        }
    }

    @Override
    public void destroy() {

    }
}
