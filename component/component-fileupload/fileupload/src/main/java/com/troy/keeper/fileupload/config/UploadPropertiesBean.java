package com.troy.keeper.fileupload.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Properties specific to JHipster.
 *
 * <p>
 *     Properties are configured in the application.yml file.
 * </p>
 */
@Component
public class UploadPropertiesBean {
    @Value("${upload.ip}")
    private String ip;
    @Value("${upload.userName}")
    private String userName;
    @Value("${upload.password}")
    private String password;
    @Value("${upload.port}")
    private String port;
    @Value("${upload.type}")
    private String type;
    @Value("${upload.path}")
    private  String path;
    @Value("${upload.downLoadUrl}")
    private String  downLoadUrl;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return Integer.parseInt(port);
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDownLoadUrl() {
        return downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }
}
