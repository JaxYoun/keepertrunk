package com.troy.keeper.monomer.demo.service.mapper;

import com.troy.keeper.monomer.api.dto.DataDictionaryDTO;
import com.troy.keeper.monomer.demo.domain.DataDictionary;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Created by yg on 2017/4/17.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DataDictionaryMapper {
    DataDictionaryDTO dataDictionaryToDataDictionaryDTO(DataDictionary dataDictionary);
    List<DataDictionaryDTO> dataDictionariesToDataDictionaryDTOs(List<DataDictionary> dataDictionaries);
}
