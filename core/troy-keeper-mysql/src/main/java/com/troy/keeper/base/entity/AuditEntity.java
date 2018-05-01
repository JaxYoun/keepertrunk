package com.troy.keeper.base.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

/**
 * Created with IntelliJ IDEA.
 * User: xiongzhan
 * Date: 17-3-29
 * Time: 下午3:04
 * To change this template use File | Settings | File Templates.
 */
@MappedSuperclass
public abstract class AuditEntity extends IdEntity {

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    @JsonIgnore
    @Audited
    private String createdBy;

    @CreatedDate
    @Column(name = "created_date", nullable = false,columnDefinition = "bigint")
    @Type(type="com.troy.keeper.core.base.entity.InstantLong")
    @Audited
    private Instant createdDate = Instant.now();

    @LastModifiedBy
    @Column(name = "last_modified_by")
    @JsonIgnore
    @Audited
    private String lastUpdatedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date",columnDefinition = "bigint")
    @Type(type="com.troy.keeper.core.base.entity.InstantLong")
    @Audited
    private Instant lastUpdatedDate = Instant.now();
    
    @Column(name = "data_status",columnDefinition = "int")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Instant getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Instant lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
}
