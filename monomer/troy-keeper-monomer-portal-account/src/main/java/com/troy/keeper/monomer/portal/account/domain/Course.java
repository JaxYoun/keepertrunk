package com.troy.keeper.monomer.portal.account.domain;




import javax.persistence.*;
import java.util.Set;

/**
 * Created by yg on 2017/3/28.
 */
@Entity
@Table(name = "course")
@Cacheable
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(name="student_course",joinColumns={@JoinColumn(name="course_id")},
            inverseJoinColumns={@JoinColumn(name="student_id")})
    private Set<Student> students;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public String getName() {
        return name;
    }
}
