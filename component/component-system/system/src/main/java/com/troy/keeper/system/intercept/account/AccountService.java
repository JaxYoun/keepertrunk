package com.troy.keeper.system.intercept.account;

import com.troy.keeper.core.base.service.BaseService;
import com.troy.keeper.system.domain.SmUser;
import com.troy.keeper.system.dto.SmUserDTO;

/**
 * Created by Harry on 2017/9/5.
 */
public interface AccountService extends BaseService<SmUser, SmUserDTO> {

    /**
     * 创建用户
     * @param smUserDTO
     */
    void createData(SmUserDTO smUserDTO);

    /**
     * 修改用户
     * @param smUserDTO
     */
    void updateData(SmUserDTO smUserDTO);

    /**
     * 删除用户
     * @param smUserDTO
     */
    void del(SmUserDTO smUserDTO);

    /**
     * 修改密码
     * @param smUserDTO
     */
    void changeUserPassword(SmUserDTO smUserDTO);

}
