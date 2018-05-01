package com.troy.keeper.fileupload.service.mapper;

import com.troy.keeper.fileupload.domain.FileType;
import com.troy.keeper.fileupload.dto.FileTypeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Created by Administrator on 2017/6/29.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FileTypeMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "categoryCode", target = "categoryCode")
    @Mapping(source = "categoryName", target = "categoryName")
    @Mapping(source = "parentCategoryCode", target = "parentCategoryCode")
    @Mapping(source = "isEncryption", target = "isEncryption")
    FileTypeDTO objToDTO(FileType fileType);


    List<FileTypeDTO> objsToDTOs(List<FileType> fileTypes);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "categoryCode", target = "categoryCode")
    @Mapping(source = "categoryName", target = "categoryName")
    @Mapping(source = "parentCategoryCode", target = "parentCategoryCode")
    @Mapping(source = "isEncryption", target = "isEncryption")
    FileType DTOToObj(FileTypeDTO fileTypeDTO);
}
