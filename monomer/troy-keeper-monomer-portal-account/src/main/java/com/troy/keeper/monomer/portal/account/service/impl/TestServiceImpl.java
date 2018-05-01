package com.troy.keeper.monomer.portal.account.service.impl;

import com.troy.keeper.core.base.repository.CommonRepository;
import com.troy.keeper.core.base.service.BaseServiceImpl;
import com.troy.keeper.monomer.portal.account.domain.Person;
import com.troy.keeper.monomer.portal.account.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yg on 2017/7/10.
 */
@Service
@Transactional
public class TestServiceImpl extends BaseServiceImpl implements TestService {

    @Autowired
    private CommonRepository commonRepository;

    @Override
    public void save(String json, String className) throws Exception {
//        System.out.println("-----------------------");
//        System.out.println(json);
//        System.out.println("-----------------------");
//        Class cla = Class.forName(className);
//        Object obj = JsonUtils.toObject(json, cla);
//        commonRepository.add(obj);

//        Person p1 = new Person();
//        p1.setName("zhangsan");
//        Person p2 = new Person();
//        p2.setName("zhangsan");
//        Person p3 = new Person();
//        p3.setName("zhangsan");
//        List<Person> list = new ArrayList<>();
//        list.add(p1);
//        list.add(p2);
//        list.add(p3);
//        commonRepository.add(list);

//        Long id = 13L;
//        Person p = (Person) commonRepository.findOne(id, Person.class);
//        System.out.println("id:" + p.getId());
//        System.out.println("name:" + p.getName());
//        p.setName("lisi" + new Date().getTime());
//        commonRepository.update(p);
//        System.out.println("new id:" + p.getId());
//        System.out.println("new name:" + p.getName());

//        Person p1 = (Person) commonRepository.findOne(13L, Person.class);
//        Person p2 = (Person) commonRepository.findOne(14L, Person.class);
//        Person p3 = (Person) commonRepository.findOne(15L, Person.class);
//        p1.setName("lisi" + new Date().getTime());
//        p2.setName("lisi" + new Date().getTime());
//        p3.setName("lisi" + new Date().getTime());
//        List<Person> list = new ArrayList<>();
//        list.add(p1);
//        list.add(p2);
//        list.add(p3);
//        commonRepository.update(list);

//        Person p = (Person) commonRepository.findOne(23L, Person.class);
//        commonRepository.delete(p);
//        commonRepository.delete(22L, Person.class);

//        Person p1 = (Person) commonRepository.findOne(19L, Person.class);
//        Person p2 = (Person) commonRepository.findOne(20L, Person.class);
//        Person p3 = (Person) commonRepository.findOne(21L, Person.class);
//        List<Person> list = new ArrayList<>();
//        list.add(p1);
//        list.add(p2);
//        list.add(p3);
//        commonRepository.deleteInBatch(list);

//        List<Person> list = commonRepository.findAll(Person.class);
//        Iterator<Person> it = list.iterator();
//        while(it.hasNext()){
//            Person p = it.next();
//            System.out.println("id:" + p.getId() + "    " + "name:" + p.getName());
//        }

        Person p1 = new Person();
        p1.setName("zhangsan");
        Person p2 = new Person();
        p2.setName("zhangsan");
        Person p3 = new Person();
        p3.setName("zhangsan");
        Person p4 = new Person();
        p4.setName("zhangsan");
        Person p5 = new Person();
        p5.setName("zhangsan");
        List<Person> list = new ArrayList<>();
        list.add(p1);
        list.add(p2);
        list.add(p3);
        list.add(p4);
        list.add(p5);
        commonRepository.customAddBatch(list, 2);
    }
}
