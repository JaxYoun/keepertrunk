package com.troy.keeper.monomer.demo.web.rest;

import com.troy.keeper.core.base.dto.BaseDTO;
import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.base.rest.BaseResource;
import com.troy.keeper.monomer.demo.service.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by yg on 2017/4/7.
 */
@RestController
public class LoginLogResource extends BaseResource<BaseDTO> {
    @Autowired
    private LoginLogService loginLogService;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    static {
        sdf.setLenient(false);
    }

    @RequestMapping(value = "/api/logs/queryLoginLogsPage", method = RequestMethod.POST)
    public ResponseDTO<Page<Map<String, Object>>> queryLoginLogsPage(String startDate, String endDate, Pageable pageable){
        try {
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);
            Page<Map<String, Object>> returnPage = loginLogService.queryLoginLogsPage(start, end, pageable);
            return getResponseDTO("200", "操作成功", returnPage);
        }catch (Exception e){
            e.printStackTrace();
            return getResponseDTO("201", "系统内部错误", null);
        }
    }
}
