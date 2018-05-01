package com.troy.keeper.monomer.api.dto;

/**
 * Description: CPU资源信息 <br/>
 * Date: 2017/4/6 14:06<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public class CpuDTO {
  /**
   * 最小CPU
   */
  private String minCpu = null;
  /**
   * 最大CPU
   */
  private String maxCpu = null;

  public String getMinCpu() {
    return minCpu;
  }

  public void setMinCpu(String minCpu) {
    this.minCpu = minCpu;
  }

  public String getMaxCpu() {
    return maxCpu;
  }

  public void setMaxCpu(String maxCpu) {
    this.maxCpu = maxCpu;
  }
}
