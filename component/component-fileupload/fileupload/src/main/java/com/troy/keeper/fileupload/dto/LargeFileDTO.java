package com.troy.keeper.fileupload.dto;


import lombok.Data;

@Data
public class LargeFileDTO  {

    private String uuid;

    private String md5;

    private Integer chunk;

    private Long size;

    private Integer total;

    private String fileName;


}
