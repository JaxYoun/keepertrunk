package com.troy.keeper.activiti.web.rest;

import com.troy.keeper.activiti.dto.NewModelDTO;
import com.troy.keeper.activiti.dto.QueryModelDTO;
import com.troy.keeper.activiti.dto.param.ActModelParams;
import com.troy.keeper.activiti.service.ActModelService;
import com.troy.keeper.core.base.dto.BaseDTO;
import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.base.rest.BaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * discription: 模型管理 <br/>
 * Date:     2017/6/1 16:09<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
@Controller
public class ActModelResource extends BaseResource<BaseDTO> {
    private Logger log = LoggerFactory.getLogger(ActModelResource.class);

    @Autowired
    private ActModelService actModelService;

    /**
     * 新增模型接口
     */
    @RequestMapping(value = "/api/actModel/newModel", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO<NewModelDTO> newModel(@RequestBody ActModelParams param) throws Exception {
        try {
            if (StringUtils.isEmpty(param.getName())) {
                return new ResponseDTO<>("202", "模型名称为空", null);
            }
            NewModelDTO newModelDTO = actModelService.newModel(param);
            return new ResponseDTO<>("200", "新增成功", newModelDTO);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    /**
     * 删除模型接口
     */
    @RequestMapping(value = "/api/actModel/delModel", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO<String> delModel(@RequestBody ActModelParams param) throws Exception {
        try {
            if (StringUtils.isEmpty(param.getId())) {
                return new ResponseDTO<>("202", "模型ID为空", null);
            }
            actModelService.delModel(param);
            return new ResponseDTO<>("200", "删除成功", null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    /**
     * 查询模型信息接口
     */
    @RequestMapping(value = "/api/actModel/queryModel", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO<List<QueryModelDTO>> queryModel(@RequestBody ActModelParams param) throws Exception {
        try {
            List<QueryModelDTO> queryModelDTOS = actModelService.queryModel(param);
            return new ResponseDTO<>("200", "查询成功", queryModelDTOS);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    /**
     * 查询模型信息接口 分页
     */
    @RequestMapping(value = "/api/actModel/queryModelPage", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO<Page<QueryModelDTO>> queryModelPage(@RequestBody ActModelParams param, Pageable pageable) throws Exception {
        try {
            if(pageable == null){
                return getResponseDTO("202", "请求参数分页参数错误", null);
            }
            Page<QueryModelDTO> queryModelDTOS = actModelService.queryModelPage(param, pageable);
            return new ResponseDTO<>("200", "查询成功", queryModelDTOS);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    /**
     * 导入模型信息接口
     */
    @RequestMapping(value = "/api/actModel/import", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO<String> importModel(@RequestParam("file") MultipartFile file, @RequestParam("fileName") String fileName) throws Exception {
        try {
            if (null == file) {
                return new ResponseDTO<>("202", "文件为空", null);
            }
            if (StringUtils.isEmpty(fileName)) {
                return new ResponseDTO<>("202", "文件名为空", null);
            }
            String msg = actModelService.importModel(file, fileName);
            if (!StringUtils.isEmpty(msg)) {
                return new ResponseDTO<>("201", msg, msg);
            }
            return new ResponseDTO<>("200", "上传成功", null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    /**
     * 导出模型信息接口
     */
    @RequestMapping(value = "/api/actModel/export", method = RequestMethod.GET)
    public void exportModel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            actModelService.exportModel(request, response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 部署模型接口
     */
    @RequestMapping(value = "/api/actModel/deploy", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO<String> deployModel(@RequestBody ActModelParams param) throws Exception {
        try {
            if (StringUtils.isEmpty(param.getId())) {
                return new ResponseDTO<>("202", "模型ID为空", null);
            }
            String msg = actModelService.deployModel(param);
            if (!StringUtils.isEmpty(msg)) {
                return new ResponseDTO<>("201", msg, msg);
            }
            return new ResponseDTO<>("200", "部署模型成功", null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

}
