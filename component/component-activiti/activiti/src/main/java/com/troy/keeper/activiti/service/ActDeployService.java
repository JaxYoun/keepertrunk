package com.troy.keeper.activiti.service;

import com.troy.keeper.activiti.dto.QueryDeployResourceDTO;
import com.troy.keeper.activiti.dto.QueryDeploymentDTO;
import com.troy.keeper.activiti.dto.param.ActProcessParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * discription: 部署资源管理接口 <br/>
 * Date:     2017/6/2 15:50<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public interface ActDeployService {
    String deploy(MultipartFile file, String fileName, String name, String category) throws Exception;

    void delete(ActProcessParams param) throws Exception;

    List<QueryDeploymentDTO> queryDeployments(ActProcessParams param) throws Exception;

    Page<QueryDeploymentDTO> queryDeploymentsPage(ActProcessParams param, Pageable pageable) throws Exception;

    List<QueryDeployResourceDTO> queryDeployResource(ActProcessParams param) throws Exception;

    void downloadResource(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
