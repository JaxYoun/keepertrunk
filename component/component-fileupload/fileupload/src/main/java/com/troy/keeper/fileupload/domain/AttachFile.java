package com.troy.keeper.fileupload.domain;

import com.troy.keeper.core.base.entity.BaseAuditingEntity;
import com.troy.keeper.fileupload.type.Whether;
import lombok.Data;
import lombok.extern.java.Log;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "attach_file")
public class AttachFile extends BaseAuditingEntity {

    // 文件名
    @Column(name = "file_name",length = 200)
    private String fileName;

    // 文件存放相对地址
    @Column(name = "file_path", length = 200)
    private String filePath;

    // 文件大小
    @Column(name = "file_size")
    private Long fileSize;

    // 是否加密文件
    @Column(name = "is_encryption", length = 2)
    @Enumerated(EnumType.STRING)
    private Whether isEncryption = Whether.N;

    @ManyToOne(fetch = FetchType.LAZY)
    private BatchFile batchFile;


}
