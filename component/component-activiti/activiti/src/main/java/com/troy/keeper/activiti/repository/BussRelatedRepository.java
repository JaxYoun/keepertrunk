package com.troy.keeper.activiti.repository;

import com.troy.keeper.activiti.domain.BussRelatedProcess;
import com.troy.keeper.core.base.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;


/**
 * Date:     2017/6/2 15:50<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public interface BussRelatedRepository extends BaseRepository<BussRelatedProcess, Serializable> {
    @Query("select bus from BussRelatedProcess bus where bus.businessId = ?1 and bus.businessType = ?2")
    BussRelatedProcess findByIdAndType(String businessId, String businessType);
}
