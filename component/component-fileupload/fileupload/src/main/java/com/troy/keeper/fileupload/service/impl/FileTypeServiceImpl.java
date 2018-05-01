package com.troy.keeper.fileupload.service.impl;

import com.troy.keeper.core.base.service.BaseServiceImpl;
import com.troy.keeper.fileupload.domain.AttachFile;
import com.troy.keeper.fileupload.domain.FileType;
import com.troy.keeper.fileupload.dto.AttachFileDTO;
import com.troy.keeper.fileupload.dto.FileTypeDTO;
import com.troy.keeper.fileupload.service.mapper.AttachFileMapper;
import com.troy.keeper.fileupload.service.mapper.FileTypeMapper;
import com.troy.keeper.fileupload.repository.FileTypeRepository;
import com.troy.keeper.fileupload.service.FileTypeService;
import com.troy.keeper.core.utils.db.DbUtils;
import com.troy.keeper.fileupload.type.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.List;


/**
 * Created by SimonChu on 2017/6/1.
 */
@Service
@Transactional
public class FileTypeServiceImpl extends BaseServiceImpl<FileType, FileTypeDTO> implements FileTypeService {
    @Autowired
    private FileTypeRepository fileTypeRepository;
    @Autowired
    private FileTypeMapper fileTypeMapper;
    @Autowired
    private AttachFileMapper attachFileMapper;
    @Override
    public  List<FileTypeDTO>  list(FileTypeDTO fileTypeDTO) {
       return fileTypeMapper.objsToDTOs(fileTypeRepository.findAll( (Root<FileType> root, CriteriaQuery<?> query, CriteriaBuilder cb)->{
           List<Predicate> list =  DbUtils.getPredicateList(root,query,cb,fileTypeDTO,FileType.class);
           Predicate[] p = new Predicate[list.size()];
           return cb.and(list.toArray(p));
       }));
    }

    @Override
    public FileTypeDTO get(Long id) {
        return fileTypeMapper.objToDTO(fileTypeRepository.findOne(id));
    }

    @Override
    public Page<FileTypeDTO> listForPage(FileTypeDTO fileTypeDTO) {
        Page<FileType> fileTypes = fileTypeRepository.findAll((Root<FileType> root, CriteriaQuery<?> query, CriteriaBuilder cb)->{
            List<Predicate> list =  DbUtils.getPredicateList(root,query,cb,fileTypeDTO,FileType.class);
            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        },new PageRequest(fileTypeDTO.getPage(), fileTypeDTO.getSize()));
        return fileTypes.map((FileType fileType) -> fileTypeMapper.objToDTO(fileType));

    }

    @Override
    public FileTypeDTO save(FileTypeDTO fileTypeDTO) {
        return fileTypeMapper.objToDTO(fileTypeRepository.save(fileTypeMapper.DTOToObj(fileTypeDTO)));
    }

    @Override
    public void del(Long id) {
        if(id == null || id == 0L)
            return;
        FileType fileType = new FileType();
        fileType.setId(id);
        fileType.setStatus(Constant.DEL_STATUS);
        fileTypeRepository.save(fileType);
    }

    @Override
    public Page<AttachFileDTO> queryFileByTypeId(FileTypeDTO fileTypeDTO) {
        Page<AttachFile> attachFiles = fileTypeRepository.queryFileByFileTypeId(fileTypeDTO.getId(),new PageRequest(fileTypeDTO.getPage(), fileTypeDTO.getSize()));
        return attachFiles.map((AttachFile attachFile) -> attachFileMapper.objToDTO(attachFile));
    }
}
