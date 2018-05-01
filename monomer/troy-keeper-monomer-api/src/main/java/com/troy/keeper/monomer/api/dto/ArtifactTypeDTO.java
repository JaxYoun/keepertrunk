package com.troy.keeper.monomer.api.dto;

import com.troy.keeper.core.base.dto.BaseDTO;

/**
 * Created by yg on 2017/4/6.
 */
public class ArtifactTypeDTO extends BaseDTO {
    private Long id;
    private String artifactTypeCode;
    private String artifactTypeName;
    private String artifactTypeDesc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArtifactTypeCode() {
        return artifactTypeCode;
    }

    public void setArtifactTypeCode(String artifactTypeCode) {
        this.artifactTypeCode = artifactTypeCode;
    }

    public String getArtifactTypeName() {
        return artifactTypeName;
    }

    public void setArtifactTypeName(String artifactTypeName) {
        this.artifactTypeName = artifactTypeName;
    }

    public String getArtifactTypeDesc() {
        return artifactTypeDesc;
    }

    public void setArtifactTypeDesc(String artifactTypeDesc) {
        this.artifactTypeDesc = artifactTypeDesc;
    }
}
