package com.troy.uaa.service.mapper;

import com.troy.api.dto.UserDTO;
import com.troy.uaa.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Created by yjm on 2017/4/9.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UserMapper {

    @Mappings({
            @Mapping(source = "userType", target = "type"),
            @Mapping(source = "userTypeValue", target = "value"),
            @Mapping(source = "login", target = "loginName")
    })
    UserDTO userToDto(User user);

    @Mappings({
        @Mapping(source = "type", target = "userType"),
        @Mapping(source = "loginName", target = "login")
    })
    User DtoToUser(UserDTO userDTO);
}
