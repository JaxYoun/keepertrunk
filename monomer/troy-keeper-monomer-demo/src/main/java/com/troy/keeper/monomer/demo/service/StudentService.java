package com.troy.keeper.monomer.demo.service;


import com.troy.keeper.core.base.dto.BaseDTO;
import com.troy.keeper.core.base.service.BaseService;
import com.troy.keeper.monomer.demo.domain.Person;
import com.troy.keeper.monomer.demo.domain.Student;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by yg on 2017/6/8.
 */
public interface StudentService extends BaseService<Student, BaseDTO> {
    public Student find(Long id);
    public void delete();
    public Student update(Long id);
    public List<Student> findAll();
    public Person findOneByHql();
    public List<Person> findListByHql();
    public List<Person> findListBySql();
    public Object findOneBySql();
    public List<Person> findAllBySpec();
    public List<Person> findAllBySpecAndSort();
    public Page<Person> findAllPage();
    public Page<Person> findAllPageBySpec();
    public Long findMaxId();
}
