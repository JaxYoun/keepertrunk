package com.troy.keeper.monomer.api.dto;

/**
 * Description: 磁盘资源信息 <br/>
 * Date: 2017/4/6 14:06<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public class DiskDTO {
  /**
   * 磁盘存储空间总大小
   */
  private String totalDiskSpace = null;
  /**
   * 磁盘存储空间已用大小
   */
  private String usedDiskSpace = null;
  /**
   * 磁盘存储空间剩余大小
   */
  private String remainDiskSpace = null;

  public String getTotalDiskSpace() {
    return totalDiskSpace;
  }

  public void setTotalDiskSpace(String totalDiskSpace) {
    this.totalDiskSpace = totalDiskSpace;
  }

  public String getUsedDiskSpace() {
    return usedDiskSpace;
  }

  public void setUsedDiskSpace(String usedDiskSpace) {
    this.usedDiskSpace = usedDiskSpace;
  }

  public String getRemainDiskSpace() {
    return remainDiskSpace;
  }

  public void setRemainDiskSpace(String remainDiskSpace) {
    this.remainDiskSpace = remainDiskSpace;
  }
}
