package com.troy.keeper.fileupload.dto;

import com.troy.keeper.core.base.dto.BaseDTO;
import com.troy.keeper.fileupload.type.Whether;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author Administrator
 */
@Data
public class UploadFileDTO extends BaseDTO {

   private Map<String,MultipartFile> fileMap;
   private Long  fileTypeId;
   private Long batchId;
}
