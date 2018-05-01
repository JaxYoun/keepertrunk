package com.troy.uaa.service;

import com.troy.api.dto.TenantDTO;
import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.client.AuthorizedFeignClient;
import com.troy.uaa.service.impl.TenantServiceHystrix;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by yjm on 2017/4/25.
 */
@AuthorizedFeignClient(name="capability",fallback = TenantServiceHystrix.class)
public interface TenantService {

    @RequestMapping(method= RequestMethod.POST,value="/api/tenant/queryTenant")
    public ResponseDTO<TenantDTO> queryTenant(@RequestParam("id") Long id);

    @RequestMapping(method= RequestMethod.GET,value="/api/tenant/getTenantNameIds")
    public ResponseDTO<Map<Long,String>> getTenantNameIds();

    @RequestMapping(method= RequestMethod.POST,value="/api/tenant/queryStatusById")
    public String queryStatusById(@RequestParam("id") Long id);
}
