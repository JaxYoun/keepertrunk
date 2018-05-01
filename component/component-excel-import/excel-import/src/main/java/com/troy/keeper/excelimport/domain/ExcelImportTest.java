package com.troy.keeper.excelimport.domain;

import com.troy.keeper.core.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by SimonChu on 2017/7/5.
 */
@Entity
@Table(name = "excel_import_test")
public class ExcelImportTest extends BaseEntity {

    @Column(name = "test_1")
    private String test1;

    @Column(name = "test_2")
    private Long test2;

    @Column(name = "test_3")
    private Integer test3;

    public String getTest1() {
        return test1;
    }

    public void setTest1(String test1) {
        this.test1 = test1;
    }

    public Long getTest2() {
        return test2;
    }

    public void setTest2(Long test2) {
        this.test2 = test2;
    }

    public Integer getTest3() {
        return test3;
    }

    public void setTest3(Integer test3) {
        this.test3 = test3;
    }
}
