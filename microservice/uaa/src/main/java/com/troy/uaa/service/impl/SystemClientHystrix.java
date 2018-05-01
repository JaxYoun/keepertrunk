package com.troy.uaa.service.impl;

import com.troy.api.dto.MenuDTO;
import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.uaa.service.SystemService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yjm on 2017/4/28.
 */
@Service
public class SystemClientHystrix implements SystemService {
    @Override
    public ResponseDTO<List<MenuDTO>> queryMenu(@RequestParam("roleId") Long roleId ,Long tenantId) {
        return new ResponseDTO("201","菜单调用失败",new ArrayList<>());
    }
}
