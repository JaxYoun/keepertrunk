package com.troy.keeper.system.web.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.base.rest.BaseResource;
import com.troy.keeper.system.config.Const;
import com.troy.keeper.system.domain.SmMenu;
import com.troy.keeper.system.dto.SmMenuDTO;
import com.troy.keeper.system.repository.SmMenuRepository;
import com.troy.keeper.system.service.SmMenuService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by SimonChu on 2017/6/1.
 * 【SmMenuResource】 菜单管理接口类 2017/6/8 接口测试完成
 */
@RestController
public class SmMenuResource extends BaseResource<SmMenuDTO> {

    private final Logger log = LoggerFactory.getLogger(SmMenuResource.class);

    @Autowired
    private SmMenuService smMenuService;

    @Autowired
    private SmMenuRepository smMenuRepository;

    // 默认分页条数
    private static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * 查询菜单接口
     * list：查询菜单接口 2017/6/8 接口测试完成
     *
     * @return
     */
    @RequestMapping(value = "/api/system/menu/list", method = RequestMethod.POST)
    public ResponseDTO<List<SmMenu>> list() {
        try {
            return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, smMenuService.list());
        } catch (Exception e) {
            log.error("[SmMenuResource Class] list:查询菜单接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 分页查询菜单接口
     * listForPage：查询菜单接口（分页） 2017/6/8 接口测试完成
     *
     * @param smMenuDTO
     * @return
     */
    @RequestMapping(value = "/api/system/menu/listForPage", method = RequestMethod.POST)
    public ResponseDTO<Page<SmMenu>> listForPage(@RequestBody SmMenuDTO smMenuDTO) {
        try {
            if (smMenuDTO.getPage() == 0) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_MENU_PAGE, null);
            } else {
                smMenuDTO.setPage(smMenuDTO.getPage() - 1);
            }
            if (smMenuDTO.getSize() == 0) {
                smMenuDTO.setSize(DEFAULT_PAGE_SIZE);
            }
            return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, smMenuService.listForPage(smMenuDTO));
        } catch (Exception e) {
            log.error("[SmMenuResource Class] listForPage:分页查询菜单接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 查询指定菜单
     * get：查询指定菜单 2017/6/8 接口测试完成
     *
     * @param smMenuDTO
     * @return
     */
    @RequestMapping(value = "/api/system/menu/get", method = RequestMethod.POST)
    public ResponseDTO<SmMenu> get(@RequestBody SmMenuDTO smMenuDTO) {
        try {
            if (smMenuDTO.getId() == null || ("").equals(smMenuDTO.getId())) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_POST_ID, null);
            }
            return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, smMenuService.get(smMenuDTO));
        } catch (Exception e) {
            log.error("[SmMenuResource Class] get:查询指定菜单接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 保存菜单接口
     * save：保存菜单接口（新增和修改功能） 2017/6/8 接口测试完成
     *
     * @param smMenuDTO
     * @return
     */
    @RequestMapping(value = "/api/system/menu/save", method = RequestMethod.POST)
    public ResponseDTO<Object> save(@RequestBody SmMenuDTO smMenuDTO) {
        try {
            if (("").equals(smMenuDTO.getMenuName()) || smMenuDTO.getMenuName() == null) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_MENU_MENU_NAME, null);
            }
            if (smMenuDTO.getOrderCode() == null || ("").equals(smMenuDTO.getOrderCode())) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_MENU_ORDER_CODE, null);
            }
            if (smMenuDTO.getMenuType() == null ){
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_MENU_TYPE, null);
            }
            if (StringUtils.isEmpty(smMenuDTO.getMenuCode())){
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_MENU_CODE, null);
            }
            //严重菜单编码是否重复
            Long count = (smMenuDTO.getId() == null?smMenuRepository.createCount(smMenuDTO.getMenuCode()):
                    smMenuRepository.updateCountByCode(smMenuDTO.getMenuCode(),smMenuDTO.getId()));
            if (count >0l){
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_MENU_CODE_EXIT_ERROR, null);
            }

            if (smMenuDTO.getId() == null || ("").equals(smMenuDTO.getId())) {
                //新增
                String msg = smMenuService.createdData(smMenuDTO);
                if (StringUtils.isNotEmpty(msg)){
                    return fail(msg);
                }else {
                    return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, null);
                }
            } else {
                //修改
                if (smMenuDTO.getpId() != null){
                    if (smMenuDTO.getpId().longValue() == smMenuDTO.getId().longValue()){
                        return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_MENU_UPDATE_ID_EQUALS_PID, null);
                    }
                }
                String msg = smMenuService.updateData(smMenuDTO);
                if (StringUtils.isNotEmpty(msg)){
                    return fail(msg);
                }else {
                    return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, null);
                }
            }
        } catch (Exception e) {
            log.error("[SmMenuResource Class] save:保存菜单接口（新增和修改功能）接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 删除菜单接口
     * del：删除菜单接口 （删除菜单后删除菜单角色中间表数据） 2017/6/8 接口测试完成
     *
     * @param smMenuDTO
     * @return
     */
    @RequestMapping(value = "/api/system/menu/del", method = RequestMethod.POST)
    public ResponseDTO<Object> del(@RequestBody SmMenuDTO smMenuDTO) {
        try {
            if (smMenuDTO.getId() == null || ("").equals(smMenuDTO.getId())) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_MENU_ID, null);
            } else {
                if (smMenuService.checkDataToPid(smMenuDTO)){
                    smMenuService.del(smMenuDTO);
                    return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, null);
                }else{
                    return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_MENU_DEL_DATA_PID, null);
                }

            }
        } catch (Exception e) {
            log.error("[SmMenuResource Class] del:删除菜单接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

}
