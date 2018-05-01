package com.troy.keeper.activiti.dto;

import com.troy.keeper.core.base.dto.BaseDTO;

/**
 * Date:     2017/6/7 15:42<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public class QueryDeployResourceDTO extends BaseDTO {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
