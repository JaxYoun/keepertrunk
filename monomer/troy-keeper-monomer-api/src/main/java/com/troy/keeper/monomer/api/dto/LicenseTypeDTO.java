package com.troy.keeper.monomer.api.dto;

import com.troy.keeper.core.base.dto.BaseDTO;

/**
 * Created by yg on 2017/4/11.
 */
public class LicenseTypeDTO extends BaseDTO{
    private Long id;
    private String licenseTypeCode;
    private String licenseTypeName;
    private String licenseSummary;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLicenseTypeCode() {
        return licenseTypeCode;
    }

    public void setLicenseTypeCode(String licenseTypeCode) {
        this.licenseTypeCode = licenseTypeCode;
    }

    public String getLicenseTypeName() {
        return licenseTypeName;
    }

    public void setLicenseTypeName(String licenseTypeName) {
        this.licenseTypeName = licenseTypeName;
    }

    public String getLicenseSummary() {
        return licenseSummary;
    }

    public void setLicenseSummary(String licenseSummary) {
        this.licenseSummary = licenseSummary;
    }
}
