package com.troy.keeper.core.base.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by yg on 2017/10/9.
 */
@Component
public class TroyRestTemplate {
    @Autowired
    private RestTemplate restTemplate;

    /**
     * get方法访问(有参)
     * @param url
     * @param responseType
     * @param uriVariables
     * @param <T>
     * @return
     * @throws RestClientException
     */
    public <T> T getForObject(String url, Class<T> responseType, Object... uriVariables) throws RestClientException {
        return restTemplate.getForObject(url, responseType, uriVariables);
    }

    /**
     * get方法访问(无参)
     * @param url
     * @param responseType
     * @param <T>
     * @return
     * @throws RestClientException
     */
    public <T> T getForObject(URI url, Class<T> responseType) throws RestClientException {
        return restTemplate.getForObject(url, responseType);
    }

    /**
     * post方法访问(有参)
     * @param url
     * @param request
     * @param responseType
     * @param uriVariables
     * @param <T>
     * @return
     * @throws RestClientException
     */
    public <T> T postForObject(String url, Object request, Class<T> responseType, Object... uriVariables) throws RestClientException{
        return restTemplate.postForObject(url, request, responseType, uriVariables);
    }

    /**
     * post方法访问(无参)
     * @param url
     * @param request
     * @param responseType
     * @param <T>
     * @return
     * @throws RestClientException
     */
    public <T> T postForObject(String url, Object request, Class<T> responseType) throws RestClientException{
        return restTemplate.postForObject(url, request, responseType);
    }

    /**
     * exchange访问
     * @param url
     * @param method    HttpMethod.GET or HttpMethod.POST
     * @param requestEntity
     * @param responseType
     * @param uriVariables
     * @param <T>
     * @return
     * @throws RestClientException
     */
    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) throws RestClientException {
        return restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
    }
}
