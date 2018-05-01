package com.troy.keeper.activiti.dto;

import com.troy.keeper.core.base.dto.BaseDTO;

/**
 * Date:     2017/6/7 15:42<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public class ActivityDTO extends BaseDTO {
    private String id;

    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        if(id != null){
            return id.hashCode();
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (this.getClass() != obj.getClass())
            return false;

        if(id != null){
            return id.equals(((ActivityDTO)obj).getId());
        }
        return super.equals(obj);
    }
}
