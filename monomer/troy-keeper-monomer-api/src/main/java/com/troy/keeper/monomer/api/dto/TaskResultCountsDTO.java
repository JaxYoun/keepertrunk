package com.troy.keeper.monomer.api.dto;

import com.troy.keeper.core.base.dto.BaseDTO;

/**
 * Description: 任务运行结果个数统计<br/>
 * Date: 2017/4/6 11:04<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public class TaskResultCountsDTO extends BaseDTO {
  /**
   * 任务总数
   */
  private String totalCount = null;
  /**
   * 任务运行成功个数
   */
  private volatile String successCount = null;
  /**
   * 任务运行失败个数
   */
  private volatile String failedCount = null;
  /**
   * 任务运行终止个数
   */
  private volatile String killedCount = null;

  public String getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(String totalCount) {
    this.totalCount = totalCount;
  }

  public String getSuccessCount() {
    return successCount;
  }

  public synchronized void setSuccessCount(String successCount) {
    this.successCount = successCount;
  }

  public String getFailedCount() {
    return failedCount;
  }

  public synchronized void setFailedCount(String failedCount) {
    this.failedCount = failedCount;
  }

  public String getKilledCount() {
    return killedCount;
  }

  public synchronized void setKilledCount(String killedCount) {
    this.killedCount = killedCount;
  }
}
