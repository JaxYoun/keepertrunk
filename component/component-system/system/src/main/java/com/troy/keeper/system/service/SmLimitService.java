package com.troy.keeper.system.service;

import com.troy.keeper.core.base.service.BaseService;
import com.troy.keeper.system.domain.SmPostUser;
import com.troy.keeper.system.dto.SmPostUserDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by SimonChu on 2017/6/6.
 */
public interface SmLimitService extends BaseService<SmPostUser, SmPostUserDTO> {

    public List<Long> queryPostUsers(SmPostUserDTO smPostUserDTO);

    public List<Map<String, Object>> queryUserPosts(SmPostUserDTO smPostUserDTO);

    public void savePostUsers(SmPostUserDTO smPostUserDTO);

}
