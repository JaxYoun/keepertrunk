package com.troy.keeper.system.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.converters.Auto;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.troy.keeper.core.base.service.RedisService;
import com.troy.keeper.core.security.SecurityUtils;
import com.troy.keeper.system.security.SysAccount;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import springfox.documentation.service.ApiListing;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PostUtils {

    @Autowired
    private Environment env;

    @Autowired
    private RedisService redisService;

    //默认不开放机构限制
    private Boolean orgLimit = false;

    private static final ObjectMapper mapper = new ObjectMapper();

    public List<Long> getOrgIds(HttpServletRequest httpServletRequest){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SysAccount sysAccount = (SysAccount)auth.getPrincipal();
        String postJson = redisService.get("post_"+sysAccount.getPostId().toString());
        List<Long> postIds = new ArrayList<>();
        postIds.add(-1l);
        if (StringUtils.isNotEmpty(postJson)){
            try {
                JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, Long.class);
                postIds =  mapper.readValue(postJson, javaType);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return postIds;
    }

    public Boolean getOrgLimit() {
        return orgLimit;
    }

    public void  LoadOrgLimit(){
        String flag = env.getProperty("system.orgLimit");
        if (StringUtils.isNotEmpty(flag) && flag.equals("true")){
            orgLimit = true;
        }
    }
}
