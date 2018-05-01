package com.troy.keeper.monomer.demo.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.monomer.demo.domain.Student;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.io.Serializable;
import java.util.List;

/**
 * Created by yg on 2017/6/8.
 */
public interface StudentRepositiry extends BaseRepository<Student, Serializable> {

    @Query("select stu from Student stu")
    @QueryHints({@QueryHint(name="org.hibernate.cacheable", value = "true")})
    public List<Student> findAllCached();

    @Query("select stu from Student stu where stu.id = ?1")
    public Student findStuById(Long id);
}
