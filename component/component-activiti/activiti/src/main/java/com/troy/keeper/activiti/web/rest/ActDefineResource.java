package com.troy.keeper.activiti.web.rest;

import com.troy.keeper.activiti.dto.QueryProcDefineDTO;
import com.troy.keeper.activiti.dto.param.ActProcessParams;
import com.troy.keeper.activiti.service.ActDefineService;
import com.troy.keeper.core.base.dto.BaseDTO;
import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.base.rest.BaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * discription: 流程定义管理 <br/>
 * Date:     2017/6/1 16:09<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
@RestController
public class ActDefineResource extends BaseResource<BaseDTO> {
    private Logger log = LoggerFactory.getLogger(ActDefineResource.class);

    @Autowired
    private ActDefineService actDefineService;

    /**
     * 激活流程定义接口
     */
    @RequestMapping(value = "/api/actDefine/activate", method = RequestMethod.POST)
    public ResponseDTO<String> activate(@RequestBody ActProcessParams param) throws Exception {
        try {
            if (StringUtils.isEmpty(param.getProcDefId())) {
                return new ResponseDTO<>("202", "流程定义ID为空", null);
            }
            actDefineService.activate(param);
            return new ResponseDTO<>("200", "激活成功", null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    /**
     * 挂起流程定义接口
     */
    @RequestMapping(value = "/api/actDefine/suspend", method = RequestMethod.POST)
    public ResponseDTO<String> suspend(@RequestBody ActProcessParams param) throws Exception {
        try {
            if (StringUtils.isEmpty(param.getProcDefId())) {
                return new ResponseDTO<>("202", "流程定义ID为空", null);
            }
            actDefineService.suspend(param);
            return new ResponseDTO<>("200", "挂起成功", null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return getResponseDTO("201", "系统内部错误", null);
        }
    }


    /**
     * 查询流程定义列表接口
     */
    @RequestMapping(value = "/api/actDefine/queryProcess", method = RequestMethod.POST)
    public ResponseDTO<List<QueryProcDefineDTO>> queryProcess(@RequestBody ActProcessParams param) throws Exception {
        try {
            List<QueryProcDefineDTO> queryProcesss = actDefineService.queryProcess(param);

            return new ResponseDTO<>("200", "查询成功", queryProcesss);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    /**
     * 查询流程定义列表接口 分页
     */
    @RequestMapping(value = "/api/actDefine/queryProcessPage", method = RequestMethod.POST)
    public ResponseDTO<Page<QueryProcDefineDTO>> queryProcessPage(@RequestBody ActProcessParams param, Pageable pageable) throws Exception {
        try {
            if(pageable == null){
                return getResponseDTO("202", "请求参数分页参数错误", null);
            }
            Page<QueryProcDefineDTO> queryProcesss = actDefineService.queryProcessPage(param, pageable);

            return new ResponseDTO<>("200", "查询成功", queryProcesss);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return getResponseDTO("201", "系统内部错误", null);
        }
    }
}
