package com.troy.keeper.monomer.demo.service;

import com.troy.keeper.core.base.service.BaseService;
import com.troy.keeper.monomer.demo.domain.Student;

/**
 * Created by yg on 2017/7/10.
 */
public interface TestService extends BaseService {
    public void save(String json, String className) throws Exception;
    public void addStu(Student stu);
    public void updateStu(Student stu);
    public void testTx();
}
