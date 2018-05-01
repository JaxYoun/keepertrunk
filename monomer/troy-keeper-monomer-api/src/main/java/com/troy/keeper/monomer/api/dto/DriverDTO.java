package com.troy.keeper.monomer.api.dto;

import com.troy.keeper.core.base.dto.BaseDTO;


/**
 * Created by alexander on 2017/4/7.
 */
public class DriverDTO extends BaseDTO {
    private Long id;

    private String driverName;
    private String driverUseName;
    private String driverDesc;
    private String driverVersion;
    private String driverJar="";
    private String driverClassName;
    private String databaseTypeName="cdap-pack.driver.databaseType";
    private String databaseType;//值00,01
    private String databaseTypeDesc;//mysql,oracle
    private String driverTypeName="cdap-pack.driver.driverType";
    private String driverType;//值00,01
    private String driverTypeDesc;//jdbc,odbc


    public String getDriverUseName() {
        return driverUseName;
    }

    public void setDriverUseName(String driverUseName) {
        this.driverUseName = driverUseName;
    }

    public String getDriverJar() {
        return driverJar;
    }

    public void setDriverJar(String driverJar) {
        this.driverJar = driverJar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverDesc() {
        return driverDesc;
    }

    public void setDriverDesc(String driverDesc) {
        this.driverDesc = driverDesc;
    }

    public String getDriverVersion() {
        return driverVersion;
    }

    public void setDriverVersion(String driverVersion) {
        this.driverVersion = driverVersion;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public String getDatabaseTypeDesc() {
        return databaseTypeDesc;
    }

    public void setDatabaseTypeDesc(String databaseTypeDesc) {
        this.databaseTypeDesc = databaseTypeDesc;
    }

    public String getDriverTypeDesc() {
        return driverTypeDesc;
    }

    public void setDriverTypeDesc(String driverTypeDesc) {
        this.driverTypeDesc = driverTypeDesc;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getDriverType() {
        return driverType;
    }

    public void setDriverType(String driverType) {
        this.driverType = driverType;
    }

    public String getDatabaseTypeName() {
        return databaseTypeName;
    }

    public void setDatabaseTypeName(String databaseTypeName) {
        this.databaseTypeName = databaseTypeName;
    }

    public String getDriverTypeName() {
        return driverTypeName;
    }

    public void setDriverTypeName(String driverTypeName) {
        this.driverTypeName = driverTypeName;
    }
}
