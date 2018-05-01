package com.troy.keeper.system.web.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.base.rest.BaseResource;
import com.troy.keeper.system.config.Const;
import com.troy.keeper.system.dto.SmPostUserDTO;
import com.troy.keeper.system.service.SmLimitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by SimonChu on 2017/6/6.
 */
@RestController
public class SmLimitResource extends BaseResource<SmPostUserDTO> {

    private final Logger log = LoggerFactory.getLogger(SmLimitResource.class);

    @Autowired
    private SmLimitService smLimitService;

    /**
     * 查询岗位下的用户：查询岗位赋权
     * queryPostUsers:查询岗位下的用户：查询岗位赋权 2017/6/6 接口测试完成
     *
     * @param smPostUserDTO
     * @return
     */
    @RequestMapping(value = "/api/system/limit/queryPostUsers", method = RequestMethod.POST)
    public ResponseDTO<List<Long>> queryPostUsers(@RequestBody SmPostUserDTO smPostUserDTO) {
        try {
            if (smPostUserDTO.getPostId() == null || ("").equals(smPostUserDTO.getPostId())) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_LIMIT_POST_ID, null);
            }
            return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, smLimitService.queryPostUsers(smPostUserDTO));
        } catch (Exception e) {
            log.error("[SmLimitResource Class] queryPostUsers:查询岗位下的用户：查询岗位赋权接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 查询用户的岗位
     * queryUserPosts:查询用户的岗位 2017/6/6 接口测试完成
     *
     * @param smPostUserDTO
     * @return
     */
    @RequestMapping(value = "/api/system/limit/queryUserPosts", method = RequestMethod.POST)
    public ResponseDTO<List<Map<String, Object>>> queryUserPosts(@RequestBody SmPostUserDTO smPostUserDTO) {
        try {
            if (smPostUserDTO.getUserId() == null || ("").equals(smPostUserDTO.getUserId())) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_LIMIT_USER_ID, null);
            }
            return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, smLimitService.queryUserPosts(smPostUserDTO));
        } catch (Exception e) {
            log.error("[SmLimitResource Class] queryUserPosts:查询用户的岗位接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 保存岗位权限
     * savePostUsers:保存岗位权限 2017/6/6 接口测试完成
     *
     * @param smPostUserDTO
     * @return
     */
    @RequestMapping(value = "/api/system/limit/savePostUsers", method = RequestMethod.POST)
    public ResponseDTO<Object> savePostUsers(@RequestBody SmPostUserDTO smPostUserDTO) {
        try {
            if (smPostUserDTO.getPostId() == null || ("").equals(smPostUserDTO.getPostId())) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_LIMIT_POST_ID, null);
            }
            smLimitService.savePostUsers(smPostUserDTO);
            return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, null);
        } catch (Exception e) {
            log.error("[SmLimitResource Class] savePostUsers:保存岗位权限接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

}
