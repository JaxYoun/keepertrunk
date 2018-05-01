package com.troy.keeper.activiti.repository;

import com.troy.keeper.activiti.domain.TaskClaimRecord;
import com.troy.keeper.core.base.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;


/**
 * Date:     2017/6/2 15:50<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public interface TaskClaimRecordRepository extends BaseRepository<TaskClaimRecord, Serializable> {
    @Modifying
    @Query(value = "delete from TaskClaimRecord t where t.procInstId = ?1")
    void deleteByProcInstId(String procInstId);
}
