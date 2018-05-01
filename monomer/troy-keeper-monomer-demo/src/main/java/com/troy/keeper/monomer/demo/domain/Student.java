package com.troy.keeper.monomer.demo.domain;



import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by yg on 2017/3/28.
 */
@Entity
@Table(name = "student")
@Cacheable
public class Student implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy="students")
    private Set<Course> courses;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
