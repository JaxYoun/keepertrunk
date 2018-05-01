package com.troy.keeper.fileupload.service.impl;

import com.troy.keeper.core.base.service.BaseServiceImpl;
import com.troy.keeper.core.error.KeeperException;
import com.troy.keeper.core.utils.object.ObjectUtil;
import com.troy.keeper.fileupload.component.IFileUpload;
import com.troy.keeper.fileupload.domain.AttachFile;
import com.troy.keeper.fileupload.domain.BatchFile;
import com.troy.keeper.fileupload.domain.FileType;
import com.troy.keeper.fileupload.dto.AttachFileDTO;
import com.troy.keeper.fileupload.dto.BatchFileDTO;
import com.troy.keeper.fileupload.dto.UploadFileDTO;
import com.troy.keeper.fileupload.repository.AttachFileRepository;
import com.troy.keeper.fileupload.repository.BatchFileRepository;
import com.troy.keeper.fileupload.repository.FileTypeRepository;
import com.troy.keeper.fileupload.service.UploadService;
import com.troy.keeper.fileupload.service.mapper.AttachFileMapper;
import com.troy.keeper.fileupload.service.mapper.BatchFileMapper;
import com.troy.keeper.fileupload.type.Whether;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

/**
 * Created by SimonChu on 2017/6/1.
 */
@Service
@Transactional
public class UploadServiceImpl extends BaseServiceImpl<AttachFile, UploadFileDTO> implements UploadService {
    private static final Log log = LogFactory.getLog(UploadServiceImpl.class);

    @Autowired
    private BatchFileRepository batchFileRepository;
    @Autowired
    private AttachFileRepository attachFileRepository;
    @Autowired
    private IFileUpload iFileUpload;
    @Autowired
    private AttachFileMapper attachFileMapper;
    @Autowired
    private BatchFileMapper batchFileMapper;
    @Autowired
    private FileTypeRepository fileTypeRepository;
    @Override
    public BatchFileDTO uploadFiles(UploadFileDTO uploadFileDTO) throws KeeperException {
        //判断有没有批次id
        try{
            BatchFile batchFile = null;
            FileType fileType = fileTypeRepository.findOne(uploadFileDTO.getFileTypeId());
            if(uploadFileDTO.getBatchId() == null || uploadFileDTO.getBatchId() == 0l){
                batchFile = new BatchFile();
                batchFile.setFileType(fileType);
                batchFile = batchFileRepository.save(batchFile);
            }else{
                batchFile = batchFileRepository.findOne(uploadFileDTO.getBatchId());

            }
            batchFile.setFileType(fileType);
            BatchFile batchFile1 = batchFile;
            uploadFileDTO.getFileMap().forEach((k,v)->{
                uploadFile(v,batchFile1);
            });
            batchFile = batchFileRepository.save(batchFile1);
            return batchFileMapper.objToDTO(batchFile);
        }catch (Exception e){
            e.printStackTrace();
            throw new KeeperException(e);
        }
    }

    @Override
    public void delFileById(Long id) {
        AttachFile attachFile = attachFileRepository.findOne(id);
        if(attachFile == null)
            return;
        iFileUpload.delFile(attachFile.getFilePath());
        attachFileRepository.delete(id);
    }

    @Override
    public void delFileByBatchId(Long id) {
        BatchFile batchFile = batchFileRepository.findOne(id);
        batchFile.getFileList().forEach((AttachFile attachFile)->{
            iFileUpload.delFile(attachFile.getFilePath());
        });
        batchFileRepository.delete(batchFile);
    }

    @Override
    public  void downloadFile(Long id, HttpServletResponse response) throws IOException {

        AttachFile attachFile = attachFileRepository.findOne(id);
        if(attachFile == null)
            return;
        response.reset();// 必须加，不然保存不了临时文件
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;  filename="+ URLEncoder.encode(attachFile.getFileName(), "UTF-8")  );
        iFileUpload.downLoadFile(attachFile.getFilePath(),response.getOutputStream());
    }

