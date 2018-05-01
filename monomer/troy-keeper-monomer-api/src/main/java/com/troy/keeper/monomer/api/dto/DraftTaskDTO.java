package com.troy.keeper.monomer.api.dto;

/**
 * Description: 任务草稿 <br/>
 * Date: 2017/4/6 14:06<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public class DraftTaskDTO {
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

  public String getNameSpace() {
    return nameSpace;
  }

  public void setNameSpace(String nameSpace) {
    this.nameSpace = nameSpace;
  }
}
