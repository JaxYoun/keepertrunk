package com.troy.keeper.system.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.troy.keeper.core.base.service.BaseServiceImpl;
import com.troy.keeper.core.base.service.RedisService;
import com.troy.keeper.core.security.SecurityUtils;
import com.troy.keeper.system.domain.SmPost;
import com.troy.keeper.system.domain.SmUser;
import com.troy.keeper.system.dto.SmUserDTO;
import com.troy.keeper.system.dto.UserInfoDTO;
import com.troy.keeper.system.repository.*;
import com.troy.keeper.system.security.SysAccount;
import com.troy.keeper.system.service.SmLoginService;
import com.troy.keeper.system.util.UserParamter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by SimonChu on 2017/6/12.
 */
@Service
@Transactional
public class SmLoginServiceImpl extends BaseServiceImpl<SmUser, SmUserDTO> implements SmLoginService {

    @Autowired
    private SmUserRepository smUserRepository;

    @Autowired
    private SmPostRoleRepository smPostRoleRepository;

    @Autowired
    private SmPostUserRepository smPostUserRepository;

    @Autowired
    private SmRoleMenuRepository smRoleMenuRepository;

    @Autowired
    private SmPostRepository smPostRepository;

    @Autowired
    private SmOrgRepository smOrgRepository;

    @Autowired
    private RedisService redisService;

    private static final ObjectMapper mapper = new ObjectMapper();

    private final Logger log = LoggerFactory.getLogger(SmLoginServiceImpl.class);

    /**
     * 获取用户数据
     *
     * @param loginName
     * @return
     */
    public Optional<SmUser> getUserInfoByLoginName(String loginName) {
        Optional<SmUser> smUserOptional = smUserRepository.findOneWithAuthoritiesByLoginName(loginName);
        return smUserOptional;
    }

    /**
     * 获取用户岗位列表
     *
     * @param userId
     * @return
     */
    public List<Long> getUserPostUserListByUserId(Long userId) {
        return smPostUserRepository.getPostUserByUserId(userId);
    }

    /**
     * 获取用户数据（ID）
     *
     * @param id
     * @return
     */
    @Cacheable(cacheNames = "UserParamter", key = "#id")
    public UserInfoDTO getUserByUserIdToCache(Long id) {
        log.debug("---------------开始加载getUserByUserIdToCache----------------");
        SmUserDTO smUserDTO = new SmUserDTO();
        smUserDTO.setId(id);
        SmUser smUser = smUserRepository.findOne(smUserDTO.getId());
        UserParamter userParamter = new UserParamter();
        userParamter.setId(smUser.getId());
        userParamter.setLoginName(smUser.getLoginName());
        userParamter.setEmail(smUser.getEmail());
        userParamter.setMobilePhone(smUser.getMobilePhone());
        userParamter.setOrgId(smUser.getOrgId());
        userParamter.setType(smUser.getType());
        userParamter.setUserName(smUser.getUserName());
        List<Long> posts = getUserPostUserListByUserId(id);
        List<Map<String, Object>> postDatas = new ArrayList<>();
        for (int i = 0; i < posts.size(); i++) {
            SmPost smPost = smPostRepository.findOne(posts.get(i));
            if (("1").equals(smPost.getStatus())) {
                Map<String, Object> data = new HashMap<>();
                data.put("postName", smPost.getPostName());
                data.put("postId", posts.get(i));
                postDatas.add(data);
            }
        }
        userParamter.setPosts(postDatas);
        return userParamter.getUserParamterDTO();
    }

    /**
     * 删除该用户ID的缓存信息
     *
     * @param id
     */
    @CacheEvict(cacheNames = "UserParamter", key = "#id")
    public void delUserIdCache(Long id) {
        log.info("[系统管理] 正在清空UserID：" + id + "的SpringCache缓存用户信息");
    }


    /**
     * 根据用户选择的岗位信息加载机构
     * @param postId
     */
    @Override
    public void cacheUserOrg(Long postId, HttpServletRequest httpServletRequest) {
        //根据岗位id找出对应的机构id
        Long orgId = smPostRepository.findOrgIdById(postId);
        if (orgId != null){
            try {
                List<Long> orgIds = smOrgRepository.findAllOrgIdsById(orgId+"-%","%-"+orgId+"-%",orgId);
                String jsonStr = mapper.writeValueAsString(orgIds);
                redisService.set("post_"+postId,jsonStr);
                // 得到当前的认证信息
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                SysAccount sysAccount = (SysAccount)auth.getPrincipal();
                sysAccount.setPostId(postId);
                // 生成新的认证信息
                Authentication newAuth = new UsernamePasswordAuthenticationToken(sysAccount, auth.getCredentials(), auth.getAuthorities());
                // 重置认证信息
                SecurityContextHolder.getContext().setAuthentication(newAuth);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 切换岗位查询岗位角色菜单后缓存的用户信息
     *
     * @param id
     * @param currentPostId
     * @return
     * @throws Exception
     */
    @CachePut(cacheNames = "UserParamter", key = "#id")
    public UserInfoDTO getLoginUserCurrentPostMenuLists(Long id, Long currentPostId) throws Exception {
        UserInfoDTO userInfoDTO = getUserByUserIdToCache(id);
        userInfoDTO.setCurrentPostId(currentPostId);
        List<Long> roleLists = smPostRoleRepository.getPostRoleByPostId(currentPostId);
        if (roleLists != null && !roleLists.isEmpty()) {
            List<Object[]> objects = smRoleMenuRepository.getMenuIdsLevelByRoleIds(roleLists);
            List<Map<String, Object>> menuIds = new ArrayList<>();
            for (int i = 0; i < objects.size(); i++) {
                menuIds.add(switchMenusData(objects.get(i)));
            }
            userInfoDTO.setMenuIds(menuIds);
        }
        return userInfoDTO;
    }

    /**
     * 转换菜单数据
     *
     * @param objects
     * @return
     */
    private Map<String, Object> switchMenusData(Object[] objects) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", objects[0].toString());
        if (objects[1] != null) {
            data.put("pId", objects[1].toString());
        } else {
            data.put("pId", "");
        }
        data.put("menuName", objects[2].toString());
        if (objects[3] != null) {
            data.put("menuUrl", objects[3].toString());
        } else {
            data.put("menuUrl", "");
        }
        if (objects[4] != null) {
            data.put("menuIcon", objects[4].toString());
        } else {
            data.put("menuIcon", "");
        }
        data.put("orderCode", objects[5].toString());
        data.put("relationship", objects[6].toString());
        data.put("menuType", objects[7].toString());
        data.put("limitLevel", objects[8].toString());
        if (!StringUtils.isEmpty(objects[9])) {
            data.put("menuCode", objects[9].toString());
        } else {
            data.put("menuCode", "");
        }
        return data;
    }

    /**
     * 获取岗位菜单列表
     *
     * @param postId
     * @return
     */
    public List<Object[]> getMenuIdsByPostId(Long postId) {
        //获取该岗位下的所有角色列表
        List<Long> roleIds = smPostRoleRepository.getPostRoleByPostId(postId);
        //获取角色下所有菜单权限
        List<Object[]> menuIds = new ArrayList<>();
        for (int i = 0; i < roleIds.size(); i++) {
            List<Object[]> data = smRoleMenuRepository.getMenuIdsByRoleId(roleIds.get(i));
            menuIds.addAll(data);
        }
        return menuIds;
    }

}
