package com.troy.keeper.task.service;

import com.troy.keeper.core.base.dto.BaseDTO;
import com.troy.keeper.core.base.service.BaseService;
import com.troy.keeper.task.domain.JobLog;
import org.springframework.data.domain.Page;

/**
 * Created by yg on 2017/6/27.
 */
public interface JobLogService extends BaseService<JobLog, BaseDTO> {
    /**
     * 保存jobLog
     * @param jobLog
     */
    public void saveJobLog(JobLog jobLog);

    /**
     * 分页查询作业日志
     * @param jobId
     * @param startDate
     * @param endDate
     * @param page
     * @param size
     * @return
     */
    public Page<JobLog> queryJobLogByPage(Long jobId, Long startDate, Long endDate, int page, int size);
}
