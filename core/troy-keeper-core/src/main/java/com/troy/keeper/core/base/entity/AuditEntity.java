package com.troy.keeper.core.base.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.troy.keeper.base.entity.IdEntity;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

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
    private Long createdBy;

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    @Audited
    private Long createdDate = new Date().getTime();

    @LastModifiedBy
    @Column(name = "last_modified_by")
    @JsonIgnore
    @Audited
    private Long lastUpdatedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    @Audited
    private Long lastUpdatedDate = new Date().getTime();
    
    @Column(name = "data_status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Long getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Long lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
}
