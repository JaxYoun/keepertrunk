package com.troy.keeper.activiti.utils.bpm.support;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qjq
 * @version V1.0
 * @Title: EdgeDTO
 * @Description:
 * @Company: troy
 * @date 2016年6月14日上午10:11:44
 */
public class EdgeDTO {
    private String id;

    private List<List<Integer>> g = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<List<Integer>> getG() {
        return g;
    }

    public void setG(List<List<Integer>> g) {
        this.g = g;
    }
}
