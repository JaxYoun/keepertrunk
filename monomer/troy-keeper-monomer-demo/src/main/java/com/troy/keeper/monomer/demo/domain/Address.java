package com.troy.keeper.monomer.demo.domain;

/**
 * Created with IntelliJ IDEA.
 * User: Harry
 * Date: 17-7-4
 * Time: 上午11:23
 * To change this template use File | Settings | File Templates.
 */
import javax.persistence.*;

@Entity
public class Address {

    private Long id;

    private Custom custom;

    private String detail;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setCustom(Custom custom) {
        this.custom = custom;
    }

    @ManyToOne
    public Custom getCustom() {
        return custom;
    }

    @Column
    public String getDetail() {
        return detail;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}