    @Override
    public void downloadFile(Long id, OutputStream outputStream) throws IOException {
        AttachFile attachFile = attachFileRepository.findOne(id);
        if(attachFile == null){
            return;
        }
        iFileUpload.downLoadFile(attachFile.getFilePath(),outputStream);
    }

    @Override
    public AttachFileDTO queryFileById(Long id) {
        return attachFileMapper.objToDTO(attachFileRepository.findOne(id));
    }

    @Override
    public List<AttachFileDTO> queryFileByBatchId(Long id) {
        return attachFileMapper.objsToDTOs(attachFileRepository.findAll(  (root, query, cb) -> query.where(cb.equal(root.get("bathId").as(Long.class),id)).getRestriction()));
    }

    @Override
    public Page<AttachFileDTO> queryFileByFileTypeId(AttachFileDTO fileDTO) {
//        Page<AttachFile> filePage =  attachFileRepository.quyerFileByFileTypeId(fileDTO.getId(),new PageRequest(fileDTO.getPage(),fileDTO.getSize()));
//        return filePage.map((AttachFile a)->attachFileMapper.objToDTO(a));
        return  null;
    }

    @Override
    public BatchFileDTO queryBatch(Long id) {
        return batchFileMapper.objToDTO(batchFileRepository.findOne(id));
    }

    @Override
    public AttachFileDTO copyFile(Long id) {
        AttachFile a =  attachFileRepository.findOne(id);
        AttachFile attachFile = ObjectUtil.clone(a,AttachFile.class);
        attachFile.setId(null);
        attachFile.setFilePath(copyFile(attachFile));
        return attachFileMapper.objToDTO(attachFileRepository.save(attachFile));
    }

    private String   copyFile( AttachFile attachFile){
        String fileName = attachFile.getFilePath();
        String newFileName = fileName.substring(0,fileName.lastIndexOf("/")+1)+UUID.randomUUID()+fileName.substring(fileName.indexOf("."));
        iFileUpload.copyFile(fileName,newFileName);
        return newFileName;
    }

    @Override
    public BatchFileDTO copyBatchFiles(Long id) {
        BatchFile b = batchFileRepository.findOne(id);
        BatchFile batchFile = ObjectUtil.clone(b,BatchFile.class);
        batchFile.setId(null);
        for(AttachFile attachFile :batchFile.getFileList()){
            attachFile.setId(null);
            attachFile.setFilePath(copyFile(attachFile));
            attachFile.setBatchFile(batchFile);
        }
        return batchFileMapper.objToDTO(batchFileRepository.save(batchFile));
    }

    private void uploadFile(MultipartFile file,BatchFile batchFile) throws KeeperException{
        AttachFile attachFile = getAttachFile( file,  batchFile);
        try {
            iFileUpload.uploadFile(file.getInputStream(),attachFile.getFilePath());
            attachFile.setBatchFile(batchFile);
            batchFile.getFileList().add(attachFile);

        } catch (IOException e) {
           throw new KeeperException(e);
        }

    }

    private AttachFile getAttachFile(MultipartFile file,BatchFile batchFile){
        AttachFile attachFile = new AttachFile();
        attachFile.setFileSize(file.getSize());
        attachFile.setFileName(file.getOriginalFilename());
        attachFile.setIsEncryption(batchFile.getFileType().getIsEncryption());
        attachFile.setFilePath(getFilePath(batchFile.getFileType(), file));
        return attachFile;
    }

    private String getFilePath(FileType fileType,MultipartFile file){
        StringBuilder sb = new StringBuilder("/");
        sb.append(fileType.getCategoryCode());
        sb.append("/");
        sb.append( DateFormatUtils.format(System.currentTimeMillis(),"yyyyMMdd"));
        sb.append("/");
        if(fileType.getIsEncryption().equals(Whether.Y)){
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            sb.append(bCryptPasswordEncoder.encode(file.getName()));
        }else{
            String fileName = file.getOriginalFilename();
            sb.append(UUID.randomUUID());
            sb.append(".");
            sb.append(fileName.substring(fileName.indexOf(".")+1));
        }
        return sb.toString();
    }

}
