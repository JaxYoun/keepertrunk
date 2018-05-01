package com.troy.keeper.activiti.domain;



import com.troy.keeper.base.entity.IdEntity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Date: 2017/6/7 15:42<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
@Entity
@Table(name = "buss_related_process", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"business_id", "business_type"}),
        @UniqueConstraint(columnNames = {"proc_inst_id"})})
public class BussRelatedProcess extends IdEntity {

    @Column(name = "proc_inst_id", nullable = false)
    private String procInstId;

    @Column(name = "business_id", nullable = false)
    private String businessId;

    @Column(name = "business_no")
    private String businessNo;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "business_type", nullable = false)
    private String businessType;

    @Column(name = "created_date", nullable = false)
    private Long createdDate = Long.valueOf((new Date()).getTime());

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }
}
