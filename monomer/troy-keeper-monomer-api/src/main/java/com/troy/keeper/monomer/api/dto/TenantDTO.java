package com.troy.keeper.monomer.api.dto;

import com.troy.keeper.core.base.dto.BaseDTO;

import java.util.List;

/**
 * Created by yjm on 2017/4/7.
 */
public class TenantDTO extends BaseDTO {
    //租户ID
    private Long id;

    //租户名称
    private String tenantName;

    //principal名称
    private String principalName;

    private String keytabUriName;

    private String hdfsPath;

    private String hbaseNamespaceName;

    private String hiveDatabaseName;

    //初始参数配置
    private List<PreferenceDTO> preferenceList;

    //yarn队列名称
    private String yarnName;

    //描述
    private String memo;

    private String state;

    private String stateValue;

    private String namespaceName;

    public String getNamespaceName() {
        return namespaceName;
    }

    public void setNamespaceName(String namespaceName) {
        this.namespaceName = namespaceName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getKeytabUriName() {
        return keytabUriName;
    }

    public void setKeytabUriName(String keytabUriName) {
        this.keytabUriName = keytabUriName;
    }

    public String getHdfsPath() {
        return hdfsPath;
    }

    public void setHdfsPath(String hdfsPath) {
        this.hdfsPath = hdfsPath;
    }

    public String getHbaseNamespaceName() {
        return hbaseNamespaceName;
    }

    public void setHbaseNamespaceName(String hbaseNamespaceName) {
        this.hbaseNamespaceName = hbaseNamespaceName;
    }

    public String getHiveDatabaseName() {
        return hiveDatabaseName;
    }

    public void setHiveDatabaseName(String hiveDatabaseName) {
        this.hiveDatabaseName = hiveDatabaseName;
    }

    public List<PreferenceDTO> getPreferenceList() {
        return preferenceList;
    }

    public void setPreferenceList(List<PreferenceDTO> preferenceList) {
        this.preferenceList = preferenceList;
    }

    public String getYarnName() {
        return yarnName;
    }

    public void setYarnName(String yarnName) {
        this.yarnName = yarnName;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateValue() {
        return stateValue;
    }

    public void setStateValue(String stateValue) {
        this.stateValue = stateValue;
    }
}
