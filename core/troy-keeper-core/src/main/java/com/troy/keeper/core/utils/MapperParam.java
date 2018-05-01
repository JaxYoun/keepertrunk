package com.troy.keeper.core.utils;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by yg on 2017/5/24.
 */
public class MapperParam<T> {
    private T obj;                                        //要转换的对象
    private List<T> list;                                 //要转换的对象list
    private Page<T> page;                                 //要转换的对象page
    private List<String> ignoreColumnList;              //忽略的字段列表
    private List<String> keyList;                        //redis的key列表(取list的key),
    private List<String> sourceColumnList;             //源column列表,顺序必须和keyList的顺序一致
    private List<String> userIdColumnList;             //userId的字段list

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public List<String> getIgnoreColumnList() {
        return ignoreColumnList;
    }

    public void setIgnoreColumnList(List<String> ignoreColumnList) {
        this.ignoreColumnList = ignoreColumnList;
    }

    public List<String> getKeyList() {
        return keyList;
    }

    public void setKeyList(List<String> keyList) {
        this.keyList = keyList;
    }

    public List<String> getSourceColumnList() {
        return sourceColumnList;
    }

    public void setSourceColumnList(List<String> sourceColumnList) {
        this.sourceColumnList = sourceColumnList;
    }

    public List<String> getUserIdColumnList() {
        return userIdColumnList;
    }

    public void setUserIdColumnList(List<String> userIdColumnList) {
        this.userIdColumnList = userIdColumnList;
    }

    public Page<T> getPage() {
        return page;
    }

    public void setPage(Page<T> page) {
        this.page = page;
    }
}
