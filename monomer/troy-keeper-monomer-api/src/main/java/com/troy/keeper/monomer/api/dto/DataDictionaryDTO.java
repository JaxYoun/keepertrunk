package com.troy.keeper.monomer.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.troy.keeper.core.base.dto.BaseDTO;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by yg on 2017/4/17.
 */
public class DataDictionaryDTO extends BaseDTO {

    private Long id;
    private String dicCode;
    private String dicValue;
    private Integer orderCode;
    private String memo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date dateLongTest;

    public Date getDateLongTest() {
        return dateLongTest;
    }

    public void setDateLongTest(Date dateLongTest) {
        this.dateLongTest = dateLongTest;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
