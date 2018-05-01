package com.troy.keeper.monomer.api.dto;

import java.util.Map;

/**
 * Description: 任务运行状态信息 <br/>
 * Date:     2017/4/12 9:52<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public class TaskRunStatusDTO {
    /**
     * 已发布未运行任务个数
     */
    private long suspendedCount = 0L;
    /**
     * 已发布运行中任务个数
     */
    private long runningCount = 0L;
    /**
     * 已发布调度中任务个数
     */
    private long scheduledCount = 0L;
    /**
     * 已发布任务的状态集合<任务id，任务状态>
     */
    private Map<String, String> statusMap = null;

    public long getSuspendedCount() {
        return suspendedCount;
    }

    public void setSuspendedCount(long suspendedCount) {
        this.suspendedCount = suspendedCount;
    }

    public long getRunningCount() {
        return runningCount;
    }

    public void setRunningCount(long runningCount) {
        this.runningCount = runningCount;
    }

    public long getScheduledCount() {
        return scheduledCount;
    }

    public void setScheduledCount(long scheduledCount) {
        this.scheduledCount = scheduledCount;
    }

    public Map<String, String> getStatusMap() {
        return statusMap;
    }

    public void setStatusMap(Map<String, String> statusMap) {
        this.statusMap = statusMap;
    }
}
