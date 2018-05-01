package com.troy.uaa.service.mapper;

import com.troy.api.dto.RoleDTO;
import com.troy.uaa.domain.Role;
import org.mapstruct.Mapper;

/**
 * Created by yjm on 2017/4/25.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RoleMapper {

    RoleDTO roleToDto(Role role);
}
