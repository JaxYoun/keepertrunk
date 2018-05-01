package com.troy.uaa.service.impl;

import com.troy.api.dto.TenantDTO;
import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.uaa.service.TenantService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Component
public class TenantServiceHystrix implements TenantService {

    @Override
    public ResponseDTO<TenantDTO> queryTenant(Long id){
        return new ResponseDTO<>("300","tenant调用失败");
    }

    @Override
    public ResponseDTO<Map<Long, String>> getTenantNameIds() {
        return new ResponseDTO<Map<Long, String>>("300","tenant调用失败",new HashMap<>());
    }

    @Override
    public String queryStatusById(@RequestParam("id") Long id) {
        return "0";
    }

}
