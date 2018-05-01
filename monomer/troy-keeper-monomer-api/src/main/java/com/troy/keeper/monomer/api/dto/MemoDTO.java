package com.troy.keeper.monomer.api.dto;

/**
 * Description: 内存资源信息 <br/>
 * Date: 2017/4/6 14:06<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public class MemoDTO {
  /**
   * 最小内存
   */
  private String minMemo = null;
  /**
   * 最大内存
   */
  private String maxMemo = null;

    public String getMinMemo() {
        return minMemo;
    }

    public void setMinMemo(String minMemo) {
        this.minMemo = minMemo;
    }

    public String getMaxMemo() {
        return maxMemo;
    }

    public void setMaxMemo(String maxMemo) {
        this.maxMemo = maxMemo;
    }
}
