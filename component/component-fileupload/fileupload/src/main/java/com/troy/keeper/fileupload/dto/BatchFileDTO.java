package com.troy.keeper.fileupload.dto;

import com.troy.keeper.core.base.dto.BaseDTO;
import com.troy.keeper.fileupload.type.Whether;
import lombok.Data;

import java.util.List;

/**
 * @author Administrator
 */
@Data
public class BatchFileDTO extends BaseDTO {
    private Long id;
    private Long fileTypeId;
    private List<AttachFileDTO> fileList;

}
