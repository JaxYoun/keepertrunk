package com.troy.keeper.monomer.portal.account.service.impl;

import com.troy.keeper.core.base.dto.BaseDTO;
import com.troy.keeper.core.base.service.BaseServiceImpl;
import com.troy.keeper.core.utils.MapperParam;
import com.troy.keeper.core.utils.MapperUtils;
import com.troy.keeper.monomer.portal.account.domain.LoginLog;
import com.troy.keeper.monomer.portal.account.repository.LoginLogRepository;
import com.troy.keeper.monomer.portal.account.service.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * Created by yg on 2017/4/7.
 */
@Service
public class LoginLogServiceImpl extends BaseServiceImpl<LoginLog, BaseDTO> implements LoginLogService {
    @Autowired
    private LoginLogRepository loginLogRepository;
    @Autowired
    private MapperUtils mapperUtils;

    @Override
    public Page<Map<String, Object>> queryLoginLogsPage(Date start, Date end, Pageable pageable)  throws Exception{
        //查询条件
        Specification<LoginLog> spec = new Specification<LoginLog>(){
            @Override
            public Predicate toPredicate(Root<LoginLog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();
                if (start != null){
                    list.add(criteriaBuilder.greaterThanOrEqualTo(root.get("loginTime").as(Date.class), start));
                }
                if(end != null){
                    list.add(criteriaBuilder.lessThan(root.get("loginTime").as(Date.class), end));
                }
                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        };

        //查询
        Page<LoginLog> page = loginLogRepository.findAll(spec, pageable);

        List<String> ignoreColumnList = new ArrayList<String>();
        ignoreColumnList.add("createdBy");
        ignoreColumnList.add("createdDate");
        ignoreColumnList.add("lastUpdatedBy");
        ignoreColumnList.add("lastUpdatedDate");

        List<String> userIdColumnList = new ArrayList<String>();
        userIdColumnList.add("userId");

        MapperParam mapperParam = new MapperParam();
//        mapperParam.setPage(page);
        mapperParam.setIgnoreColumnList(ignoreColumnList);
        mapperParam.setKeyList(null);
        mapperParam.setSourceColumnList(null);
        mapperParam.setUserIdColumnList(userIdColumnList);

        return mapperUtils.convertPage(mapperParam);
    }
}
