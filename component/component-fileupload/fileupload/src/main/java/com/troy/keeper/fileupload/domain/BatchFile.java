package com.troy.keeper.fileupload.domain;

import com.troy.keeper.core.base.entity.BaseAuditingEntity;
import com.troy.keeper.fileupload.type.Whether;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Administrator
 */
@Entity
@Data
@Table(name = "batch_file")
public class BatchFile extends BaseAuditingEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private FileType fileType;

    @OneToMany(cascade= {CascadeType.ALL},fetch = FetchType.EAGER,mappedBy="batchFile" )
    private List<AttachFile> fileList = new ArrayList();
}
