package com.troy.keeper.activiti.repository;

import com.troy.keeper.activiti.domain.ActRuIdentitylink;
import com.troy.keeper.core.base.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;
import java.util.List;


/**
 * Date:     2017/6/2 15:50<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public interface ActRuIdentitylinkRepository extends BaseRepository<ActRuIdentitylink, Serializable> {
    @Query("select userId from ActRuIdentitylink ai where ai.type = 'candidate' and ai.groupId is null and ai.task.id = ?1")
    List<String> queryTaskSingleTodoUser(String taskId);
}
