package com.troy.keeper.monomer.demo.domain;

import com.troy.keeper.core.base.entity.BaseAuditingEntity;
import com.troy.keeper.monomer.demo.test.LogRecord;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * Created by yjm on 2017/6/8.
 */
@Entity
@Table(name = "history_log")
public class HistoryLog extends BaseAuditingEntity {

    //描述
    @Column(name = "context")
    private String context;

    @Column(name = "tableName")
    private String tableName;

    @Enumerated
    @Column(name = "TYPE")
    private LogType type;


    public LogType getType() {
        return type;
    }

    public void setType(LogType type) {
        this.type = type;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
