package com.troy.keeper.monomer.api.dto;

/**
 * Description: 已发布任务 <br/>
 * Date: 2017/4/6 14:06<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public class PubTaskDTO {
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
   * 任务状态
   */
  private String taskStatus = null;
  /**
   * 任务运行总次数
   */
  private String taskRunCounts = null;
  /**
   * 任务上次运行开始时间
   */
  private String taskLastStartTime = null;
  /**
   * 任务上次运行结束时间
   */
  private String taskLastEndTime = null;
  /**
   * 任务上次运行结果
   */
  private String taskLastEndStatus = null;
  /**
   * 命名空间
   */
  private String nameSpace = null;

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

  public String getTaskStatus() {
    return taskStatus;
  }

  public void setTaskStatus(String taskStatus) {
    this.taskStatus = taskStatus;
  }

  public String getTaskRunCounts() {
    return taskRunCounts;
  }

  public void setTaskRunCounts(String taskRunCounts) {
    this.taskRunCounts = taskRunCounts;
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

  public String getTaskLastEndStatus() {
    return taskLastEndStatus;
  }

  public void setTaskLastEndStatus(String taskLastEndStatus) {
    this.taskLastEndStatus = taskLastEndStatus;
  }

  public String getNameSpace() {
    return nameSpace;
  }

  public void setNameSpace(String nameSpace) {
    this.nameSpace = nameSpace;
  }
}
