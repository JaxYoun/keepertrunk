package com.troy.keeper.system.web.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.base.rest.BaseResource;
import com.troy.keeper.system.config.Const;
import com.troy.keeper.system.domain.SmRole;
import com.troy.keeper.system.domain.SmRoleMenu;
import com.troy.keeper.system.dto.SmRoleDTO;
import com.troy.keeper.system.dto.SmRoleMenuDTO;
import com.troy.keeper.system.repository.SmRoleRepository;
import com.troy.keeper.system.service.SmRoleService;
import org.apache.commons.lang3.StringUtils;
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

/**
 * Created by SimonChu on 2017/6/1.
 */
@RestController
public class SmRoleResource extends BaseResource<SmRoleDTO> {

    private final Logger log = LoggerFactory.getLogger(SmRoleResource.class);

    @Autowired
    private SmRoleService smRoleService;

    @Autowired
    private SmRoleRepository smRoleRepository;

    // 默认分页条数
    private static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * 查询角色列表
     * list：查询角色列表接口 2017/6/8 接口测试完成
     *
     * @param smRoleDTO
     * @return
     */
    @RequestMapping(value = "/api/system/role/list", method = RequestMethod.POST)
    public ResponseDTO<List<SmRole>> list(@RequestBody SmRoleDTO smRoleDTO,HttpServletRequest httpServletRequest) {
        try {
            return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, smRoleService.list(smRoleDTO,httpServletRequest));
        } catch (Exception e) {
            log.error("[SmRoleResource Class] list:查询角色列表接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 查询角色列表（分页）
     * listForPage：查询角色列表（分页）接口 2017/6/8 接口测试完成
     *
     * @param smRoleDTO
     * @return
     */
    @RequestMapping(value = "/api/system/role/listForPage", method = RequestMethod.POST)
    public ResponseDTO<Page<SmRole>> listForPage(@RequestBody SmRoleDTO smRoleDTO) {
        try {
            if (smRoleDTO.getPage() == 0) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_ROLE_PAGE, null);
            } else {
                smRoleDTO.setPage(smRoleDTO.getPage() - 1);
            }
            if (smRoleDTO.getSize() == 0) {
                smRoleDTO.setSize(DEFAULT_PAGE_SIZE);
            }
            return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, smRoleService.listForPage(smRoleDTO));
        } catch (Exception e) {
            log.error("[SmRoleResource Class] listForPage:查询角色列表（分页）接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 查询指定角色
     * get：查询指定角色接口 2017/6/8 接口测试完成
     *
     * @param smRoleDTO
     * @return
     */
    @RequestMapping(value = "/api/system/role/get", method = RequestMethod.POST)
    public ResponseDTO<SmRole> get(@RequestBody SmRoleDTO smRoleDTO) {
        try {
            if (smRoleDTO.getId() == null || ("").equals(smRoleDTO.getId())) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_ROLE_ID, null);
            }
            return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, smRoleService.get(smRoleDTO));
        } catch (Exception e) {
            log.error("[SmRoleResource Class] get:查询指定角色接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 角色保存
     * save：角色保存接口（新增与修改功能） 2017/6/8 接口测试完成
     *
     * @param smRoleDTO
     * @return
     */
    @RequestMapping(value = "/api/system/role/save", method = RequestMethod.POST)
    public ResponseDTO<Object> save(@RequestBody SmRoleDTO smRoleDTO) {
        try {
            if (smRoleDTO.getRoleName() == null || ("").equals(smRoleDTO.getRoleName())) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_ROLE_ROLE_NAME, null);
            }
//            if (StringUtils.isEmpty(smRoleDTO.getRoleCode())){
//                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_ROLE_ROLE_CODE, null);
//            }
            //验证角色编码是否重复
            if (StringUtils.isNotEmpty(smRoleDTO.getRoleCode())) {
                Long count = smRoleDTO.getId() == null ? smRoleRepository.createCount(smRoleDTO.getRoleCode()) :
                        smRoleRepository.updateCount(smRoleDTO.getRoleCode(), smRoleDTO.getId());
                if (count > 0l) {
                    return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_ROLE_ROLE_CODE_EXIT_ERROR, null);
                }
            }

            //判断是修改还是新增
            if (smRoleDTO.getId() == null || ("").equals(smRoleDTO.getId())) {
                // 新增
                smRoleService.createdData(smRoleDTO);
                return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, null);
            } else {
                // 修改
                smRoleService.updateDate(smRoleDTO);
                return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, null);
            }
        } catch (Exception e) {
            log.error("[SmRoleResource Class] save:角色保存接口（新增与修改功能）接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 删除角色
     * del：删除角色接口 2017/6/8 接口测试完成
     * del：新增删除角色是否与岗位关联检查
     *
     * @param smRoleDTO
     * @return
     */
    @RequestMapping(value = "/api/system/role/del", method = RequestMethod.POST)
    public ResponseDTO<Object> del(@RequestBody SmRoleDTO smRoleDTO) {
        try {
            if (smRoleDTO.getId() == null || ("").equals(smRoleDTO.getId())) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_ROLE_ID, null);
            }
            // 检测该角色是否已存在岗位关联
            if (!smRoleService.checkRole(smRoleDTO)) {
                smRoleService.del(smRoleDTO);
            } else {
                return fail(Const.MSG_CODE_202_ROLE_POST_DEL_ERROR);
            }
            return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, null);
        } catch (Exception e) {
            log.error("[SmRoleResource Class] del:删除角色接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 查询角色对应的菜单
     *
     * @param smRoleMenuDTO
     * @return
     */
    @RequestMapping(value = "/api/system/role/queryMenuListByRoleId", method = RequestMethod.POST)
    public ResponseDTO<List<SmRoleMenu>> queryMenuListByRoleId(@RequestBody SmRoleMenuDTO smRoleMenuDTO) {
        try {
            if (smRoleMenuDTO.getRoleId() == null || ("").equals(smRoleMenuDTO.getRoleId())) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_ROLE_ROLE_ID, null);
            }
            return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, smRoleService.queryMenuListByRoleId(smRoleMenuDTO));
        } catch (Exception e) {
            log.error("[SmRoleResource Class] queryMenuListByRoleId:查询角色对应的菜单接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 保存角色的菜单权限
     *
     * @param smRoleMenuDTO
     * @return
     */
    @RequestMapping(value = "/api/system/role/saveMenuLimit", method = RequestMethod.POST)
    public ResponseDTO<Object> saveMenuLimit(@RequestBody SmRoleMenuDTO smRoleMenuDTO) {
        try {
            if (smRoleMenuDTO.getMenus() == null || ("").equals(smRoleMenuDTO.getMenus())) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_ROLE_MENUS, null);
            }
            if (smRoleMenuDTO.getRoleId() == null || ("").equals(smRoleMenuDTO.getRoleId())) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_ROLE_ROLE_ID, null);
            }
            if (!smRoleService.checkMenuData(smRoleMenuDTO)) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_ROLE_MENU_ID, null);
            }
            smRoleService.saveMenuLimit(smRoleMenuDTO);
            return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, null);
        } catch (Exception e) {
            log.error("[SmRoleResource Class] saveMenuLimit:保存角色的菜单权限接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

}
