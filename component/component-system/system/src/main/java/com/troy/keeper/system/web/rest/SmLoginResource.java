package com.troy.keeper.system.web.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.base.entity.Account;
import com.troy.keeper.core.security.SecurityUtils;
import com.troy.keeper.system.config.Const;
import com.troy.keeper.system.dto.SmLoginDTO;
import com.troy.keeper.system.dto.UserInfoDTO;
import com.troy.keeper.system.service.SmLoginService;
import com.troy.keeper.system.util.CheckCodeUtil;
import com.troy.keeper.system.util.PostUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by SimonChu on 2017/6/14.
 */
@RestController
public class SmLoginResource extends BaseSystemResource<SmLoginDTO> {

    private final Logger log = LoggerFactory.getLogger(SmLoginResource.class);

    @Autowired
    private SmLoginService smLoginService;

    @Autowired
    private PostUtils postUtils;

    /**
     * 获取登录验证码接口
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/api/system/login/getCode", method = RequestMethod.POST)
    public ResponseDTO<byte[]> queryPostUsers(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckCodeUtil checkCodeUtil = new CheckCodeUtil();
            byte[] image = checkCodeUtil.getgetRandcodeByByteArray(request, response, "imageCode");
            if (image == null) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_LOGIN_IMAGE_ERROR, null);
            }
            return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, image);
        } catch (Exception e) {
            log.error("[SmLoginResource Class] queryPostUsers:获取登录验证码接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 验证登录验证码
     *
     * @param smLoginDTO
     * @return
     */
    @RequestMapping(value = "/api/system/login/checkCode", method = RequestMethod.POST)
    public ResponseDTO<Object> checkCode(@RequestBody SmLoginDTO smLoginDTO, HttpServletRequest request) {
        String validateCode;
        try {
            if (smLoginDTO.getCode() == null || ("").equals(smLoginDTO.getCode())) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_LOGIN_CODE, null);
            }
            String code = smLoginDTO.getCode().toUpperCase();
            try{
                validateCode = request.getSession().getAttribute("imageCode").toString();
                if (validateCode.equals(code)) {
                    return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, true);
                }
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_LOGIN_CODE_CHECK_ERROR, false);
            }catch (Exception e){
                return fail("验证码已过期！请重新获取验证码");
            }
        } catch (Exception e) {
            log.error("[SmLoginResource Class] checkCode:验证登录验证码接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 判断当前用户登录状态
     *
     * @return
     */
    @RequestMapping(value = "/api/system/login/currentUserStatus", method = RequestMethod.POST)
    public ResponseDTO<Object> currentUserStatus() {
        try {
            if (SecurityUtils.isAuthenticated()) {
                return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, true);
            } else {
                return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, false);
            }
        } catch (Exception e) {
            log.error("[SmLoginResource Class] currentUserStatus:判断当前用户登录状态接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 获取当前登录用户缓存信息
     *
     * @return
     */
    @RequestMapping(value = "/api/system/login/getUserByUserIdToCache", method = RequestMethod.POST)
    public ResponseDTO<UserInfoDTO> getUserByUserIdToCache() {
        try {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            Object object = securityContext.getAuthentication().getPrincipal();
            if (SecurityUtils.isAuthenticated()) {
                return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, smLoginService.getUserByUserIdToCache(((Account) object).getAccountId()));
            } else {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_200, Const.MSG_CODE_202_LOGIN_LOGIN_OUT);
            }
        } catch (Exception e) {
            log.error("[SmLoginResource Class] getUserByUserIdToCache:获取当前登录用户缓存信息接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    @RequestMapping(value = "/api/system/login/getUserByUserIdToCacheTest", method = RequestMethod.POST)
    public ResponseDTO<UserInfoDTO> getUserByUserIdToCacheTest() {
        return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, getLoginUserInfo());
    }

    /**
     * 获取当前登录用户的岗位列表
     *
     * @return
     */
    @RequestMapping(value = "/api/system/login/getLoginUserPosts", method = RequestMethod.POST)
    public ResponseDTO<Object> getLoginUserPosts() {
        try {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            Object object = securityContext.getAuthentication().getPrincipal();
            if (SecurityUtils.isAuthenticated()) {
                UserInfoDTO userInfoDTO = smLoginService.getUserByUserIdToCache(((Account) object).getAccountId());
                return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, userInfoDTO.getPosts());
            } else {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_200, Const.MSG_CODE_202_LOGIN_LOGIN_OUT);
            }
        } catch (Exception e) {
            log.error("[SmLoginResource Class] getLoginUserPosts:获取当前登录用户的岗位列表接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 切换岗位接口
     *
     * @param smLoginDTO
     * @return
     */
    @RequestMapping(value = "/api/system/login/switchPost", method = RequestMethod.POST)
    public ResponseDTO<Object> switchPost(@RequestBody SmLoginDTO smLoginDTO,HttpServletRequest httpServletRequest) {
        try {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            Object object = securityContext.getAuthentication().getPrincipal();
            if (smLoginDTO.getCurrentPostId() == null || ("").equals(smLoginDTO.getCurrentPostId())) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_200, Const.MSG_CODE_202_LOGIN_CURRENT_POST_ID);
            }
            if (SecurityUtils.isAuthenticated()) {
                //2018-2-26 添加当前用户所属机构存入redis
                if (postUtils.getOrgLimit()) {
                    smLoginService.cacheUserOrg(smLoginDTO.getCurrentPostId(),httpServletRequest);
                }
                // 通过选定的岗位返回菜单列表
                UserInfoDTO userInfoDTO = smLoginService.getLoginUserCurrentPostMenuLists(((Account) object).getAccountId(), smLoginDTO.getCurrentPostId());
                return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, userInfoDTO.getMenuIds());
            } else {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_200, Const.MSG_CODE_202_LOGIN_LOGIN_OUT);
            }
        } catch (Exception e) {
            log.error("[SmLoginResource Class] switchPost:切换岗位接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

}
