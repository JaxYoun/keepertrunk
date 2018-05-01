package com.troy.keeper.monomer.portal.account.service;

import com.troy.keeper.core.base.dto.BaseDTO;
import com.troy.keeper.core.base.service.BaseService;
import com.troy.keeper.monomer.portal.account.domain.LoginLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.Map;

/**
 * Created by yg on 2017/4/7.
 */
public interface LoginLogService extends BaseService<LoginLog, BaseDTO> {
    public Page<Map<String, Object>> queryLoginLogsPage(Date start, Date end, Pageable pageable) throws Exception;
}
