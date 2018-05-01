package com.troy.keeper.fileupload.web.rest;


import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.base.rest.BaseResource;
import com.troy.keeper.core.error.KeeperException;

import com.troy.keeper.core.utils.validate.Validate;
import com.troy.keeper.fileupload.config.UploadPropertiesBean;
import com.troy.keeper.fileupload.dto.AttachFileDTO;
import com.troy.keeper.fileupload.dto.BatchFileDTO;
import com.troy.keeper.fileupload.dto.FileTypeDTO;
import com.troy.keeper.fileupload.dto.UploadFileDTO;
import com.troy.keeper.fileupload.service.UploadService;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Map;
import java.util.logging.Level;


@RestController
public class FileUploadResource extends BaseResource<FileTypeDTO> {
    private static final String MSG_ERROR_FILE = "文件未传入！！";
    private static final String MSG_ERROR_FILE_TYPE = "文件类型id未传入！！";
    private static final String MSG_ERROR_ID = "id未传入！！";
    @Autowired
    private UploadService uploadService;

    @Autowired
    private UploadPropertiesBean uploadPropertiesBean;
    @RequestMapping(value = "/api/upload/uploadFiles")
    public ResponseDTO<?> uploadFiles(HttpServletRequest request,UploadFileDTO uploadFileDTO) throws Exception {
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) (request);
        Map<String,MultipartFile> fileMap = mRequest.getFileMap();
        if(fileMap == null || fileMap.size() ==0){
            return  fail(MSG_ERROR_FILE);
        }
        if(uploadFileDTO.getFileTypeId() == null || uploadFileDTO.getFileTypeId() == 0L){
            return  fail(MSG_ERROR_FILE_TYPE);
        }
        uploadFileDTO.setFileMap(fileMap);
       return success(addBatchDownloadUrl(uploadService.uploadFiles(uploadFileDTO)));
    }
    @RequestMapping(value = "/api/upload/downloadFileById")
    public  void downloadFile(HttpServletResponse response,AttachFileDTO attachFileDTO){
        if(attachFileDTO.getId() == null || attachFileDTO.getId() == 0L){
            throw new KeeperException(MSG_ERROR_ID);
        }
        try {
            uploadService.downloadFile(attachFileDTO.getId(),response);
        } catch (IOException e) {
            throw new KeeperException(e);
        }
    }
    @RequestMapping(value = "/api/upload/delFileByAttachFileId")
    public  ResponseDTO<?> delFile(@RequestBody AttachFileDTO attachFileDTO){
        try {
            if (attachFileDTO.getId() == null || attachFileDTO.getId() == 0L){
                throw new KeeperException(MSG_ERROR_ID);
            }
            uploadService.delFileById(attachFileDTO.getId());
        }catch (Exception e){
            e.printStackTrace();
        }
        return  success();
    }
    @RequestMapping(value = "/api/upload/delFileByBatchId")
    public  ResponseDTO<?> delFileByBatchId(@RequestBody BatchFileDTO batchFileDTO){
        if(batchFileDTO.getId() == null || batchFileDTO.getId() == 0L){
            throw new KeeperException(MSG_ERROR_ID);
        }
        uploadService.delFileByBatchId(batchFileDTO.getId());
        return  success();
    }
    @RequestMapping(value = "/api/upload/queryFileByBatchId", method = RequestMethod.POST)
    public  ResponseDTO<?> queryBatchFileById(@RequestBody BatchFileDTO batchFileDTO){
        if(batchFileDTO.getId() == null || batchFileDTO.getId() == 0L){
            throw new KeeperException(MSG_ERROR_ID);
        }
        return  success(addBatchDownloadUrl(uploadService.queryBatch(batchFileDTO.getId())));
    }
    @RequestMapping(value = "/api/upload/copyFile", method = RequestMethod.POST)
    public  ResponseDTO<?> copyFile(@RequestBody AttachFileDTO attachFileDTO){
        if(attachFileDTO.getId() == null || attachFileDTO.getId() == 0L){
            throw new KeeperException(MSG_ERROR_ID);
        }
        return  success(addDownloadUrl(uploadService.copyFile(attachFileDTO.getId())));
    }
    @RequestMapping(value = "/api/upload/copyBatchFiles", method = RequestMethod.POST)
    public  ResponseDTO<?> copyBatchFiles(@RequestBody BatchFileDTO batchFileDTO){
        if(batchFileDTO.getId() == null || batchFileDTO.getId() == 0L){
            throw new KeeperException(MSG_ERROR_ID);
        }
        return  success(addBatchDownloadUrl(uploadService.copyBatchFiles(batchFileDTO.getId())));
    }
    private BatchFileDTO addBatchDownloadUrl(BatchFileDTO batchFileDTO){
        if(batchFileDTO.getFileList()!=null){
            batchFileDTO.getFileList().forEach((e)->{
                addDownloadUrl(e);
            });
        }
        return batchFileDTO;
    }

    private  AttachFileDTO addDownloadUrl(AttachFileDTO attachFileDTO){
        if(uploadPropertiesBean.getDownLoadUrl()!= null && uploadPropertiesBean.getDownLoadUrl().trim().length()>0){
                attachFileDTO.setFileUrl(uploadPropertiesBean.getDownLoadUrl()+attachFileDTO.getFilePath());
        }
        return attachFileDTO;
    }

}
