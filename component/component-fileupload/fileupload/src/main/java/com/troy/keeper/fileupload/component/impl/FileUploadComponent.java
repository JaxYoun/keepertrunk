package com.troy.keeper.fileupload.component.impl;

import com.troy.keeper.core.error.KeeperException;
import com.troy.keeper.fileupload.component.FileUploadFactory;
import com.troy.keeper.fileupload.component.IFileUpload;
import com.troy.keeper.fileupload.config.UploadPropertiesBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2017/6/22.
 */
@Component
public class FileUploadComponent implements IFileUpload{
    private static final Log log = LogFactory.getLog(FileUploadComponent.class);
    @Autowired
    private UploadPropertiesBean uploadProperties;


    private ObjectPool<IFileUpload> pool;
    @PostConstruct
    public void init(){
        FileUploadFactory factory = new FileUploadFactory();
        factory.setConfigBean(uploadProperties);
        pool = new GenericObjectPool<IFileUpload>(factory);
        log.info("连接初始化成功！！");
    }
    @Override
    public void uploadFile(File file, String newName) throws KeeperException {
        IFileUpload upload =null;
        try{
            upload = pool.borrowObject();
            upload.cdRoot();
            upload.uploadFile(file,newName);
        }catch (Exception e){
                 throw  new KeeperException(e);
        }finally {
            try {
                if(upload != null)
                pool.returnObject(upload);
            } catch (Exception e) {
                throw  new KeeperException(e);
            }
        }

    }

    @Override
    public void uploadFile(InputStream in, String newName) throws KeeperException {
        IFileUpload upload = null;
        try{
            upload = pool.borrowObject();
            upload.cdRoot();
            upload.uploadFile(in,newName);
        }catch (Exception e){
            throw  new KeeperException(e);
        }finally {
            try {
                if(upload != null)
                 pool.returnObject(upload);
            } catch (Exception e) {
                throw  new KeeperException(e);
            }
        }
    }

    @Override
    public <T extends OutputStream> void downLoadFile(String fileName, T t) throws KeeperException {
        IFileUpload upload = null;
        try{
            upload = pool.borrowObject();
            upload.cdRoot();
            upload.downLoadFile(fileName,t);
        }catch (Exception e){
            throw  new KeeperException(e);
        }finally {
            try {
                if(upload != null)
                pool.returnObject(upload);
            } catch (Exception e) {
                throw  new KeeperException(e);
            }
        }
    }

    @Override
    public <T extends OutputStream> void downLoadFilePart(String fileName, T t, long start, long end) throws KeeperException {
        IFileUpload upload = null;
        try{
            upload = pool.borrowObject();
            upload.cdRoot();
            upload.downLoadFilePart(fileName,t,start,end);
        }catch (Exception e){
            throw  new KeeperException(e);
        }finally {
            try {
                if(upload != null)
                pool.returnObject(upload);
            } catch (Exception e) {
                throw  new KeeperException(e);
            }
        }
    }

    @Override
    public void delFile(String fileName) throws KeeperException {
        IFileUpload upload = null;
        try{
            upload = pool.borrowObject();
            upload.cdRoot();
            upload.delFile(fileName);
        }catch (Exception e){
            throw  new KeeperException(e);
        }finally {
            try {
                if(upload != null)
                pool.returnObject(upload);
            } catch (Exception e) {
                throw  new KeeperException(e);
            }
        }
    }

    @Override
    public InputStream getInputStream(String fileName) throws KeeperException {
        IFileUpload upload = null;
        try{
            upload = pool.borrowObject();
            upload.cdRoot();
            return upload.getInputStream(fileName);
        }catch (Exception e){
            throw  new KeeperException(e);
        }finally {
            try {
                if(upload != null)
                pool.returnObject(upload);
            } catch (Exception e) {
                throw  new KeeperException(e);
            }
        }
    }

    @Override
    public void cdRoot() {

    }

    @Override
    public void copyFile(String fileName, String newName) {
        IFileUpload upload = null;
        try{
            upload = pool.borrowObject();
            upload.cdRoot();
            upload.copyFile(fileName,newName);
        }catch (Exception e){
            throw  new KeeperException(e);
        }finally {
            try {
                if(upload != null)
                    pool.returnObject(upload);
            } catch (Exception e) {
                throw  new KeeperException(e);
            }
        }
    }
}
