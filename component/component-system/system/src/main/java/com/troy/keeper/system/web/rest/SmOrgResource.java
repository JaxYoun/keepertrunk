package com.troy.keeper.system.web.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.base.rest.BaseResource;
import com.troy.keeper.system.config.Const;
import com.troy.keeper.system.domain.SmOrg;
import com.troy.keeper.system.dto.SmOrgDTO;
import com.troy.keeper.system.service.SmOrgService;
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
 * Created by SimonChu on 2017/5/25.
 * 【SmOrg】机构管理接口层 2017/6/7 单元测试完成
 */
@RestController
public class SmOrgResource extends BaseResource<SmOrgDTO> {

    private final Logger log = LoggerFactory.getLogger(SmOrgResource.class);

    @Autowired
    private SmOrgService smOrgService;

    // 默认分页条数
    private static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * 查询组织机构列表
     * list：查询组织机构列表接口 2017/6/7 接口测试完成
     *
     * @return
     */
    @RequestMapping(value = "/api/system/org/list", method = RequestMethod.POST)
    public ResponseDTO<List<SmOrg>> list(HttpServletRequest httpServletRequest) {
        try {
            return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, smOrgService.list(httpServletRequest));
        } catch (Exception e) {
            log.error("[SmOrgResource Class] list:查询组织机构列表接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 分页查询组织结构列表
     * listForPage:分页查询组织机构列表接口 2017/6/7 接口测试完成
     *
     * @param smOrgDTO
     * @return
     */
    @RequestMapping(value = "/api/system/org/listForPage", method = RequestMethod.POST)
    public ResponseDTO<Page<SmOrg>> listForPage(@RequestBody SmOrgDTO smOrgDTO) {
        try {
            if (smOrgDTO.getPage() == 0) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_ORG_PAGE, null);
            } else {
                smOrgDTO.setPage(smOrgDTO.getPage() - 1);
            }
            if (smOrgDTO.getSize() == 0) {
                smOrgDTO.setSize(DEFAULT_PAGE_SIZE);
            }
            return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, smOrgService.listForPage(smOrgDTO));
        } catch (Exception e) {
            log.error("[SmOrgResource Class] listForPage:分页查询组织机构列表接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 查询指定组织机构
     * get：查询指定组织机构接口 2017/6/7 接口测试完成
     *
     * @param smOrgDTO
     * @return
     */
    @RequestMapping(value = "/api/system/org/get", method = RequestMethod.POST)
    public ResponseDTO<SmOrg> get(@RequestBody SmOrgDTO smOrgDTO) {
        try {
            if (smOrgDTO.getId() == null || ("").equals(smOrgDTO.getId())) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_ORG_ID, null);
            }
            return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, smOrgService.get(smOrgDTO));
        } catch (Exception e) {
            log.error("[SmOrgResource Class] get:查询指定组织机构接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 机构保存接口
     * save:机构保存接口(新增和修改功能) 2017/6/7 接口测试完成
     *
     * @param smOrgDTO
     * @return
     */
    @RequestMapping(value = "/api/system/org/save", method = RequestMethod.POST)
    public ResponseDTO save(@RequestBody SmOrgDTO smOrgDTO) {
        try {
            if (("").equals(smOrgDTO.getOrgName()) || smOrgDTO.getOrgName() == null) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_ORG_ORG_NAME, null);
            }
            if (smOrgDTO.getOrderCode() == null || ("").equals(smOrgDTO.getOrderCode())) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_ORG_ORDER_CODE, null);
            }
            if (smOrgDTO.getId() == null || ("").equals(smOrgDTO.getId())) {
                //新增
                smOrgService.createdData(smOrgDTO);
                return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, null);
            } else {
                //修改
                if (smOrgDTO.getpId() != null) {
                    if (smOrgDTO.getId().longValue() == smOrgDTO.getpId().longValue()) {
                        return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_ORG_UPDATE_PID_EQUALS_ID, null);
                    }
                }
                smOrgService.updateData(smOrgDTO);
                return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, null);
            }
        } catch (Exception e) {
            log.error("[SmOrgResource Class] save:机构保存接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

    /**
     * 组织机构删除
     * del：组织机构删除接口 2017/6/7 接口测试完成
     *
     * @param smOrgDTO
     * @return
     */
    @RequestMapping(value = "/api/system/org/del", method = RequestMethod.POST)
    public ResponseDTO del(@RequestBody SmOrgDTO smOrgDTO) {
        try {
            if (smOrgDTO.getId() == null || ("").equals(smOrgDTO.getId())) {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_ORG_ID, null);
            }
            // 检查该机构是否存在用户关联数据
            if (!smOrgService.checkOrgAndUser(smOrgDTO)){
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_ORG_DEL_USER, null);
            }
            // 检查改ID是否有子数据有关的数据
            if (smOrgService.checkPidData(smOrgDTO)) {
                smOrgService.del(smOrgDTO);
                return getResponseDTO(Const.CODE_200, Const.MSG_CODE_200, null);
            } else {
                return getResponseDTO(Const.CODE_202, Const.MSG_CODE_202_ORG_DEL_DATA_PID, null);
            }
        } catch (Exception e) {
            log.error("[SmOrgResource Class] del:组织机构删除接口异常", e);
            return getResponseDTO(Const.CODE_201, Const.MSG_CODE_201, null);
        }
    }

}
