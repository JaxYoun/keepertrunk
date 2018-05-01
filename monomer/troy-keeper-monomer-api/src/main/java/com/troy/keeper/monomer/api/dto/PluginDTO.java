package com.troy.keeper.monomer.api.dto;

import com.troy.keeper.core.base.dto.BaseDTO;


/**
 * Created by alexander on 2017/4/7.
 */
public class PluginDTO extends BaseDTO {
    private  String artifactType;
    private  String artifactName;
    private  String artifactDesc;
    private  String artifactVersion;
    private  String artifactClassName;
    private  String artifactJar;

    public String getArtifactType() {
        return artifactType;
    }

    public void setArtifactType(String artifactType) {
        this.artifactType = artifactType;
    }

    public String getArtifactName() {
        return artifactName;
    }

    public void setArtifactName(String artifactName) {
        this.artifactName = artifactName;
    }

    public String getArtifactDesc() {
        return artifactDesc;
    }

    public void setArtifactDesc(String artifactDesc) {
        this.artifactDesc = artifactDesc;
    }

    public String getArtifactVersion() {
        return artifactVersion;
    }

    public void setArtifactVersion(String artifactVersion) {
        this.artifactVersion = artifactVersion;
    }

    public String getArtifactClassName() {
        return artifactClassName;
    }

    public void setArtifactClassName(String artifactClassName) {
        this.artifactClassName = artifactClassName;
    }

    public String getArtifactJar() {
        return artifactJar;
    }

    public void setArtifactJar(String artifactJar) {
        this.artifactJar = artifactJar;
    }
}
