package com.troy.uaa.service;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.client.AuthorizedFeignClient;
import com.troy.uaa.service.impl.LogsearchClientHystrix;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by yjm on 2017/4/25.
 */
@AuthorizedFeignClient(name="capability",fallback = LogsearchClientHystrix.class)
public interface LogsearchService {

    @RequestMapping(method= RequestMethod.POST,value="/api/logs/saveLoginLog")
    public ResponseDTO saveLoginLog(@RequestParam("userId") String userId, @RequestParam("sessionId") String sessionId, @RequestParam("ipAddress") String ipAddress);

    @RequestMapping(value = "/api/logs/saveLogoutLog", method = RequestMethod.POST)
    public ResponseDTO saveLogoutLog(@RequestHeader("sessionId") String sessionId);
}
