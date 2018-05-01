package com.troy.keeper.task;

import com.troy.keeper.core.utils.SpringUtils;
import com.troy.keeper.task.config.Const;
import com.troy.keeper.task.domain.JobLog;
import com.troy.keeper.task.domain.ScheduleJob;
import com.troy.keeper.task.service.JobLogService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;

/**
 * Created by yg on 2017/6/22.
 */
public class TaskUtils {
	public static final Logger log = Logger.getLogger(TaskUtils.class);
	public static final String TASK_NAME = "任务名称";

	private TaskUtils(){

	}

	/**
	 * 通过反射调用scheduleJob中定义的方法
	 * 
	 * @param scheduleJob
	 */
	public static void invokMethod(ScheduleJob scheduleJob) {
		Object object = null;
		Class clazz = null;
		Long id = scheduleJob.getId();
		String springId = scheduleJob.getSpringId();
		String beanClass = scheduleJob.getBeanClass();
		String jobName = scheduleJob.getJobName();
		String methodName = scheduleJob.getMethodName();

		JobLog jobLog = new JobLog();
		jobLog.setJobId(id);

		JobLogService jobLogService = SpringUtils.getBean("jobLogServiceImpl");

		if (StringUtils.isNotBlank(springId)) {
			object = SpringUtils.getBean(springId);
		}
		else if (StringUtils.isNotBlank(beanClass)) {
			try {
				clazz = Class.forName(beanClass);
				object = clazz.newInstance();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		if (object == null) {
			String errorMsg = TASK_NAME + " = [" + jobName + "]---------------未启动成功，请检查是否配置正确！！！";
			saveStartJobErrorLog(jobLog, jobLogService, errorMsg);
			return;
		}
		clazz = object.getClass();
		Method method = null;
		try {
			method = clazz.getDeclaredMethod(methodName);
		} catch (NoSuchMethodException e) {
			String errorMsg = TASK_NAME + " = [" + jobName + "]---------------未启动成功，方法名设置错误！！！";
			saveStartJobErrorLog(jobLog, jobLogService, errorMsg);
			return;
		} catch (SecurityException e) {
			String errorMsg = e.getMessage();
			saveStartJobErrorLog(jobLog, jobLogService, errorMsg);
			return;
		}
		if (method != null) {
			try {
				log.info(TASK_NAME + " = [" + jobName + "]----------启动成功");
				jobLog.setStartFlag(JobLog.START_SUCCESS);
				jobLog.setStartFlagValue(Const.START_FLAG_VALUE_1);
				jobLogService.saveJobLog(jobLog);

				//执行method
				method.invoke(object);

				log.info(TASK_NAME + " = [" + jobName + "]----------执行成功");
				jobLog.setFinishFlag(JobLog.FINISH_SUCCESS);
				jobLog.setFinishFlagValue(Const.FINISH_FLAG_VALUE_1);
				jobLogService.saveJobLog(jobLog);
			} catch (Exception e) {
				String errorMsg = "\"" + TASK_NAME + " = [" + jobName + "]----------执行发生异常!\"" + e.getCause();
				saveJobExeErrorLog(jobLog, jobLogService, errorMsg);
				return;
			}
		}
		else{
			String errorMsg = TASK_NAME + " = [" + jobName + "]---------------未启动成功，方法名设置错误！！！";
			saveStartJobErrorLog(jobLog, jobLogService, errorMsg);
			return;
		}
	}

	private static void saveJobExeErrorLog(JobLog jobLog, JobLogService jobLogService, String errorMsg) {
		log.error(errorMsg);
		jobLog.setFinishFlag(JobLog.FINISH_FAIL);
		jobLog.setFinishFlagValue(Const.FINISH_FLAG_VALUE_0);
		jobLog.setErrorMsg(errorMsg.getBytes());
		jobLogService.saveJobLog(jobLog);
	}

	private static void saveStartJobErrorLog(JobLog jobLog, JobLogService jobLogService, String errorMsg) {
		log.error(errorMsg);
		jobLog.setStartFlag(JobLog.START_FAIL);
		jobLog.setStartFlagValue(Const.START_FLAG_VALUE_0);
		jobLog.setErrorMsg(errorMsg.getBytes());
		jobLogService.saveJobLog(jobLog);
	}
}
