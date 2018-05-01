package com.troy.keeper.activiti.client.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


/**
 * Description: http工具类 <br/>
 * Date:     2017/4/6 15:29<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
@Component
public class HttpUtil {

    @Autowired
    private RestTemplate restTemplate;
    /**
     * Main method, used to run the application.
     * 带头部请求
     * @param url url
     * @param headMap httpheader的map对象
     * @throws Exception 抛出异常
     */
    public String doPostByHeadWithBody(String url, Map<String,String> headMap,String body) throws Exception{
        if(url == null){
            throw new RuntimeException("URLBuilder is null");
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        if(headMap != null && headMap.size() > 0){
            for (String in : headMap.keySet()) {
                httpHeaders.add(in,headMap.get(in));
            }
        }
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        httpHeaders.setContentType(type);
        HttpEntity<String> request = new HttpEntity<String>(body, httpHeaders);
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        return  exchange.getBody();
    }

}
