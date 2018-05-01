package com.troy.keeper.task.service.impl;

import com.troy.keeper.core.base.dto.BaseDTO;
import com.troy.keeper.core.base.service.BaseServiceImpl;
import com.troy.keeper.task.QuartzJobFactory;
import com.troy.keeper.task.QuartzJobFactoryDisallowConcurrentExecution;
import com.troy.keeper.task.domain.ScheduleJob;
import com.troy.keeper.task.repository.TaskRepository;
import com.troy.keeper.task.service.TaskService;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by yg on 2017/6/22.
 */
@Service
@Transactional
public class TaskServiceImpl extends BaseServiceImpl<ScheduleJob, BaseDTO> implements TaskService{
    public static final Logger log = Logger.getLogger(TaskServiceImpl.class);
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
    @Autowired
    private TaskRepository taskRepository;

    @PostConstruct
    public void init() throws SchedulerException{
        // 这里获取任务信息数据
        List<ScheduleJob> jobList = getAllTask();
        for (ScheduleJob job : jobList) {
            addJob(job);
        }
    }

    @Override
    public List<ScheduleJob> getAllTask() {
        return taskRepository.getAllTask();
    }

    @Override
    public void addTask(ScheduleJob scheduleJob) throws SchedulerException{
        if(scheduleJob == null){
            return;
        }
        String jobStatus = scheduleJob.getJobStatus();
        if (ScheduleJob.STATUS_RUNNING.equals(jobStatus)) {
            addJob(scheduleJob);
        }
        taskRepository.save(scheduleJob);
    }

    @Override
    public void deleteTask(Long id) throws SchedulerException{
        ScheduleJob scheduleJob = getTaskById(id);
        deleteJob(scheduleJob);
        taskRepository.delete(id);
    }

    @Override
    public ScheduleJob getTaskById(Long id) {
        return taskRepository.findOne(id);
    }

    @Override
    public void updateCron(Long id, String cron) throws SchedulerException{
        ScheduleJob scheduleJob = getTaskById(id);
        if (scheduleJob == null) {
            return;
        }
        scheduleJob.setCronExpression(cron);
        String jobStatus = scheduleJob.getJobStatus();
        if (ScheduleJob.STATUS_RUNNING.equals(jobStatus)) {
            updateJobCron(scheduleJob);
        }
        taskRepository.save(scheduleJob);
    }

    @Override
    public void addJob(ScheduleJob scheduleJob) throws SchedulerException {
        if(scheduleJob == null){
            return;
        }
        String jobStatus = scheduleJob.getJobStatus();
        if (!ScheduleJob.STATUS_RUNNING.equals(jobStatus)) {
            return;
        }
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        log.info(scheduler + ".......................................................................................add");
        String jobName = scheduleJob.getJobName();
        String jobGroup = scheduleJob.getJobGroup();
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        CronTrigger trigger = null;
        trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        String isConcurrent = scheduleJob.getIsConcurrent();
        String cronExpression = scheduleJob.getCronExpression();
        // 不存在，创建一个
        if (null == trigger) {
            Class clazz = ScheduleJob.CONCURRENT_IS.equals(isConcurrent) ? QuartzJobFactory.class : QuartzJobFactoryDisallowConcurrentExecution.class;
            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(jobName, jobGroup).build();
            jobDetail.getJobDataMap().put("scheduleJob", scheduleJob);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup).withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, trigger);
        } else {
            // Trigger已存在，那么更新相应的定时设置
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        }
    }

    @Override
    public List<ScheduleJob> getAllJob() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        List<ScheduleJob> jobList = new ArrayList<>();
        for (JobKey jobKey : jobKeys) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            String jobName = jobKey.getName();
            String jobGroup = jobKey.getGroup();
            for (Trigger trigger : triggers) {
                TriggerKey key = trigger.getKey();
                ScheduleJob scheduleJob = new ScheduleJob();
                scheduleJob.setJobName(jobName);
                scheduleJob.setJobGroup(jobGroup);
                scheduleJob.setDescription("触发器:" + key);
                Trigger.TriggerState triggerState = scheduler.getTriggerState(key);
                String jobStatus = triggerState.name();
                scheduleJob.setJobStatus(jobStatus);
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    scheduleJob.setCronExpression(cronExpression);
                }
                jobList.add(scheduleJob);
            }
        }
        return jobList;
    }

    @Override
    public List<ScheduleJob> getRunningJob() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
        int listSize = executingJobs.size();
        List<ScheduleJob> jobList = new ArrayList<>(listSize);
        for (JobExecutionContext executingJob : executingJobs) {
            ScheduleJob scheduleJob = new ScheduleJob();
            JobDetail jobDetail = executingJob.getJobDetail();
            JobKey jobKey = jobDetail.getKey();
            Trigger trigger = executingJob.getTrigger();
            String jobName = jobKey.getName();
            String jobGroup = jobKey.getGroup();
            TriggerKey key = trigger.getKey();
            scheduleJob.setJobName(jobName);
            scheduleJob.setJobGroup(jobGroup);
            scheduleJob.setDescription("触发器:" + key);
            Trigger.TriggerState triggerState = scheduler.getTriggerState(key);
            String jobStatus = triggerState.name();
            scheduleJob.setJobStatus(jobStatus);
            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                String cronExpression = cronTrigger.getCronExpression();
                scheduleJob.setCronExpression(cronExpression);
            }
            jobList.add(scheduleJob);
        }
        return jobList;
    }

    @Override
    public void pauseJob(ScheduleJob scheduleJob) throws SchedulerException {
        if(scheduleJob == null){
            return;
        }
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        String jobName = scheduleJob.getJobName();
        String jobGroup = scheduleJob.getJobGroup();
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        scheduler.pauseJob(jobKey);
    }

    @Override
    public void resumeJob(ScheduleJob scheduleJob) throws SchedulerException {
        if(scheduleJob == null){
            return;
        }
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        String jobName = scheduleJob.getJobName();
        String jobGroup = scheduleJob.getJobGroup();
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        scheduler.resumeJob(jobKey);
    }

    @Override
    public void deleteJob(ScheduleJob scheduleJob) throws SchedulerException {
        if(scheduleJob == null){
            return;
        }
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        String jobName = scheduleJob.getJobName();
        String jobGroup = scheduleJob.getJobGroup();
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        scheduler.deleteJob(jobKey);
    }

    @Override
    public void runAJobNow(ScheduleJob scheduleJob) throws SchedulerException {
        if(scheduleJob == null){
            return;
        }
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        String jobName = scheduleJob.getJobName();
        String jobGroup = scheduleJob.getJobGroup();
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        scheduler.triggerJob(jobKey);
    }

    @Override
    public void updateJobCron(ScheduleJob scheduleJob) throws SchedulerException {
        if(scheduleJob == null){
            return;
        }
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        String jobName = scheduleJob.getJobName();
        String jobGroup = scheduleJob.getJobGroup();
        String cronExpression = scheduleJob.getCronExpression();
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if(trigger != null){
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            scheduler.rescheduleJob(triggerKey, trigger);
        }
    }

    @Override
    public void updateJobStatus(Long id, String jobStatus) throws SchedulerException {
        ScheduleJob scheduleJob = getTaskById(id);
        if (scheduleJob == null) {
            return;
        }
        scheduleJob.setJobStatus(jobStatus);
        if (ScheduleJob.STATUS_RUNNING.equals(jobStatus)) {
            addJob(scheduleJob);
        }
        else{
            deleteJob(scheduleJob);
        }
        taskRepository.save(scheduleJob);

    }
}