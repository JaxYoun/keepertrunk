package com.troy.keeper.system.domain;

import com.troy.keeper.core.base.entity.BaseAuditingEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by yg on 2017/4/17.
 */
@Entity
@Table(name = "data_dictionary")
public class DataDictionary extends BaseAuditingEntity {
    @Column(name = "dic_code")
    private String dicCode;

    @Column(name = "dic_value")
    private String dicValue;

    @Column(name = "order_code")
    private Integer orderCode;

    @Column(name = "memo")
    private String memo;

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
