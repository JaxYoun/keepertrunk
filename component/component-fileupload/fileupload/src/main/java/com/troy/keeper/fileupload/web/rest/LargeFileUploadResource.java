package com.troy.keeper.fileupload.web.rest;


import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.base.rest.BaseResource;
import com.troy.keeper.fileupload.dto.FileTypeDTO;
import com.troy.keeper.fileupload.dto.LargeFileDTO;
import com.troy.keeper.fileupload.util.MD5FileUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;


@RestController
public class LargeFileUploadResource extends BaseResource<FileTypeDTO> {
    private static final org.apache.commons.logging.Log log = LogFactory.getLog(LargeFileUploadResource.class);
    private static final String MSG_ERROR_FILE = "文件未传入！！";
    @Value("${upload.large.basePath}")
    private String basePath;
    @RequestMapping(value = "/api/upload/largeFile/upload")
    public ResponseDTO<?> uploadFiles(HttpServletRequest request, @RequestParam LargeFileDTO largeFileDTO) throws Exception {
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) (request);
        Map<String,MultipartFile> fileMap = mRequest.getFileMap();
        String filePath = basePath+largeFileDTO.getUuid();
        if(fileMap == null || fileMap.size() ==0){
            return  fail(MSG_ERROR_FILE);
        }
        File file = new File(filePath);
        if(!file.exists())
          FileUtils.forceMkdir(new File(filePath));
        for(MultipartFile f : fileMap.values()){
            File file1 = new File(filePath+"/"+largeFileDTO.getUuid()+"_"+largeFileDTO.getChunk());
            FileCopyUtils.copy(f.getInputStream(),new FileOutputStream(file1));
            String md5 = MD5FileUtil.getFileMD5String(file1);
            if(!md5.equals(largeFileDTO.getMd5())){
                file.delete();
                return fail();
            }
        }
        log.info("文件分片上传，第"+largeFileDTO.getChunk()+"片上传成功！！");
       return success();
    }
    @RequestMapping(value = "/api/upload/largeFile/check")
    public  ResponseDTO<?>  check( @RequestParam LargeFileDTO largeFileDTO){
        String filePath = basePath+largeFileDTO.getUuid();
       File file = new File(filePath+"/"+largeFileDTO.getUuid()+"_"+largeFileDTO.getChunk());
       if(file.exists() && file.length() == largeFileDTO.getSize()){
           log.info("文件分块"+largeFileDTO.getChunk()+"存在！！");
           return  success();
       }else{
           return fail();
       }
    }

    @RequestMapping(value = "/api/upload/largeFile/mergeChunks")
    public ResponseDTO<?> mergeChunks(@RequestParam LargeFileDTO largeFileDTO) throws Exception {
        String filePath = basePath+largeFileDTO.getFileName();
        File[] files = new File(filePath).listFiles();
        if(files.length == largeFileDTO.getTotal().intValue()){
            unionFile(new File(basePath+"upload"),new File(filePath),largeFileDTO.getFileName());
            return success();
        }
        return fail("文件未上传完！！");
    }
    /**
     * 合并文件
     * @param dirFile
     * @param tempFile
     * @param fileName
     * @throws IOException
     */
    public static void unionFile(File dirFile, File tempFile, String fileName) throws IOException {
        //判断目标地址是否存在，不存在则创建
        if(!dirFile.isFile()){
            dirFile.mkdirs();
        }
        List<FileInputStream> list = new ArrayList<FileInputStream>();
        //获取暂存地址中的文件
        File[] files = tempFile.listFiles();

        for (int i = 0; i < files.length; i++) {
            //用FileInputStream读取放入list集合
            list.add(new FileInputStream(new File(tempFile, tempFile.getName()+"_"+i)));
        }
        //使用 Enumeration（列举） 将文件全部列举出来
        Enumeration<FileInputStream> eum = Collections.enumeration(list);
        //SequenceInputStream合并流 合并文件
        SequenceInputStream sis = new SequenceInputStream(eum);
        FileOutputStream fos = new FileOutputStream(new File(dirFile, fileName));
        byte[] by = new byte[2048];
        int len;
        while((len=sis.read(by)) != -1){
            fos.write(by, 0, len);
        }
        fos.flush();
        fos.close();
        sis.close();
        log.info("文件合并成功！！");
    }

}
