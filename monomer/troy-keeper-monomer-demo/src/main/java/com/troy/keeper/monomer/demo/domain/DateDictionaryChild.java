package com.troy.keeper.monomer.demo.domain;

import com.troy.keeper.core.base.entity.BaseAuditingEntity;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

/**
 * Created by yjm on 2017/5/26.
 */
@Entity
@Audited
@Table(name = "data_dictionary_child")
public class DateDictionaryChild extends BaseAuditingEntity {

    @Column(name = "instant_one", columnDefinition = "bigint")
    @Type(type="com.troy.keeper.core.base.entity.InstantLong")
    private Instant instantOne;

    @Column(name = "instant_two", columnDefinition = "bigint")
    @Type(type="com.troy.keeper.core.base.entity.InstantLong")
    private Instant instantTwo;

    @Column(name = "instant_three", columnDefinition = "bigint")
    @Type(type="com.troy.keeper.core.base.entity.InstantLong")
    private Instant instantWhree;

    @Column(name = "date_one",columnDefinition="bigint")
    @Type(type="com.troy.keeper.core.base.entity.DateLongNew")
    private Date dateOne;

    @Column(name = "date_two",columnDefinition="bigint")
    @Type(type="com.troy.keeper.core.base.entity.DateLongNew")
    private Date dateTwo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_id")
    @NotAudited
    private DataDictionary dataDictionary;

    public DataDictionary getDataDictionary() {
        return dataDictionary;
    }

    public void setDataDictionary(DataDictionary dataDictionary) {
        this.dataDictionary = dataDictionary;
    }

    public Instant getInstantOne() {
        return instantOne;
    }

    public void setInstantOne(Instant instantOne) {
        this.instantOne = instantOne;
    }

    public Instant getInstantTwo() {
        return instantTwo;
    }

    public void setInstantTwo(Instant instantTwo) {
        this.instantTwo = instantTwo;
    }

    public Instant getInstantWhree() {
        return instantWhree;
    }

    public void setInstantWhree(Instant instantWhree) {
        this.instantWhree = instantWhree;
    }

    public Date getDateOne() {
        return dateOne;
    }

    public void setDateOne(Date dateOne) {
        this.dateOne = dateOne;
    }

    public Date getDateTwo() {
        return dateTwo;
    }

    public void setDateTwo(Date dateTwo) {
        this.dateTwo = dateTwo;
    }
}
