package com.troy.keeper.fileupload.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.fileupload.domain.AttachFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface AttachFileRepository extends BaseRepository<AttachFile, Long> {

//    @Query("select a from BatchFile a    where b.fileTypeId = ?1")
//    public Page<AttachFile> quyerFileByFileTypeId(Long fileTypeId,Pageable pageable);

}
