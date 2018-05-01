package com.troy.keeper.activiti.service.mapper;

import com.troy.keeper.activiti.domain.ActReDeployment;
import com.troy.keeper.activiti.dto.QueryDeploymentDTO;
import org.mapstruct.Mapper;

import java.util.List;


/**
 * Date:     2017/6/2 15:50<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
@Mapper(componentModel = "spring", uses = {})
public interface ActReDeploymentMapper {
    QueryDeploymentDTO objToDTO(ActReDeployment deployment);

    List<QueryDeploymentDTO> objsToDTOs(List<ActReDeployment> deployments);
}
