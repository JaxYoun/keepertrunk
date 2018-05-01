package com.troy.keeper.core.base.dto;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: xiongzhan
 * Date: 17-3-29
 * Time: 下午3:43
 * To change this template use File | Settings | File Templates.
 */
public class PageDTO extends BaseDTO {
    private int page = 0;
    private  int size = 20;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
