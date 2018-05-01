package com.troy.keeper.system.intercept.account.impl;

import com.troy.keeper.core.base.service.BaseServiceImpl;
import com.troy.keeper.core.utils.validate.Validate;
import com.troy.keeper.system.domain.SmUser;
import com.troy.keeper.system.dto.SmUserDTO;
import com.troy.keeper.system.intercept.account.AccountAdviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Harry on 2017/9/6.
 */
public abstract class AbstractAccountAdviceServiceImpl extends BaseServiceImpl<SmUser, SmUserDTO> implements AccountAdviceService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String serviceName;

    public AbstractAccountAdviceServiceImpl(String serviceName) {
        Validate.notNull(serviceName, "serviceName can't be null!");
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractAccountAdviceServiceImpl)) return false;

        AbstractAccountAdviceServiceImpl that = (AbstractAccountAdviceServiceImpl) o;

        return serviceName.equals(that.serviceName);

    }

    @Override
    public int hashCode() {
        return serviceName.hashCode();
    }

    @Override
    public void postChangeUserPassword(SmUserDTO smUserDTO) {

    }

    @Override
    public void preCreatedData(SmUserDTO smUserDTO) {

    }

    @Override
    public void postCreatedData(SmUserDTO smUserDTO) {

    }

    @Override
    public void preUpdateData(SmUserDTO smUserDTO) {

    }

    @Override
    public void postUpdateData(SmUserDTO smUserDTO) {

    }

    @Override
    public void preDel(SmUserDTO smUserDTO) {

    }

    @Override
    public void postDel(SmUserDTO smUserDTO) {

    }

    @Override
    public void preChangeUserPassword(SmUserDTO smUserDTO) {

    }

    @Override
    public void createData(SmUserDTO smUserDTO) {

    }

    @Override
    public void updateData(SmUserDTO smUserDTO) {

    }

    @Override
    public void del(SmUserDTO smUserDTO) {

    }

    @Override
    public void changeUserPassword(SmUserDTO smUserDTO) {

    }
}
