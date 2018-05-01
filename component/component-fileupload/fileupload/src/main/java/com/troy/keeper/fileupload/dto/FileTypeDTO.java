package com.troy.keeper.fileupload.dto;

import com.troy.keeper.core.base.dto.PageDTO;
import com.troy.keeper.fileupload.type.Whether;
import lombok.Data;

@Data
public class FileTypeDTO extends PageDTO {

    private Long id;

    // 文件分类代码
    private String categoryCode;

    // 文件分类名称
    private String categoryName;

    // 父文件分类代码
    private String parentCategoryCode;

    // 是否加密文件
    private Whether isEncryption = Whether.N;


}
