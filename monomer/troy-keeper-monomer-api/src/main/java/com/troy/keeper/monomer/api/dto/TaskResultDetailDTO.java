package com.troy.keeper.monomer.api.dto;

/**
 * Description:  任务运行详细信息 <br/>
 * Date:     2017/4/13 14:07<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public class TaskResultDetailDTO {
    /**
     * 任务运行ID
     */
    private String taskRunId = null;
    /**
     * 任务ID
     */
    private String taskId = null;
    /**
     * 任务名称
     */
    private String taskName = null;
    /**
     * 任务上次运行结果
     */
    private String taskRunResult = null;
    /**
     * 任务运行开始时间
     */
    private String taskStartTime = null;
    /**
     * 任务运行结束时间
     */
    private String taskEndTime = null;
    /**
     * 任务运行耗时
     */
    private String taskRunTime = null;
    /**
     * 命名空间
     */
    private String nameSpace = null;

    public String getTaskRunId() {
        return taskRunId;
    }

    public void setTaskRunId(String taskRunId) {
        this.taskRunId = taskRunId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskRunResult() {
        return taskRunResult;
    }

    public void setTaskRunResult(String taskRunResult) {
        this.taskRunResult = taskRunResult;
    }

    public String getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(String taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public String getTaskEndTime() {
        return taskEndTime;
    }

    public void setTaskEndTime(String taskEndTime) {
        this.taskEndTime = taskEndTime;
    }

    public String getTaskRunTime() {
        return taskRunTime;
    }

    public void setTaskRunTime(String taskRunTime) {
        this.taskRunTime = taskRunTime;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }
}
