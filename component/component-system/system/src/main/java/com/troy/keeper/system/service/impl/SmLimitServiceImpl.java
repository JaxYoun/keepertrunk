package com.troy.keeper.system.service.impl;

import com.troy.keeper.core.base.service.BaseServiceImpl;
import com.troy.keeper.system.domain.SmPostUser;
import com.troy.keeper.system.dto.SmPostUserDTO;
import com.troy.keeper.system.repository.SmPostUserRepository;
import com.troy.keeper.system.service.SmLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SimonChu on 2017/6/6.
 */
@Service
@Transactional
public class SmLimitServiceImpl extends BaseServiceImpl<SmPostUser, SmPostUserDTO> implements SmLimitService {

    @Autowired
    private SmPostUserRepository smPostUserRepository;

    /**
     * 查询岗位下的用户：查询岗位赋权
     *
     * @param smPostUserDTO
     * @return
     */
    public List<Long> queryPostUsers(SmPostUserDTO smPostUserDTO) {
        List<Long> smPostUsers = smPostUserRepository.queryPostUsers(smPostUserDTO.getPostId());
        return smPostUsers;
    }

    /**
     * 查询用户的岗位
     *
     * @param smPostUserDTO
     * @return
     */
    public List<Map<String, Object>> queryUserPosts(SmPostUserDTO smPostUserDTO) {
        List<Object[]> smPostUsers = smPostUserRepository.queryUserPosts(smPostUserDTO.getUserId());
        List<Map<String, Object>> data = new ArrayList<>();
        if (smPostUsers.size() != 0) {
            for (int i = 0; i < smPostUsers.size(); i++) {
                Map<String, Object> map =  new HashMap<>();
                Object[] obj = smPostUsers.get(i);
                for (int j = 0; j < 2; j++) {
                    if (j==0){
                        map.put("postId",obj[j]);
                    }
                    if (j==1){
                        map.put("userId",obj[j]);
                    }
                }
                data.add(map);
            }
        }
        return data;
    }

    /**
     * 保存岗位权限
     *
     * @param smPostUserDTO
     */
    public void savePostUsers(SmPostUserDTO smPostUserDTO) {
        // 清空该岗位下的用户信息
        smPostUserRepository.deletePostUserByPostId(smPostUserDTO.getPostId());
        // 重新保存该岗位下的用户信息
        if (smPostUserDTO.getUserIds() != null) {
            List<Long> userIds = smPostUserDTO.getUserIds();
            if (userIds.size() != 0) {
                for (int i = 0; i < userIds.size(); i++) {
                    SmPostUser smPostUser = new SmPostUser();
                    smPostUser.setPostId(smPostUserDTO.getPostId());
                    smPostUser.setUserId(userIds.get(i));
                    smPostUserRepository.save(smPostUser);
                }
            }
        }
    }

}
