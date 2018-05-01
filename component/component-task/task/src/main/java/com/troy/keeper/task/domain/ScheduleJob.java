package com.troy.keeper.task.domain;

import com.troy.keeper.core.base.entity.BaseAuditingEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by yg on 2017/6/22.
 */
@Entity
@Table(name = "task_schedule_job")
public class ScheduleJob extends BaseAuditingEntity {
    //状态运行
    public static final String STATUS_RUNNING = "1";
    //状态未运行
    public static final String STATUS_NOT_RUNNING = "0";
    //有状态
    public static final String CONCURRENT_IS = "1";
    //无状态
    public static final String CONCURRENT_NOT = "0";

    /**
     * 任务名称
     */
    @Column(name = "job_name")
    private String jobName;
    /**
     * 任务分组
     */
    @Column(name = "job_group")
    private String jobGroup;
    /**
     * 任务状态 是否启动任务
     */
    @Column(name = "job_status")
    private String jobStatus;
    /**
     * cron表达式
     */
    @Column(name = "cron_expression")
    private String cronExpression;
    /**
     * 描述
     */
    @Column(name = "description")
    private String description;
    /**
     * 任务执行时调用哪个类的方法 包名+类名
     */
    @Column(name = "bean_class")
    private String beanClass;
    /**
     * 任务是否有状态
     */
    @Column(name = "is_concurrent")
    private String isConcurrent;
    /**
     * spring bean
     */
    @Column(name = "spring_id")
    private String springId;
    /**
     * 任务调用的方法名
     */
    @Column(name = "method_name")
    private String methodName;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(String beanClass) {
        this.beanClass = beanClass;
    }

    public String getIsConcurrent() {
        return isConcurrent;
    }

    public void setIsConcurrent(String isConcurrent) {
        this.isConcurrent = isConcurrent;
    }

    public String getSpringId() {
        return springId;
    }

    public void setSpringId(String springId) {
        this.springId = springId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}