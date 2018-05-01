package com.troy.keeper.fileupload.component;

import com.troy.keeper.core.error.KeeperException;
import com.troy.keeper.fileupload.component.impl.FtpFileUpload;
import com.troy.keeper.fileupload.component.impl.SftpFileUpload;
import com.troy.keeper.fileupload.config.UploadPropertiesBean;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * Created by Administrator on 2017/6/22.
 */
public class FileUploadFactory extends BasePooledObjectFactory<IFileUpload> {
    private static final String TYPE_SFTP = "sftp";
    private static final String TYPE_FTP = "ftp";
    private UploadPropertiesBean configBean;

    public void setConfigBean(UploadPropertiesBean configBean) {
        this.configBean = configBean;
    }

    @Override
    public IFileUpload create()  {
        if(configBean.getType().equals(TYPE_SFTP)){
            return new SftpFileUpload(configBean);
        }else if(configBean.getType().equals(TYPE_FTP)){
            return new FtpFileUpload(configBean);
        }
        return null ;

    }

    @Override
    public PooledObject<IFileUpload> wrap(IFileUpload iFileUpload) {
        return new DefaultPooledObject<>(iFileUpload);
    }
}
