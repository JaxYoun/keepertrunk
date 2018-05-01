package com.troy.keeper.system.web.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.base.rest.BaseResource;
import com.troy.keeper.system.config.Const;
import com.troy.keeper.system.domain.SmUser;
import com.troy.keeper.system.dto.SmOrgDTO;
import com.troy.keeper.system.dto.SmUserDTO;
import com.troy.keeper.system.service.SmUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by SimonChu on 2017/5/27.
 */
@RestController
public class SmUserResource extends BaseResource<SmOrgDTO> {

    private final Logger log = LoggerFactory.getLogger(SmUserResource.class);

    @Autowired
    private SmUserService smUserService;

    // 默认分页条数
    private static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * 查询用户列表数据
     * 查询用户列表数据（机构ID：false和用户名称：false 模糊组合查询） 2017/6/7 接口测试完成
     *
     * @return
     */
    @RequestMapping(value = "/api/system/user/list", method = RequestMethod.POST)
    public ResponseDTO<List<Map<String, Object>>> list(@RequestBody SmUserDTO smUserDTO, HttpServletRequest httpServletRequest) {
        try {
            return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, smUserService.list(smUserDTO,httpServletRequest));
        } catch (Exception e) {
            log.error("[SmUserResource Class] list:查询用户列表数据接口异常", e);
            return getResponseDTO(Const.CODE_202, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 查询用户列表数据（分页）
     * listForPage：查询用户列表数据（分页）（机构ID：false和用户名称：false 模糊组合查询） 2017/6/8 接口测试完成
     *
     * @param smUserDTO
     * @return
     */
    @RequestMapping(value = "/api/system/user/listForPage", method = RequestMethod.POST)
    public ResponseDTO<Page<Map<String, Object>>> listForPage(@RequestBody SmUserDTO smUserDTO) {
        try {
            if (smUserDTO.getPage() == 0) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_USER_PAGE, null);
            } else {
                smUserDTO.setPage(smUserDTO.getPage() - 1);
            }
            if (smUserDTO.getSize() == 0) {
                smUserDTO.setSize(DEFAULT_PAGE_SIZE);
            }
            return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, smUserService.listForPage(smUserDTO));
        } catch (Exception e) {
            log.error("[SmUserResource Class] listForPage:查询用户列表数据（分页）接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 查询指定用户信息
     * get：查询指定用户信息接口 2017/6/7 接口测试完成
     *
     * @param smUserDTO
     * @return
     */
    @RequestMapping(value = "/api/system/user/get", method = RequestMethod.POST)
    public ResponseDTO<Map<String, Object>> get(@RequestBody SmUserDTO smUserDTO) {
        try {
            if (smUserDTO.getId() == null) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_USER_ID, null);
            }
            return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, smUserService.get(smUserDTO));
        } catch (Exception e) {
            log.error("[SmUserResource Class] get:查询指定用户信息接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 删除指定用户（逻辑删除）
     * del：删除指定用户接口 2017/6/8 接口测试完成
     *
     * @param smUserDTO
     * @return
     */
    @RequestMapping(value = "/api/system/user/del", method = RequestMethod.POST)
    public ResponseDTO<SmUser> del(@RequestBody SmUserDTO smUserDTO) {
        try {
            if (smUserDTO.getId() == null) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_USER_ID, null);
            }
            smUserService.del(smUserDTO);
            return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, null);
        } catch (Exception e) {
            log.error("[SmUserResource Class] del:删除指定用户接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 用户保存接口
     * save：用户保存接口（新增及修改功能） 2017/6/7 接口测试完成
     *
     * @return
     */
    @RequestMapping(value = "/api/system/user/save", method = RequestMethod.POST)
    public ResponseDTO<Object> save(@RequestBody SmUserDTO smUserDTO) {
        try {
            // 判断必传字段
            if (smUserDTO.getOrgId() == null || ("").equals(smUserDTO.getOrgId())) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_USER_ORGID, null);
            }
            if (smUserDTO.getUserName() == null || ("").equals(smUserDTO.getUserName())) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_USER_USER_NAME, null);
            }
            if (smUserDTO.getType() == null || ("").equals(smUserDTO.getType())) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_USER_TYPE, null);
            }
            if (smUserDTO.getLoginName() == null || ("").equals(smUserDTO.getLoginName())) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_USER_LOGIN_NAME, null);
            }
            if (smUserDTO.getEmail() == null || ("").equals(smUserDTO.getEmail())) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_USER_EMAIL, null);
            }
            // 如果查询返回有数据则按需判断登录ID是否重复
            List<SmUser> list = smUserService.checkLoginName(smUserDTO);
            // 如果是修改
            if (list.size() != 0 && smUserDTO.getId() != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getId().longValue() != smUserDTO.getId().longValue()) {
                        return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_USER_LOGIN_NAME_REPEAT, null);
                    }
                }
                // 如果是新增
            } else if (smUserDTO.getId() == null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getLoginName().equals(smUserDTO.getLoginName())) {
                        return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_USER_LOGIN_NAME_REPEAT, null);
                    }
                }
            }
            // 如果查询返回有数据则判定email重复
            List<SmUser> emailCheck = smUserService.checkEmail(smUserDTO);
            // 如果是修改
            if (emailCheck.size() != 0 && smUserDTO.getId() != null) {
                for (int q = 0; q < emailCheck.size(); q++) {
                    if (emailCheck.get(q).getId().longValue() != smUserDTO.getId().longValue()) {
                        return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_USER_EMAIL_REPEAT, null);
                    }
                }
                // 如果是新增
            } else if (smUserDTO.getId() == null) {
                for (int i = 0; i < emailCheck.size(); i++) {
                    if (emailCheck.get(i).getEmail().equals(smUserDTO.getEmail())) {
                        return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_USER_EMAIL_REPEAT, null);
                    }
                }
            }
            if (smUserDTO.getId() == null) {
                // 新增
                smUserService.createData(smUserDTO);
            } else {
                // 修改
                smUserService.updateData(smUserDTO);
            }
            return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, null);
        } catch (Exception e) {
            log.error("[SmUserResource Class] save:用户保存接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 机构用户树查询
     * queryOrgUserTree：机构用户树查询接口 2017/6/8 接口测试完成
     *
     * @return
     */
    @RequestMapping(value = "/api/system/user/queryOrgUserTree", method = RequestMethod.POST)
    public ResponseDTO<Object> queryOrgUserTree(HttpServletRequest httpServletRequest) {
        try {
            return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, smUserService.queryOrgUserTree(httpServletRequest));
        } catch (NullPointerException e2) {
            log.error("[SmUserResource Class] queryOrgUserTree:机构用户树查询接口异常，数据存在残缺", e2);
            return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_USER_ORG_AND_USER_TREE_ERROR, null);
        } catch (Exception e) {
            log.error("[SmUserResource Class] queryOrgUserTree:机构用户树查询接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 用户密码修改
     * changeUserPassword：用户密码修改接口 2017/6/8 接口测试完成
     *
     * @return
     */
    @RequestMapping(value = "/api/system/user/changeUserPassword", method = RequestMethod.POST)
    public ResponseDTO<Object> changeUserPassword(@RequestBody SmUserDTO smUserDTO) {
        try {
            if (smUserDTO.getId() == null || ("").equals(smUserDTO.getId())) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_USER_ID, null);
            }
            if (smUserDTO.getOldPassword() == null || ("").equals(smUserDTO.getOldPassword())) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_USER_OLD_PASSWORD, null);
            }
            if (smUserDTO.getNewPassword() == null || ("").equals(smUserDTO.getNewPassword())) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_USER_NEW_PASSWORD, null);
            }
            // 判断账户是否存在
            if (smUserService.ifUserId(smUserDTO) == 0) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_USER_NOT_USER_INFO, null);
            }
            // 检查老密码与当前账户密码是否一致
            if (smUserService.checkUserOldPassword(smUserDTO) != 0) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_USER_NEW_AND_OLDPASSWORD_ERROR, null);
            } else {
                //修改密码
                smUserService.changeUserPassword(smUserDTO);
            }
            return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, null);
        } catch (Exception e) {
            log.error("[SmUserResource Class] changeUserPassword:用户密码修改接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 激活指定用户
     *
     * @param smUserDTO
     * @return
     */
    @RequestMapping(value = "/api/system/user/activate", method = RequestMethod.POST)
    public ResponseDTO<?> activate(@RequestBody SmUserDTO smUserDTO) {
        if (smUserDTO.getId() == null) {
            return fail(Const.MSG_CODE_202_USER_ID);
        }
        smUserService.activate(smUserDTO);
        return success();
    }

    /**
     * 停用指定用户
     *
     * @param smUserDTO
     * @return
     */
    @RequestMapping(value = "/api/system/user/inactivate", method = RequestMethod.POST)
    public ResponseDTO<?> inactivate(@RequestBody SmUserDTO smUserDTO) {
        if (smUserDTO.getId() == null) {
            return fail(Const.MSG_CODE_202_USER_ID);
        }
        smUserService.inactivate(smUserDTO);
        return success();
    }

    /**
     * 重置用户密码（管理员专用）
     *
     * @param smUserDTO
     * @return
     */
    @RequestMapping(value = "/api/system/user/resetPassword", method = RequestMethod.POST)
    public ResponseDTO<?> resetPassword(@RequestBody SmUserDTO smUserDTO) {
        if (smUserDTO.getId() == null) {
            return fail(Const.MSG_CODE_202_USER_ID);
        }
        smUserService.resetPassword(smUserDTO);
        return success();
    }

}
