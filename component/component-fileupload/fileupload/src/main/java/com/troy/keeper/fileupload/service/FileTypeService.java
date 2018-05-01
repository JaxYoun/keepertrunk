package com.troy.keeper.fileupload.service;

import com.troy.keeper.core.base.service.BaseService;
import com.troy.keeper.core.error.KeeperException;
import com.troy.keeper.fileupload.domain.FileType;
import com.troy.keeper.fileupload.dto.AttachFileDTO;
import com.troy.keeper.fileupload.dto.FileTypeDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;


public interface FileTypeService extends BaseService<FileType, FileTypeDTO> {

      List<FileTypeDTO>  list(FileTypeDTO fileTypeDTO);

     FileTypeDTO get(Long id)throws Exception;

     Page<FileTypeDTO> listForPage(FileTypeDTO fileTypeDTO);

     FileTypeDTO save(FileTypeDTO fileTypeDTO);

     void del(Long id);

    Page<AttachFileDTO> queryFileByTypeId(FileTypeDTO fileTypeDTO);

}
