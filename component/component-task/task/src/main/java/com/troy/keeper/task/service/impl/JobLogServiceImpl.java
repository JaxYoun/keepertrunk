package com.troy.keeper.task.service.impl;

import com.troy.keeper.core.base.dto.BaseDTO;
import com.troy.keeper.core.base.service.BaseServiceImpl;
import com.troy.keeper.task.domain.JobLog;
import com.troy.keeper.task.repository.JobLogRepository;
import com.troy.keeper.task.service.JobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yg on 2017/6/27.
 */
@Service
@Transactional
public class JobLogServiceImpl extends BaseServiceImpl<JobLog, BaseDTO> implements JobLogService {
    @Autowired
    private JobLogRepository jobLogRepository;

    @Override
    public void saveJobLog(JobLog jobLog) {
        jobLogRepository.save(jobLog);
    }

    @Override
    public Page<JobLog> queryJobLogByPage(Long jobId, Long startDate, Long endDate, int page, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        Specification<JobLog> spec = (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            if (jobId != null) {
                list.add(cb.equal(root.get("jobId").as(Long.class), jobId));
            }
            if (startDate != null) {
                list.add(cb.greaterThanOrEqualTo(root.get("createdDate").as(Long.class), startDate));
            }
            if (endDate != null) {
                list.add(cb.lessThanOrEqualTo(root.get("createdDate").as(Long.class), endDate));
            }
            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        };
        return jobLogRepository.findAll(spec, pageRequest);
    }
}