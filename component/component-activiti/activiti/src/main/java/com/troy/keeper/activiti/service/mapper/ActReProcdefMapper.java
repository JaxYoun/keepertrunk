package com.troy.keeper.activiti.service.mapper;

import com.troy.keeper.activiti.domain.ActReProcdef;
import com.troy.keeper.activiti.dto.QueryProcDefineDTO;
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
public interface ActReProcdefMapper {
    @Mapping(source = "deployment.id", target = "deployId")
    @Mapping(source = "deployment.deployTime", target = "deployTime")
    QueryProcDefineDTO objToDTO(ActReProcdef actReProcdef);

    List<QueryProcDefineDTO> objsToDTOs(List<ActReProcdef> actReProcdefs);
}
