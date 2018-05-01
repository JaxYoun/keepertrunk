package com.troy.keeper.system.service;

import com.troy.keeper.core.base.service.BaseService;

/**
 * * * * * * * * * * * * *
 * 系统管理初始化服务接口层
 * * * * * * * * * * * * *
 * Interface Name:SmInitService
 *
 * @author SimonChu By Troy
 * @create 2017-09-15 14:33
 **/
public interface SmInitService extends BaseService {

    /**
     * 检查系统是否存在数据
     *
     * @return
     */
    public boolean checkSystemDataNotNull();

    /**
     * 初始化系统数据
     */
    public void initSystemData();

}
