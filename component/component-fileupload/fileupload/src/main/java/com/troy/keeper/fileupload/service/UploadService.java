package com.troy.keeper.fileupload.service;

import com.troy.keeper.core.base.service.BaseService;
import com.troy.keeper.core.error.KeeperException;
import com.troy.keeper.fileupload.domain.AttachFile;
import com.troy.keeper.fileupload.domain.BatchFile;
import com.troy.keeper.fileupload.dto.AttachFileDTO;
import com.troy.keeper.fileupload.dto.BatchFileDTO;
import com.troy.keeper.fileupload.dto.UploadFileDTO;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


public interface UploadService extends BaseService<AttachFile, UploadFileDTO> {

    /**
     * 文件上传 支持多文件上传
     * @param uploadFileDTO
     * @return
     * @throws KeeperException
     */
      BatchFileDTO  uploadFiles(UploadFileDTO uploadFileDTO) throws KeeperException;

    /**
     * 根据文件id删除文件
     * @param id
     */
      void  delFileById(Long id) ;

    /**
     * 根据批次号删除一批文件
     * @param id
     */
      void  delFileByBatchId(Long id);

    /**
     * 根据文件id下载文件
     * @param id
     * @param response
     * @throws IOException
     */
      void   downloadFile(Long id, HttpServletResponse response) throws IOException;
    /**
     * 根据文件id下载文件
     * @param id
     * @param outputStream
     * @throws IOException
     */
    void   downloadFile(Long id, OutputStream outputStream) throws IOException;
    /**
     * 根据文件id查询文件
     * @param id
     * @return
     */
     AttachFileDTO queryFileById(Long id);

    /**
     * 根据批次id查询文件
     * @param id
     * @return
     */
     List<AttachFileDTO> queryFileByBatchId(Long id);

    /**
     * 根据文件类型查询文件
     * @param fileDTO
     * @return
     */
     Page<AttachFileDTO> queryFileByFileTypeId(AttachFileDTO fileDTO);

    /**
     * 查询批次
     * @param id
     * @return
     */
     BatchFileDTO queryBatch(Long id);

    /**
     * 拷贝文件
     * @param id
     * @return
     */
    AttachFileDTO copyFile(Long id);

    /**
     * 批量拷贝文件
     * @param id
     * @return
     */
    BatchFileDTO copyBatchFiles(Long id);
}
