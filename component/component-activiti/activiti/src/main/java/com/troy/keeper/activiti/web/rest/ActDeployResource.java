package com.troy.keeper.activiti.web.rest;

import com.troy.keeper.activiti.dto.QueryDeployResourceDTO;
import com.troy.keeper.activiti.dto.QueryDeploymentDTO;
import com.troy.keeper.activiti.dto.param.ActProcessParams;
import com.troy.keeper.activiti.service.ActDeployService;
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
 * discription: 部署资源管理 <br/>
 * Date:     2017/6/1 16:09<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
@Controller
public class ActDeployResource extends BaseResource<BaseDTO> {
    private Logger log = LoggerFactory.getLogger(ActDeployResource.class);

    @Autowired
    private ActDeployService actDeployService;

    /**
     * 新增部署包接口
     */
    @RequestMapping(value = "/api/actResource/deploy", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO<String> deploy(@RequestParam("file") MultipartFile file, @RequestParam("fileName") String fileName, @RequestParam("name") String name, @RequestParam("category") String category) throws Exception {
        try {
            if (null == file) {
                return new ResponseDTO<>("202", "文件为空", null);
            }
            if (StringUtils.isEmpty(fileName)) {
                return new ResponseDTO<>("202", "文件名称为空", null);
            }
            if (StringUtils.isEmpty(name)) {
                return new ResponseDTO<>("202", "流程名称为空", null);
            }
            String msg = actDeployService.deploy(file, fileName, name, category);

            if (!StringUtils.isEmpty(msg)) {
                return new ResponseDTO<>("201", msg, msg);
            }
            return new ResponseDTO<>("200", "部署成功", null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    /**
     * 删除部署包接口
     */
    @RequestMapping(value = "/api/actResource/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO<String> delete(@RequestBody ActProcessParams param) throws Exception {
        try {
            if (StringUtils.isEmpty(param.getDeployId())) {
                return new ResponseDTO<>("202", "部署ID为空", null);
            }
            actDeployService.delete(param);
            return new ResponseDTO<>("200", "删除成功", null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    /**
     * 查询部署包列表接口
     */
    @RequestMapping(value = "/api/actResource/queryDeployments", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO<List<QueryDeploymentDTO>> queryDeployments(@RequestBody ActProcessParams param) throws Exception {
        try {
            List<QueryDeploymentDTO> queryDeployments = actDeployService.queryDeployments(param);
            return new ResponseDTO<>("200", "查询成功", queryDeployments);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    /**
     * 查询部署包列表接口 分页
     */
    @RequestMapping(value = "/api/actResource/queryDeploymentsPage", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO<Page<QueryDeploymentDTO>> queryDeploymentsPage(@RequestBody ActProcessParams param, Pageable pageable) throws Exception {
        try {
            if(pageable == null){
                return getResponseDTO("202", "请求参数分页参数错误", null);
            }
            Page<QueryDeploymentDTO> queryDeployments = actDeployService.queryDeploymentsPage(param, pageable);
            return new ResponseDTO<>("200", "查询成功", queryDeployments);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    /**
     * 查询部署包资源接口
     */
    @RequestMapping(value = "/api/actResource/queryDeployResource", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO<List<QueryDeployResourceDTO>> queryDeployResource(@RequestBody ActProcessParams param) throws Exception {
        try {
            if (StringUtils.isEmpty(param.getDeployId())) {
                return new ResponseDTO<>("202", "部署ID为空", null);
            }
            List<QueryDeployResourceDTO> deployResources = actDeployService.queryDeployResource(param);
            return new ResponseDTO<>("200", "查询成功", deployResources);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    /**
     * 下载部署资源接口
     */
    @RequestMapping(value = "/api/actResource/downloadResource", method = RequestMethod.GET)
    public void downloadResource(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            actDeployService.downloadResource(request, response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
