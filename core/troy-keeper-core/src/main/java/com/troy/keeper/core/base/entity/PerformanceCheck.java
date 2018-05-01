package com.troy.keeper.core.base.entity;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.Instant;

/**
 * Created by Harry on 2017/11/17.
 */
@Entity
public class PerformanceCheck extends BaseEntity{

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private Instant createdDate = Instant.now();

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "class", nullable = false)
    private String clazz;

    @Column(name = "method", nullable = false)
    private String method;

    @Column(name = "arguments")
    private String arguments;

    @Column(name = "millisecond", nullable = false)
    private long millisecond;

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getClazz() {
        return clazz;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public long getMillisecond() {
        return millisecond;
    }

    public void setMillisecond(long millisecond) {
        this.millisecond = millisecond;
    }
}
