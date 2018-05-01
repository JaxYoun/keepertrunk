package com.troy.keeper.monomer.demo.repository.impl;

import com.troy.keeper.core.base.repository.BaseRepositoryImpl;
import com.troy.keeper.monomer.demo.web.rest.vo.BYAuditInfoSearchDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;

import javax.persistence.Query;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by yineng on 2015/10/9.
 */
public class CustomRepositoryImpl extends BaseRepositoryImpl {

    Integer hello() {
        return 0;
    }

}
