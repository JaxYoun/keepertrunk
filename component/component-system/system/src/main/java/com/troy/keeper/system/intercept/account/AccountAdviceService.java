package com.troy.keeper.system.intercept.account;

import com.troy.keeper.system.dto.SmUserDTO;

/**
 * Created by Harry on 2017/9/6.
 */
public interface AccountAdviceService extends AccountService {

    /**
     * 前置创建用户
     * @param smUserDTO
     */
    void preCreatedData(SmUserDTO smUserDTO);

    /**
     * 后置创建用户
     * @param smUserDTO
     */
    void postCreatedData(SmUserDTO smUserDTO);

    /**
     * 前置修改用户
     * @param smUserDTO
     */
    void preUpdateData(SmUserDTO smUserDTO);

    /**
     * 后置修改用户
     * @param smUserDTO
     */
    void postUpdateData(SmUserDTO smUserDTO);

    /**
     * 前置删除用户
     * @param smUserDTO
     */
    void preDel(SmUserDTO smUserDTO);

    /**
     * 后置删除用户
     * @param smUserDTO
     */
    void postDel(SmUserDTO smUserDTO);

    /**
     * 前置修改密码
     * @param smUserDTO
     */
    void preChangeUserPassword(SmUserDTO smUserDTO);

    /**
     * 后置修改密码
     * @param smUserDTO
     */
    void postChangeUserPassword(SmUserDTO smUserDTO);

}
