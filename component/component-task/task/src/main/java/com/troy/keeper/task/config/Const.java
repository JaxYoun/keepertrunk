package com.troy.keeper.task.config;

/**
 * Created by yg on 2017/6/23.
 */
public class Const {

    private Const(){

    }

    public static final String START_FLAG_VALUE_0="启动失败";
    public static final String START_FLAG_VALUE_1="启动成功";
    public static final String FINISH_FLAG_VALUE_0="失败完成";
    public static final String FINISH_FLAG_VALUE_1="成功完成";
    public static final String MSG_CODE_400_ID = "请求参数id错误";
    public static final String MSG_CODE_400_JOBID = "请求参数jobId错误";
    public static final String MSG_CODE_400_PAGE = "请求参数page错误";
    public static final String MSG_CODE_400_JOBNAME = "请求参数jobName错误";
    public static final String MSG_CODE_400_JOBGROUP = "请求参数jobGroup错误";
    public static final String MSG_CODE_400_JOBSTATUS = "请求参数jobStatus错误";
    public static final String MSG_CODE_400_CRON = "请求参数cronExpression错误";
    public static final String MSG_CODE_400_DESCRI = "请求参数description错误";
    public static final String MSG_CODE_400_ISCON = "请求参数isConcurrent错误";
    public static final String MSG_CODE_400_METHODNAME = "请求参数methodName错误";
    public static final String MSG_CODE_400_SPRINGID_OR_BEANCLASS = "请求参数springId、beanClass错误";
    public static final String MSG_CODE_400_ID_2 = "id不存在";
    public static final String MSG_CODE_400_CRON_2 = "cron表达式有误，不能被解析";
    public static final String MSG_CODE_400_CRON_3 = "cronExpression更新失败";
    public static final String MSG_CODE_400_METHODNAME_2 = "methodName错误,未找到目标方法";
    public static final String MSG_CODE_400_SPRINGID_OR_BEANCLASS_2 = "springId、beanClass错误,未找到目标类";
    public static final String MSG_CODE_400_JOBNAME_AND_JOBGROUP = "jobName和jobGroup的组合已经存在";
    public static final String MSG_CODE_400_TASKLIST = "获取任务列表失败";
    public static final String MSG_CODE_400_PAUSE = "暂停任务失败";
    public static final String MSG_CODE_400_RESUME = "恢复任务失败";
    public static final String MSG_CODE_400_STOP = "停止任务失败";
    public static final String MSG_CODE_400_START = "启动任务失败";
    public static final String MSG_CODE_400_START_JOBSTATUS = "启动任务失败,任务状态错误";
}
