package com.troy.keeper.monomer.api.dto;

import com.troy.keeper.core.base.dto.BaseDTO;

/**
 * Description: 任务个数统计<br/>
 * Date: 2017/4/6 11:04<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public class TaskCountsDTO extends BaseDTO {
  /**
   * 任务总数
   */
  private String totalCount = null;
  /**
   * 未发布任务个数
   */
  private String draftCount = null;
  /**
   * 已发布未运行任务个数
   */
  private String suspendedCount = null;
  /**
   * 已发布运行中任务个数
   */
  private String runningCount = null;
  /**
   * 已发布调度中任务个数
   */
  private String scheduledCount = null;

  public String getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(String totalCount) {
    this.totalCount = totalCount;
  }

  public String getDraftCount() {
    return draftCount;
  }

  public void setDraftCount(String draftCount) {
    this.draftCount = draftCount;
  }

  public String getSuspendedCount() {
    return suspendedCount;
  }

  public void setSuspendedCount(String suspendedCount) {
    this.suspendedCount = suspendedCount;
  }

  public String getRunningCount() {
    return runningCount;
  }

  public void setRunningCount(String runningCount) {
    this.runningCount = runningCount;
  }

  public String getScheduledCount() {
    return scheduledCount;
  }

  public void setScheduledCount(String scheduledCount) {
    this.scheduledCount = scheduledCount;
  }
}
