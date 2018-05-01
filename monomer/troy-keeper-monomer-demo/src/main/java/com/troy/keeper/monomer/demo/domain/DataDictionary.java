package com.troy.keeper.monomer.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.troy.keeper.core.base.entity.BaseAuditingEntity;
import com.troy.keeper.monomer.demo.repository.DataDictionaryRepository;
import com.troy.keeper.monomer.demo.test.Comment;
import org.hibernate.annotations.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * Created by yg on 2017/4/17.
 */
@Entity
@Table(name = "data_dictionary")
@Audited
public class DataDictionary extends BaseAuditingEntity {

    @Column(name = "dic_code")
    private String dicCode;


    @Column(name = "dic_value")
    private String dicValue;


    @Column(name = "order_code")
    private Integer orderCode;

    @Column(name = "memo")
    private String memo;


    @OneToMany(mappedBy = "dataDictionary",fetch = FetchType.EAGER)
    @NotAudited
    private List<DateDictionaryChild> dateDictionaryChildList;


    public List<DateDictionaryChild> getDateDictionaryChildList() {
        return dateDictionaryChildList;
    }

    public void setDateDictionaryChildList(List<DateDictionaryChild> dateDictionaryChildList) {
        this.dateDictionaryChildList = dateDictionaryChildList;
    }


//    @Column(name = "datelong",columnDefinition="bigint")
//    @Type(type="com.troy.keeper.core.base.entity.DateLongNew")
//    private Date datelong;
//
//    @Column(name = "date_long_test",columnDefinition="bigint")
//    @Type(type="com.troy.keeper.core.base.entity.DateLongNew")
//    private Date dateLongTest;
//
//    @Column(name = "date_long_update",columnDefinition="bigint")
//    @Type(type="com.troy.keeper.core.base.entity.DateLongNew")
//    private Date dateLongUpdate;
//
//    @Column(name = "instant_long_test",columnDefinition="bigint")
//    @Type(type="com.troy.keeper.core.base.entity.InstantLong")
//    private Instant instantLongTest;

//    public Instant getInstantLongTest() {
//        return instantLongTest;
//    }
//
//    public void setInstantLongTest(Instant instantLongTest) {
//        this.instantLongTest = instantLongTest;
//    }
//
//    public Date getDateLongUpdate() {
//        return dateLongUpdate;
//    }
//
//    public void setDateLongUpdate(Date dateLongUpdate) {
//        this.dateLongUpdate = dateLongUpdate;
//    }
//
//    public Date getDateLongTest() {
//        return dateLongTest;
//    }
//
//    public void setDateLongTest(Date dateLongTest) {
//        this.dateLongTest = dateLongTest;
//    }

//    public Date getDatelong() {
//        return datelong;
//    }
//
//    public void setDatelong(Date datelong) {
//        this.datelong = datelong;
//    }

    public String getDicCode() {
        return dicCode;
    }

    public void setDicCode(String dicCode) {
        this.dicCode = dicCode;
    }

    public String getDicValue() {
        return dicValue;
    }

    public void setDicValue(String dicValue) {
        this.dicValue = dicValue;
    }

    public Integer getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(Integer orderCode) {
        this.orderCode = orderCode;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
