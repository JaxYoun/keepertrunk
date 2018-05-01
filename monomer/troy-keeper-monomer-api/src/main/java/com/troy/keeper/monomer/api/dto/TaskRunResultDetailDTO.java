package com.troy.keeper.monomer.api.dto;

import java.util.List;

/**
 * Description: 任务运行结果详细信息汇总 <br/>
 * Date:     2017/4/12 17:24<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public class TaskRunResultDetailDTO {
    /**
     * 任务总数
     */
    private String totalElements = null;
    /**
     * 任务平均耗时
     */
    private String avgTime = null;
    /**
     * 任务下一次开始时间(只有周期任务才有值)
     */
    private String nextStartTime = null;
    /**
     * 任务类型
     */
    private List<TaskResultDetailDTO> taskRunResultDetail = null;

    public String getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(String totalElements) {
        this.totalElements = totalElements;
    }

    public String getAvgTime() {
        return avgTime;
    }

    public void setAvgTime(String avgTime) {
        this.avgTime = avgTime;
    }

    public String getNextStartTime() {
        return nextStartTime;
    }

    public void setNextStartTime(String nextStartTime) {
        this.nextStartTime = nextStartTime;
    }

    public List<TaskResultDetailDTO> getTaskRunResultDetail() {
        return taskRunResultDetail;
    }

    public void setTaskRunResultDetail(List<TaskResultDetailDTO> taskRunResultDetail) {
        this.taskRunResultDetail = taskRunResultDetail;
    }
}
