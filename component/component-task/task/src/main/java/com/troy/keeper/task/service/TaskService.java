package com.troy.keeper.task.service;

import com.troy.keeper.core.base.dto.BaseDTO;
import com.troy.keeper.core.base.service.BaseService;
import com.troy.keeper.task.domain.ScheduleJob;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * Created by yg on 2017/6/22.
 */
public interface TaskService extends BaseService<ScheduleJob, BaseDTO> {

    /**
     * 从数据库中取 区别于getAllJob
     */
    public List<ScheduleJob> getAllTask();


    /**
     * 添加到数据库中 区别于addJob
     * @param  scheduleJob
     */
    public void addTask(ScheduleJob scheduleJob) throws SchedulerException;

    /**
     * 在数据库中删除  区别于deleteJob
     * @param id
     */
    public void deleteTask(Long id) throws SchedulerException;

    /**
     * 从数据库中查询job
     * @param  id
     */
    public ScheduleJob getTaskById(Long id);

    /**
     * 更改任务 cron表达式
     * @param  id
     * @param  cron
     * @throws SchedulerException
     */
    public void updateCron(Long id, String cron) throws SchedulerException;

    /**
     * 添加任务
     * @param  scheduleJob
     * @throws SchedulerException
     */
    public void addJob(ScheduleJob scheduleJob) throws SchedulerException;

    /**
     * 获取所有计划中的任务列表
     * @throws SchedulerException
     */
    public List<ScheduleJob> getAllJob() throws SchedulerException;

    /**
     * 所有正在运行的job
     * @throws SchedulerException
     */
    public List<ScheduleJob> getRunningJob() throws SchedulerException;

    /**
     * 暂停一个job
     * @param  scheduleJob
     * @throws SchedulerException
     */
    public void pauseJob(ScheduleJob scheduleJob) throws SchedulerException;

    /**
     * 恢复一个job
     * @param  scheduleJob
     * @throws SchedulerException
     */
    public void resumeJob(ScheduleJob scheduleJob) throws SchedulerException;

    /**
     * 删除一个job
     * @param  scheduleJob
     * @throws SchedulerException
     */
    public void deleteJob(ScheduleJob scheduleJob) throws SchedulerException;

    /**
     * 立即执行job
     * @param  scheduleJob
     * @throws SchedulerException
     */
    public void runAJobNow(ScheduleJob scheduleJob) throws SchedulerException;

    /**
     * 更新job时间表达式
     * @param  scheduleJob
     * @throws SchedulerException
     */
    public void updateJobCron(ScheduleJob scheduleJob) throws SchedulerException;

    /**
     * 更新任务状态
     * @param  id
     * @param  jobStatus
     * @throws SchedulerException
     */
    public void updateJobStatus(Long id, String jobStatus) throws SchedulerException;
}
