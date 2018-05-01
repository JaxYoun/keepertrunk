package com.troy.keeper.task.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.task.domain.ScheduleJob;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yg on 2017/6/22.
 */
public interface TaskRepository extends BaseRepository<ScheduleJob, Serializable> {

    @Query("select scheduleJob from ScheduleJob scheduleJob")
    public List<ScheduleJob> getAllTask();
}
