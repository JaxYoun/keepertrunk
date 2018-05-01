package com.troy.keeper.system.service;

import com.troy.keeper.core.base.service.BaseService;
import com.troy.keeper.system.domain.SmUser;
import com.troy.keeper.system.dto.SmUserDTO;
import com.troy.keeper.system.dto.UserInfoDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * Created by SimonChu on 2017/6/12.
 */
public interface SmLoginService extends BaseService<SmUser, SmUserDTO> {

    public Optional<SmUser> getUserInfoByLoginName(String loginName);

    public List<Long> getUserPostUserListByUserId(Long userId);

    public UserInfoDTO getUserByUserIdToCache(Long id);

    public UserInfoDTO getLoginUserCurrentPostMenuLists(Long id, Long currentPostId) throws Exception;

    public void delUserIdCache(Long id);

    /**
     * 根据用户选择的岗位信息加载机构
     * @param postId
     */
    public void cacheUserOrg(Long postId ,HttpServletRequest httpServletRequest);
}
