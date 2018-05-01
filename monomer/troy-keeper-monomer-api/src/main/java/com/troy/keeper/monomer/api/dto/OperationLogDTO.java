package com.troy.keeper.monomer.api.dto;

import com.troy.keeper.core.base.dto.BaseDTO;

/**
 * Created by yg on 2017/4/7.
 */
public class OperationLogDTO extends BaseDTO {
    private Long id;
    private Long userId;
    private String userName;
    private String operationType;
    private String operationTypeValue;
    private String operationTypeValueDesc;
    private String operationContent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOperationType() {
        return operationType;
    }

    public String getOperationTypeValue() {
        return operationTypeValue;
    }

    public void setOperationTypeValue(String operationTypeValue) {
        this.operationTypeValue = operationTypeValue;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getOperationTypeValueDesc() {
        return operationTypeValueDesc;
    }

    public void setOperationTypeValueDesc(String operationTypeValueDesc) {
        this.operationTypeValueDesc = operationTypeValueDesc;
    }

    public String getOperationContent() {
        return operationContent;
    }

    public void setOperationContent(String operationContent) {
        this.operationContent = operationContent;
    }
}
