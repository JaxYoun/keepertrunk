package com.troy.uaa.service.impl;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.uaa.service.LogsearchService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class LogsearchClientHystrix implements LogsearchService {
    @Override
    public ResponseDTO saveLoginLog(String userId, String sessionId, String ipAddress){
        return new ResponseDTO("300","登录日志调用失败");
    }

    @Override
    public ResponseDTO saveLogoutLog(@RequestParam("sessionId") String sessionId) {
        return new ResponseDTO("300","退出日志调用失败");
    }

}
