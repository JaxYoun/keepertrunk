package com.troy.keeper.fileupload.service.mapper;

import com.troy.keeper.fileupload.domain.BatchFile;
import com.troy.keeper.fileupload.dto.BatchFileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Created by Administrator on 2017/6/29.
 */
@Mapper(componentModel = "spring", uses = {AttachFileMapper.class})
public interface BatchFileMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "fileList", target = "fileList")
    BatchFileDTO objToDTO(BatchFile batchFile);
    List<BatchFileDTO> objsToDTOs(List<BatchFile> batchFiles);
    @Mapping(source = "id", target = "id")
    BatchFile DTOToObj(BatchFileDTO batchFileDTO);
}
