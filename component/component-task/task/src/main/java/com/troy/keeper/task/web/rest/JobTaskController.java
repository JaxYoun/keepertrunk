package com.troy.keeper.task.web.rest;

import com.troy.keeper.core.base.dto.BaseDTO;
import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.base.rest.BaseResource;
import com.troy.keeper.core.error.KeeperException;
import com.troy.keeper.core.utils.MapperParam;
import com.troy.keeper.core.utils.MapperUtils;
import com.troy.keeper.core.utils.SpringUtils;
import com.troy.keeper.task.config.Const;
import com.troy.keeper.task.domain.ScheduleJob;
import com.troy.keeper.task.dto.JobLogDTO;
import com.troy.keeper.task.service.JobLogService;
import com.troy.keeper.task.service.TaskService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by yg on 2017/6/23.
 */
@RestController
public class JobTaskController extends BaseResource<BaseDTO> {
	// 日志记录器
	public static final Logger log = Logger.getLogger(JobTaskController.class);
	@Autowired
	private TaskService taskService;
	@Autowired
	private JobLogService jobLogService;
	@Autowired
	private MapperUtils mapperUtils;

	@RequestMapping(value = "/api/task/taskList", method = RequestMethod.POST)
	public ResponseDTO taskList() {
		List<Map<String, Object>> result = null;
		try {
			MapperParam mapperParam = new MapperParam();
			mapperParam.setList(taskService.getAllTask());
			result = mapperUtils.convertList(mapperParam);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new KeeperException(e);
		}
		return success(result);
	}

