package com.troy.keeper.fileupload.service.mapper;

import com.troy.keeper.fileupload.domain.AttachFile;
import com.troy.keeper.fileupload.domain.FileType;
import com.troy.keeper.fileupload.dto.AttachFileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Created by Administrator on 2017/6/29.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AttachFileMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "fileName", target = "fileName")
    @Mapping(source = "filePath", target = "filePath")
    @Mapping(source = "fileSize", target = "fileSize")
    @Mapping(source = "isEncryption", target = "isEncryption")
    AttachFileDTO objToDTO(AttachFile attachFile);
    List<AttachFileDTO> objsToDTOs(List<AttachFile> attachFiles);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "fileName", target = "fileName")
    @Mapping(source = "filePath", target = "filePath")
    @Mapping(source = "fileSize", target = "fileSize")
    @Mapping(source = "isEncryption", target = "isEncryption")
    AttachFile DTOToObj(AttachFileDTO fileTypeDTO);
}
