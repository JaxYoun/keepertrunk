package com.troy.keeper.fileupload.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.fileupload.domain.AttachFile;
import com.troy.keeper.fileupload.domain.FileType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface FileTypeRepository extends BaseRepository<FileType, Long> {
    @Query(" select a from AttachFile a join a.batchFile bf join bf.fileType ft  where ft.id=?1")
    public Page<AttachFile> queryFileByFileTypeId(Long id, Pageable pageable);
}