	@RequestMapping(value = "/api/task/add", method = RequestMethod.POST)
	public ResponseDTO add(@RequestBody ScheduleJob scheduleJob) {
		ResponseDTO x = paramErrorReturn(scheduleJob);
		if (x != null) {
			return x;
		}

		try {
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
			log.info("cron表达式:" + scheduleBuilder + "验证通过!");
		} catch (Exception e) {
			log.error(e.getMessage());
			return fail(Const.MSG_CODE_400_CRON_2);
		}
		Object obj = null;
		try {
			obj = getObject(scheduleJob.getSpringId(), scheduleJob.getBeanClass());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		if (obj == null) {
			return fail(Const.MSG_CODE_400_SPRINGID_OR_BEANCLASS_2);
		} else {
			if (checkMethod(scheduleJob.getMethodName(), obj)) {
				return fail(Const.MSG_CODE_400_METHODNAME_2);
			}
		}
		try {
			taskService.addTask(scheduleJob);
		} catch (SchedulerException e) {
			log.error(e.getMessage());
			return fail(Const.MSG_CODE_400_JOBNAME_AND_JOBGROUP);
		}
		return success();
	}

	private boolean checkMethod(String methodName, Object obj) {
		Class clazz = obj.getClass();
		Method method = null;
		try {
            method = clazz.getMethod(methodName, null);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
		return (method == null);
	}

	private Object getObject(String springId, String beanClass) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Object obj = null;
		if (StringUtils.isNotBlank(springId)) {
            obj = SpringUtils.getBean(springId);
        }
        else {
            Class clazz = Class.forName(beanClass);
            obj = clazz.newInstance();
        }
		return obj;
	}

	private ResponseDTO paramErrorReturn(ScheduleJob scheduleJob) {
		String jobName = scheduleJob.getJobName();
		String jobGroup = scheduleJob.getJobGroup();
		String jobStatus = scheduleJob.getJobStatus();
		String cronExpression = scheduleJob.getCronExpression();
		String description = scheduleJob.getDescription();
		String isConcurrent = scheduleJob.getIsConcurrent();
		String methodName = scheduleJob.getMethodName();
		String springId = scheduleJob.getSpringId();
		String beanClass = scheduleJob.getBeanClass();
		if(StringUtils.isEmpty(jobName)){
			return fail(Const.MSG_CODE_400_JOBNAME);
		}
		if(StringUtils.isEmpty(jobGroup)){
			return fail(Const.MSG_CODE_400_JOBGROUP);
		}
		if(StringUtils.isEmpty(jobStatus) || (!ScheduleJob.STATUS_NOT_RUNNING.equals(jobStatus) && !ScheduleJob.STATUS_RUNNING.equals(jobStatus))){
			return fail(Const.MSG_CODE_400_JOBSTATUS);
		}
		if(StringUtils.isEmpty(cronExpression)){
			return fail(Const.MSG_CODE_400_CRON);
		}
		if(StringUtils.isEmpty(description)){
			return fail(Const.MSG_CODE_400_DESCRI);
		}
		if(StringUtils.isEmpty(isConcurrent) || (!ScheduleJob.CONCURRENT_NOT.equals(isConcurrent) && !ScheduleJob.CONCURRENT_IS.equals(isConcurrent))){
			return fail(Const.MSG_CODE_400_ISCON);
		}
		if(StringUtils.isEmpty(methodName)){
			return fail(Const.MSG_CODE_400_METHODNAME);
		}
		if(StringUtils.isEmpty(springId) && isBeanClassNull(beanClass)){
			return fail(Const.MSG_CODE_400_SPRINGID_OR_BEANCLASS);
		}
		return null;
	}

	private boolean isBeanClassNull(String beanClass){
		return (beanClass == null || "".equals(beanClass));
	}

	@RequestMapping(value = "/api/task/delete", method = RequestMethod.POST)
	public ResponseDTO delete(@RequestBody ScheduleJob scheduleJob) {
		Long id = scheduleJob.getId();
		if(id == null){
			return fail(Const.MSG_CODE_400_ID);
		}
		try {
			taskService.deleteTask(id);
		} catch (Exception e) {
			log.error(e.getMessage());
			return fail(Const.MSG_CODE_400_ID_2);
		}
		return success();
	}

	@RequestMapping(value = "/api/task/listIndb", method = RequestMethod.POST)
	public ResponseDTO listIndb() {
		List<Map<String, Object>> result = null;
		try {
			MapperParam mapperParam = new MapperParam();
			mapperParam.setList(taskService.getAllTask());
			result = mapperUtils.convertList(mapperParam);
		} catch (Exception e) {
			log.error(e.getMessage());
			return fail(Const.MSG_CODE_400_TASKLIST);
		}
		return success(result);
	}

	@RequestMapping(value = "/api/task/list", method = RequestMethod.POST)
	public ResponseDTO list() {
		List<Map<String, Object>> result = null;
		try {
			MapperParam mapperParam = new MapperParam();
			mapperParam.setList(taskService.getAllJob());
			result = mapperUtils.convertList(mapperParam);
		} catch (Exception e) {
			log.error(e.getMessage());
			return fail(Const.MSG_CODE_400_TASKLIST);
		}
		return success(result);
	}

	@RequestMapping(value = "/api/task/runningList", method = RequestMethod.POST)
	public ResponseDTO runningList() {
		List<Map<String, Object>> result = null;
		try {
			MapperParam mapperParam = new MapperParam();
			mapperParam.setList(taskService.getRunningJob());
			result = mapperUtils.convertList(mapperParam);
		} catch (Exception e) {
			log.error(e.getMessage());
			return fail(Const.MSG_CODE_400_TASKLIST);
		}
		return success(result);
	}

	@RequestMapping(value = "/api/task/pause", method = RequestMethod.POST)
	public ResponseDTO pause(@RequestBody ScheduleJob scheduleJob) {
		Long id = scheduleJob.getId();
		if(id == null){
			return fail(Const.MSG_CODE_400_ID);
		}
		ScheduleJob sj = taskService.getTaskById(id);
		try {
			taskService.pauseJob(sj);
		} catch (SchedulerException e) {
			log.error(e.getMessage());
			return fail(Const.MSG_CODE_400_PAUSE);
		}
		return success();
	}

	@RequestMapping(value = "/api/task/resume", method = RequestMethod.POST)
	public ResponseDTO resume(@RequestBody ScheduleJob scheduleJob) {
		Long id = scheduleJob.getId();
		if(id == null){
			return fail(Const.MSG_CODE_400_ID);
		}
		ScheduleJob sj = taskService.getTaskById(id);
		try {
			taskService.resumeJob(sj);
		} catch (SchedulerException e) {
			log.error(e.getMessage());
			return fail(Const.MSG_CODE_400_RESUME);
		}
		return success();
	}

	@RequestMapping(value = "/api/task/stop", method = RequestMethod.POST)
	public ResponseDTO stop(@RequestBody ScheduleJob scheduleJob) {
		Long id = scheduleJob.getId();
		if(id == null){
			return fail(Const.MSG_CODE_400_ID);
		}
		ScheduleJob sj = taskService.getTaskById(id);
		try {
			taskService.deleteJob(sj);
		} catch (SchedulerException e) {
			log.error(e.getMessage());
			return fail(Const.MSG_CODE_400_STOP);
		}
		return success();
	}

	@RequestMapping(value = "/api/task/start", method = RequestMethod.POST)
	public ResponseDTO start(@RequestBody ScheduleJob scheduleJob) {
		Long id = scheduleJob.getId();
		if(id == null){
			return fail(Const.MSG_CODE_400_ID);
		}
		ScheduleJob sj = taskService.getTaskById(id);
		String jobStatus = sj.getJobStatus();
		if (!ScheduleJob.STATUS_RUNNING.equals(jobStatus)) {
			return fail(Const.MSG_CODE_400_START_JOBSTATUS);
		}
		try {
			taskService.addJob(sj);
		} catch (SchedulerException e) {
			log.error(e.getMessage());
			return fail(Const.MSG_CODE_400_START);
		}
		return success();
	}

	@RequestMapping(value = "/api/task/runJobNow", method = RequestMethod.POST)
	public ResponseDTO runJobNow(@RequestBody ScheduleJob scheduleJob) {
		Long id = scheduleJob.getId();
		if(id == null){
			return fail(Const.MSG_CODE_400_ID);
		}
		ScheduleJob sj = taskService.getTaskById(id);
		try {
			taskService.runAJobNow(sj);
		} catch (SchedulerException e) {
			log.error(e.getMessage());
			return fail(Const.MSG_CODE_400_START);
		}
		return success();
	}

	@RequestMapping(value = "/api/task/updateCron", method = RequestMethod.POST)
	public ResponseDTO updateCron(@RequestBody ScheduleJob scheduleJob) {
		Long id = scheduleJob.getId();
		String cronExpression = scheduleJob.getCronExpression();
		if(id == null){
			return fail(Const.MSG_CODE_400_ID);
		}
		if(StringUtils.isEmpty(cronExpression)){
			return fail(Const.MSG_CODE_400_CRON);
		}
		try {
			//验证传入的cronExpression是否正确
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
			log.info("cron表达式:" + scheduleBuilder + "验证通过!");
		} catch (Exception e) {
			log.error(e.getMessage());
			return fail(Const.MSG_CODE_400_CRON_2);
		}
		try {
			taskService.updateCron(id, cronExpression);
		} catch (SchedulerException e) {
			log.error(e.getMessage());
			return fail(Const.MSG_CODE_400_CRON_3);
		}
		return success();
	}

	@RequestMapping(value = "/api/task/updateJobStatus", method = RequestMethod.POST)
	public ResponseDTO updateJobStatus(@RequestBody ScheduleJob scheduleJob) {
		Long id = scheduleJob.getId();
		String jobStatus = scheduleJob.getJobStatus();
		if(id == null){
			return fail(Const.MSG_CODE_400_ID);
		}
		if(StringUtils.isEmpty(jobStatus) || (!ScheduleJob.STATUS_NOT_RUNNING.equals(jobStatus)) && !ScheduleJob.STATUS_RUNNING.equals(jobStatus)){
			return fail(Const.MSG_CODE_400_JOBSTATUS);
		}
		try {
			taskService.updateJobStatus(id, jobStatus);
		} catch (SchedulerException e) {
			log.error(e.getMessage());
			return fail(Const.MSG_CODE_400_CRON_3);
		}
		return success();
	}

	@RequestMapping(value = "/api/task/findById", method = RequestMethod.POST)
	public ResponseDTO findById(@RequestBody ScheduleJob scheduleJob) {
		Long id = scheduleJob.getId();
		if(id == null){
			return fail(Const.MSG_CODE_400_ID);
		}
		Map<String, Object> result = null;
		try {
			MapperParam mapperParam = new MapperParam();
			mapperParam.setObj(taskService.getTaskById(id));
			result = mapperUtils.convert(mapperParam);
		} catch (Exception e) {
			log.error(e.getMessage());
			return fail(Const.MSG_CODE_400_ID_2);
		}
		return success(result);
	}

	@RequestMapping(value = "/api/task/queryJobLog", method = RequestMethod.POST)
	public ResponseDTO queryJobLog(@RequestBody JobLogDTO jobLogDTO) {
		Long jobId = jobLogDTO.getJobId();
		if(jobId == null){
			return fail(Const.MSG_CODE_400_JOBID);
		}
		Integer page = jobLogDTO.getPage();
		if(page == null){
			return fail(Const.MSG_CODE_400_PAGE);
		}
		Integer size = jobLogDTO.getSize();
		if(size == null){
			size = 20;
		}
		Long startDate = jobLogDTO.getStartDate();
		Long endDate = jobLogDTO.getEndDate();
		Page<Map<String, Object>> result = null;
		try {
			MapperParam mapperParam = new MapperParam();
			mapperParam.setPage(jobLogService.queryJobLogByPage(jobId, startDate, endDate, page, size));
			result = mapperUtils.convertPage(mapperParam);
		} catch (Exception e) {
			log.error(e.getMessage());
			return fail();
		}
		return success(result);
	}
}