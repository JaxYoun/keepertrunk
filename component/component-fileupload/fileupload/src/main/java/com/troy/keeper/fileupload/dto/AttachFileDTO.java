package com.troy.keeper.fileupload.dto;

import com.troy.keeper.core.base.dto.PageDTO;
import com.troy.keeper.fileupload.type.Whether;
import lombok.Data;

@Data
public class AttachFileDTO extends PageDTO {

    private Long id;

    // 文件名
    private String fileName;

    // 文件存放相对地址
    private String filePath;

    // 文件大小
    private Long fileSize;

    private String fileUrl;

    // 是否加密文件
    private Whether isEncryption = Whether.N;


}
