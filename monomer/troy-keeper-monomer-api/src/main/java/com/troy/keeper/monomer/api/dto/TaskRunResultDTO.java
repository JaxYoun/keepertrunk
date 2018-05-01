package com.troy.keeper.monomer.api.dto;

/**
 * Description: 任务运行结果 <br/>
 * Date:     2017/4/12 17:24<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public class TaskRunResultDTO {
    /**
     * 运行ID
     */
    private String runId = null;
    /**
     * 任务ID
     */
    private String taskId = null;
    /**
     * 任务名称
     */
    private String taskName = null;
    /**
     * 任务类型
     */
    private String taskType = null;
    /**
     * 任务上次运行结果
     */
    private String taskRunResult = null;
    /**
     * 任务上次运行开始时间
     */
    private String taskLastStartTime = null;
    /**
     * 任务上次运行结束时间
     */
    private String taskLastEndTime = null;
    /**
     * 任务上次运行耗时
     */
    private String taskLastDuration = null;
    /**
     * 命名空间
     */
    private String nameSpace = null;

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
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

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskRunResult() {
        return taskRunResult;
    }

    public void setTaskRunResult(String taskRunResult) {
        this.taskRunResult = taskRunResult;
    }

    public String getTaskLastStartTime() {
        return taskLastStartTime;
    }

    public void setTaskLastStartTime(String taskLastStartTime) {
        this.taskLastStartTime = taskLastStartTime;
    }

    public String getTaskLastEndTime() {
        return taskLastEndTime;
    }

    public void setTaskLastEndTime(String taskLastEndTime) {
        this.taskLastEndTime = taskLastEndTime;
    }

    public String getTaskLastDuration() {
        return taskLastDuration;
    }

    public void setTaskLastDuration(String taskLastDuration) {
        this.taskLastDuration = taskLastDuration;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }
}
