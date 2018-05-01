package com.troy.uaa.service;

import com.troy.api.dto.MenuDTO;
import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.client.AuthorizedFeignClient;
import com.troy.uaa.service.impl.SystemClientHystrix;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by yjm on 2017/4/28.
 */
@AuthorizedFeignClient(name="capability",fallback = SystemClientHystrix.class)
public interface SystemService {

    @RequestMapping(method= RequestMethod.POST,value="/api/user/queryMenu")
    public ResponseDTO<List<MenuDTO>> queryMenu(@RequestParam("roleId") Long roleId,@RequestParam("tenantId") Long tenantId);
}
