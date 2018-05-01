package com.troy.keeper.activiti.service;

import com.troy.keeper.activiti.dto.NewModelDTO;
import com.troy.keeper.activiti.dto.QueryModelDTO;
import com.troy.keeper.activiti.dto.param.ActModelParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * discription: 模型管理接口 <br/>
 * Date:     2017/6/2 15:50<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public interface ActModelService {
    NewModelDTO newModel(ActModelParams param) throws Exception;

    void delModel(ActModelParams param) throws Exception;

    List<QueryModelDTO> queryModel(ActModelParams param) throws Exception;

    Page<QueryModelDTO> queryModelPage(ActModelParams param, Pageable pageable) throws Exception;

    String importModel(MultipartFile file, String fileName) throws Exception;

    void exportModel(HttpServletRequest request, HttpServletResponse response) throws Exception;

    String deployModel(ActModelParams param) throws Exception;
}
