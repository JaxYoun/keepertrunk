package com.troy.keeper.fileupload.domain;

import com.troy.keeper.core.base.entity.BaseAuditingEntity;
import com.troy.keeper.fileupload.type.Whether;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@Table(name = "file_Type")
public class FileType extends BaseAuditingEntity {

    // 文件分类代码
    @Column(name = "category_code",length = 50)
    private String categoryCode;

    // 文件分类名称
    @Column(name = "category_name", length = 50)
    private String categoryName;

    // 父文件分类代码
    @Column(name = "parent_category_code")
    private String parentCategoryCode;
    // 父文件分类代码
    @Column(name = "parent_category_id")
    private String parentCategoryId;

    // 是否加密文件
    @Column(name = "is_encryption", length = 2)
    @Enumerated(EnumType.STRING)
    private Whether isEncryption = Whether.N;
    @OneToMany(fetch = FetchType.LAZY)
    private List<BatchFile> batchFileList = new ArrayList();

}
