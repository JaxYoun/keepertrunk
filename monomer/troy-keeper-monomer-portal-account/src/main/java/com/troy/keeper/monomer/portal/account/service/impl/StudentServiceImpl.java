package com.troy.keeper.monomer.portal.account.service.impl;

import com.troy.keeper.core.base.dto.BaseDTO;
import com.troy.keeper.core.base.repository.CommonRepository;
import com.troy.keeper.core.base.service.BaseServiceImpl;
import com.troy.keeper.monomer.portal.account.domain.Person;
import com.troy.keeper.monomer.portal.account.domain.Student;
import com.troy.keeper.monomer.portal.account.repository.StudentRepositiry;
import com.troy.keeper.monomer.portal.account.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yg on 2017/6/8.
 */
@Service
public class StudentServiceImpl extends BaseServiceImpl<Student, BaseDTO> implements StudentService {

    @Autowired
    private StudentRepositiry studentRepositiry;
    @Autowired
    private CommonRepository commonRepository;

    private Specification<Person> getPersonSpecification() {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            list.add(criteriaBuilder.greaterThanOrEqualTo(root.get("id"), 16));
            list.add(criteriaBuilder.lessThanOrEqualTo(root.get("id"), 18));
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        };
    }

    private Sort getOrders() {
        return new Sort(Sort.Direction.DESC, "id");
    }

    private Pageable getPageable(){
        Sort sort = getOrders();
        int page = 0;
        int size = 1;
        Pageable pageable = new PageRequest(page, size, sort);
        return pageable;
    }

    @Override
    @Cacheable(value="stu1", key="#id + 'find'")
    public Student find(Long id) {
        return studentRepositiry.findStuById(id);
    }

    @Override
    @CacheEvict(value="stu1", allEntries=true)
    public void delete(){
        System.out.println("delete........cache");
    }

    @Override
    @CachePut(value="stu1", key="#id + 'find'")
    public Student update(Long id){
        Student student = new Student();
        student.setId(new Long(2));
        student.setName("lisi");
        return student;
    }

    @Override
    public List<Student> findAll() {
        return studentRepositiry.findAllCached();
    }

    @Override
    public Person findOneByHql() {
        String hql = "from Person p where p.id = (select max(a.id) from Person a)";
        return (Person)commonRepository.findOneByHql(hql, null);
    }

    @Override
    public List<Person> findListByHql() {
        String hql = "from Person p where p.name = 'zhangsan'";
        return commonRepository.findListByHql(hql, null);
    }

    @Override
    public List<Person> findListBySql() {
        String sql = "select * from person p where p.name = 'zhangsan'";
        return commonRepository.findListBySql(sql, Person.class);
    }

    @Override
    public Object findOneBySql() {
        String sql = "select * from person p where p.id = 13";
        return commonRepository.findOneBySql(sql, Person.class);
    }

    @Override
    public List<Person> findAllBySpec(){
        Specification<Person> spec = getPersonSpecification();
        return commonRepository.findAll(Person.class, spec);
    }

    @Override
    public List<Person> findAllBySpecAndSort(){
        Specification<Person> spec = getPersonSpecification();
        Sort sort = getOrders();
        return commonRepository.findAll(Person.class, spec, sort);
    }

    @Override
    public Page<Person> findAllPage(){
        Pageable pageable = getPageable();
        return commonRepository.findAll(pageable, Person.class);
    }

    @Override
    public Page<Person> findAllPageBySpec(){
        Specification<Person> spec = getPersonSpecification();
        Pageable pageable = getPageable();
        return commonRepository.findAll(pageable, Person.class, spec);
    }

    @Override
    public Long findMaxId(){
        String hql = "select max(a.id) as id from Person a";
        return (Long)commonRepository.findOneByHql(hql, null);
    }
}
