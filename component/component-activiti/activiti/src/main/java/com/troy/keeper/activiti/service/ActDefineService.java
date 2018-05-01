package com.troy.keeper.activiti.service;

import com.troy.keeper.activiti.dto.QueryProcDefineDTO;
import com.troy.keeper.activiti.dto.param.ActProcessParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * discription: 流程定义管理接口 <br/>
 * Date:     2017/6/2 15:50<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public interface ActDefineService {
    void activate(ActProcessParams param) throws Exception;

    void suspend(ActProcessParams param) throws Exception;

    List<QueryProcDefineDTO> queryProcess(ActProcessParams param) throws Exception;

    Page<QueryProcDefineDTO> queryProcessPage(ActProcessParams param, Pageable pageable) throws Exception;
}
