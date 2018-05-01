package com.troy.keeper.activiti.service.mapper;

import com.troy.keeper.activiti.domain.ToDoTask;
import com.troy.keeper.activiti.dto.ToDoTaskDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


/**
 * Date:     2017/6/2 15:50<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
@Mapper(componentModel = "spring", uses = {})
public interface ToDoTaskMapper {
    @Mapping(source = "taskId", target = "actId")
    @Mapping(source = "taskDefKey", target = "actKey")
    @Mapping(source = "taskDefName", target = "actName")
    @Mapping(source = "assignee", target = "userId")
    @Mapping(source = "procDefId", target = "procdefId")
    @Mapping(source = "procDefName", target = "procdefName")
    ToDoTaskDTO objToDTO(ToDoTask task);

    List<ToDoTaskDTO> objsToDTOs(List<ToDoTask> tasks);
}
