package com.troy.keeper.monomer.portal.account.repository.impl;

import com.troy.keeper.core.base.repository.BaseRepositoryImpl;
import com.troy.keeper.monomer.portal.account.web.rest.vo.BYAuditInfoSearchDTO;
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
public class BYAuditInfoRepositoryImpl extends BaseRepositoryImpl {

    /**
     * 查询统计信息
     * @return
     */
    public Integer queryTeacherCountAuditInfoByCondition(BYAuditInfoSearchDTO auditInfoSearch,List<String> classList){
        StringBuilder hqlBuilder = this.createHqlBuilderByParam((byte)0,auditInfoSearch,classList,null);
        Query query = em.createQuery(hqlBuilder.toString());
        buildParam(query,auditInfoSearch,classList,null);
        Object count = query.getSingleResult();
        return Integer.valueOf(count.toString());
    }



    /**
     * 查询数据信息
     * @param pageable
     * @return
     */
    public List<Object[]>  queryTeacherAuditInfoByCondition(BYAuditInfoSearchDTO auditInfoSearch,List<String> classList,Pageable pageable){
        StringBuilder hqlBuilder = this.createHqlBuilderByParam((byte)1,auditInfoSearch,classList,null);
        Query query = em.createQuery(hqlBuilder.toString());
        buildParam(query, auditInfoSearch,classList,null);
        if(pageable!=null){
            query.setFirstResult(pageable.getPageNumber()*pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());
        }
        List<Object[]> obList = query.getResultList();
        return obList;
    }

    /**
     * 查询数据信息
     * @return
     */
    public List<Object[]>  queryTeacherAuditInfoByIdsList(List<String> idsList){
        BYAuditInfoSearchDTO auditInfoSearch = new BYAuditInfoSearchDTO();
        StringBuilder hqlBuilder = this.createHqlBuilderByParam((byte)1,auditInfoSearch,null,idsList);
        Query query = em.createQuery(hqlBuilder.toString());
        buildParam(query, auditInfoSearch,null,idsList);
        List<Object[]> obList = query.getResultList();
        return obList;
    }

    private StringBuilder createHqlBuilderByParam(byte doType, BYAuditInfoSearchDTO auditInfoSearch,List<String> classList,List<String> idsList) {
        StringBuilder hqlBuilder = new StringBuilder();
        if(doType == (byte)0){
            hqlBuilder.append("SELECT COUNT(*) ");
        }else if(doType == (byte)1){
            hqlBuilder.append(" SELECT sm.id,sm.displayName,sm.userNumber,cpcs.platformSysGradeName,cpcs.name,cpcs.platformSysSpecialtyName");
            hqlBuilder.append(" ,sm.mobile,sm.credentialNumber,sa.archivesStatus");
        }else{
            hqlBuilder.append(" SELECT sm.id");
        }
        hqlBuilder.append(" FROM StuMainInfo sm JOIN sm.coursePlanTeachingAdminClass cpcs ");
        hqlBuilder.append(" ,StuArchivesInfo sa ");
        hqlBuilder.append(" WHERE cpcs.id IS NOT NULL");
        hqlBuilder.append(" AND sa.stuBaseInfo.platformSysUserId = sm.id");

        hqlBuilder.append(" AND EXISTS (SELECT 1 FROM CoursePlanTeachingClassStudent tcs WHERE tcs.coursePlanTeachingAdminClass.id = cpcs.id AND tcs.platformSysUserId = sm.id AND  tcs.outClassDate>=:nowDate AND tcs.inClassDate<=:nowDate) ");


        if(StringUtils.isNotEmpty(auditInfoSearch.getStatus())||StringUtils.isNotEmpty(auditInfoSearch.getReachStandard())||StringUtils.isNotEmpty(auditInfoSearch.getFixedItemReach())||StringUtils.isNotEmpty(auditInfoSearch.getResult())){
            hqlBuilder.append(" AND ( EXISTS (SELECT 1 FROM BYAuditInfo bai WHERE bai.stuSysUserId = sm.id");
            if(StringUtils.isNotEmpty(auditInfoSearch.getStatus())){
                hqlBuilder.append(" AND bai.status = :status ");
            }
            if(StringUtils.isNotEmpty(auditInfoSearch.getReachStandard())){
                hqlBuilder.append(" AND bai.reachStandard = :reachStandard ");
            }
            if(StringUtils.isNotEmpty(auditInfoSearch.getFixedItemReach())){
                hqlBuilder.append(" AND bai.fixedItemReach = :fixedItemReach ");
            }
            if(StringUtils.isNotEmpty(auditInfoSearch.getResult())){
                hqlBuilder.append(" AND bai.result = :result ");
            }
            hqlBuilder.append(")");
            if(StringUtils.isNotEmpty(auditInfoSearch.getStatus())&&StringUtils.equals("0",auditInfoSearch.getStatus())){
                hqlBuilder.append(" OR NOT EXISTS (SELECT 1 FROM BYAuditInfo bai WHERE bai.stuSysUserId = sm.id )");
            }
            hqlBuilder.append(")");
        }
        if(CollectionUtils.isNotEmpty(classList)){
            hqlBuilder.append(" AND cpcs.id IN(:classList) ");
        }
        if(CollectionUtils.isNotEmpty(idsList)){
            hqlBuilder.append(" AND sm.id IN(:idsList) ");
        }
        if(StringUtils.isNotEmpty(auditInfoSearch.getRollType())){
            hqlBuilder.append(" AND sa.archivesStatus = :archivesStatus ");
        }
        if(StringUtils.isNotEmpty(auditInfoSearch.getSearchValue())){
            if(StringUtils.equals("1",auditInfoSearch.getSearchType())){
                hqlBuilder.append(" AND sm.name LIKE :name ");
            }else if(StringUtils.equals("2",auditInfoSearch.getSearchType())){
                hqlBuilder.append(" AND sm.userNumber LIKE :userNumber ");
            }else if(StringUtils.equals("3",auditInfoSearch.getSearchType())){
                hqlBuilder.append(" AND sm.credentialNumber LIKE :credentialNumber ");
            }else if(StringUtils.equals("4",auditInfoSearch.getSearchType())){
                hqlBuilder.append(" AND sm.regNumber LIKE :regNumber ");
            }
        }

        if(doType != (byte)0){
            hqlBuilder.append(" ORDER BY cpcs.name,sm.userNumber");
        }
        return hqlBuilder;
    }

    private void buildParam(Query query, BYAuditInfoSearchDTO auditInfoSearch, List<String> classList,List<String> idsList) {
        if(StringUtils.isNotEmpty(auditInfoSearch.getStatus())){
            query.setParameter("status", Byte.valueOf(auditInfoSearch.getStatus()));
        }
        query.setParameter("nowDate", new Date());

        if(StringUtils.isNotEmpty(auditInfoSearch.getReachStandard())){
            query.setParameter("reachStandard",  Byte.valueOf(auditInfoSearch.getReachStandard()));
        }
        if(StringUtils.isNotEmpty(auditInfoSearch.getFixedItemReach())){
            query.setParameter("fixedItemReach",  Byte.valueOf(auditInfoSearch.getFixedItemReach()));
        }
        if(StringUtils.isNotEmpty(auditInfoSearch.getResult())){
            query.setParameter("result",  Byte.valueOf(auditInfoSearch.getResult()));
        }

        if(CollectionUtils.isNotEmpty(classList)){
            query.setParameter("classList", classList);
        }
        if(CollectionUtils.isNotEmpty(idsList)){
            query.setParameter("idsList", idsList);
        }
        if(StringUtils.isNotEmpty(auditInfoSearch.getRollType())){
            query.setParameter("archivesStatus", auditInfoSearch.getRollType());
        }
        if(StringUtils.isNotEmpty(auditInfoSearch.getSearchValue())){
            if(StringUtils.equals("1",auditInfoSearch.getSearchType())){
                query.setParameter("name", "%"+auditInfoSearch.getSearchValue()+"%");
            }else if(StringUtils.equals("2",auditInfoSearch.getSearchType())){
                query.setParameter("userNumber", "%"+auditInfoSearch.getSearchValue()+"%");
            }else if(StringUtils.equals("3",auditInfoSearch.getSearchType())){
                query.setParameter("credentialNumber", "%"+auditInfoSearch.getSearchValue()+"%");
            }else if(StringUtils.equals("4",auditInfoSearch.getSearchType())){
                query.setParameter("regNumber", "%"+auditInfoSearch.getSearchValue()+"%");
            }
        }
    }


    /**
     * 查询统计信息
     * @return
     */
    public Integer queryAdminCountAuditInfoByCondition(BYAuditInfoSearchDTO auditInfoSearch,List<String> orgNoList){
        StringBuilder hqlBuilder = this.createAdminHqlBuilderByParam((byte)0,auditInfoSearch,orgNoList,null);
        Query query = em.createQuery(hqlBuilder.toString());
        buildAdminParam(query, auditInfoSearch, orgNoList, null);
        Object count = query.getSingleResult();
        return Integer.valueOf(count.toString());
    }



    /**
     * 查询数据信息
     * @param pageable
     * @return
     */
    public List<Object[]>  queryAdminAuditInfoByCondition(BYAuditInfoSearchDTO auditInfoSearch,List<String> orgNoList,Pageable pageable){
        StringBuilder hqlBuilder = this.createAdminHqlBuilderByParam((byte)1,auditInfoSearch,orgNoList,null);
        Query query = em.createQuery(hqlBuilder.toString());
        buildAdminParam(query, auditInfoSearch, orgNoList, null);
        if(pageable!=null){
            query.setFirstResult(pageable.getPageNumber()*pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());
        }
        List<Object[]> obList = query.getResultList();
        return obList;
    }

    /**
     * 查询数据信息
     * @return
     */
    public List<Object[]>  queryAuditInfoByIdsList(List<String> idsList){
        BYAuditInfoSearchDTO auditInfoSearch = new BYAuditInfoSearchDTO();
        StringBuilder hqlBuilder = this.createAdminHqlBuilderByParam((byte)1,auditInfoSearch,null,idsList);
        Query query = em.createQuery(hqlBuilder.toString());
        buildParam(query, auditInfoSearch,null,idsList);
        List<Object[]> obList = query.getResultList();
        return obList;
    }

    private StringBuilder createAdminHqlBuilderByParam(byte doType, BYAuditInfoSearchDTO auditInfoSearch,List<String> orgNoList,List<String> idsList) {
        StringBuilder hqlBuilder = new StringBuilder();
        if(doType == (byte)0){
            hqlBuilder.append("SELECT COUNT(*) ");
        }else if(doType == (byte)1){
            hqlBuilder.append(" SELECT sm.id,sm.displayName,sm.userNumber,cpcs.platformSysGradeName,cpcs.name,cpcs.platformSysSpecialtyName");
            hqlBuilder.append(" ,sm.mobile,sm.credentialNumber,sa.archivesStatus");
            if(StringUtils.isNotEmpty(auditInfoSearch.getGradeYear())){
                hqlBuilder.append(",pscs.graduateDate");
            }
        }else{
            hqlBuilder.append(" SELECT sm.id");
        }
        hqlBuilder.append(" FROM StuMainInfo sm JOIN sm.coursePlanTeachingAdminClass cpcs ");
        hqlBuilder.append(" ,StuArchivesInfo sa ");
        if(StringUtils.isNotEmpty(auditInfoSearch.getGradeYear())){
            hqlBuilder.append(" ,PlatformSysAdminClassStudent pscs ");
        }
        hqlBuilder.append(" WHERE cpcs.id IS NOT NULL");
        hqlBuilder.append(" AND sa.stuBaseInfo.platformSysUserId = sm.id");
        if(StringUtils.isNotEmpty(auditInfoSearch.getGradeYear())){
            hqlBuilder.append(" AND pscs.platformSysUser.id = sm.id AND pscs.status = 1");
        }

        if(StringUtils.isNotEmpty(auditInfoSearch.getStatus())||StringUtils.isNotEmpty(auditInfoSearch.getReachStandard())||StringUtils.isNotEmpty(auditInfoSearch.getFixedItemReach())||StringUtils.isNotEmpty(auditInfoSearch.getResult())){
            hqlBuilder.append(" AND ( EXISTS (SELECT 1 FROM BYAuditInfo bai WHERE bai.stuSysUserId = sm.id");
            if(StringUtils.isNotEmpty(auditInfoSearch.getStatus())){
                hqlBuilder.append(" AND bai.status = :status ");
            }
            if(StringUtils.isNotEmpty(auditInfoSearch.getReachStandard())){
                hqlBuilder.append(" AND bai.reachStandard = :reachStandard ");
            }
            if(StringUtils.isNotEmpty(auditInfoSearch.getFixedItemReach())){
                hqlBuilder.append(" AND bai.fixedItemReach = :fixedItemReach ");
            }
            if(StringUtils.isNotEmpty(auditInfoSearch.getResult())){
                hqlBuilder.append(" AND bai.result = :result ");
            }
            hqlBuilder.append(")");
            if(StringUtils.isNotEmpty(auditInfoSearch.getStatus())&&StringUtils.equals("0",auditInfoSearch.getStatus())){
                hqlBuilder.append(" OR NOT EXISTS (SELECT 1 FROM BYAuditInfo bai WHERE bai.stuSysUserId = sm.id )");
            }
            hqlBuilder.append(")");
        }
        if(CollectionUtils.isNotEmpty(orgNoList)){
            hqlBuilder.append(" AND cpcs.platformSysOrgNo IN(:orgNoList) ");
        }
        if(CollectionUtils.isNotEmpty(idsList)){
            hqlBuilder.append(" AND sm.id IN(:idsList) ");
        }
        if(StringUtils.isNotEmpty(auditInfoSearch.getRollType())){
            hqlBuilder.append(" AND sa.archivesStatus = :archivesStatus ");
        }
        if(StringUtils.isNotEmpty(auditInfoSearch.getGradeId())){//年级
            hqlBuilder.append(" AND cpcs.platformSysGradeId IN(:gradeId) ");
        }

        if(StringUtils.isNotEmpty(auditInfoSearch.getSpecialtyId())){//专业
            hqlBuilder.append(" AND cpcs.platformSysSpecialtyId IN(:specialtyId) ");
        }

        if(auditInfoSearch.getClassIds()!=null && auditInfoSearch.getClassIds().length>0){//班级
            hqlBuilder.append(" AND cpcs.id IN(:classList) ");
        }
        if(StringUtils.isNotEmpty(auditInfoSearch.getEnrollType())){//招生类型
            hqlBuilder.append(" AND cpcs.enrollTypeId IN(:enrollType) ");
        }
        if(StringUtils.isNotEmpty(auditInfoSearch.getSearchValue())){
            if(StringUtils.equals("1",auditInfoSearch.getSearchType())){
                hqlBuilder.append(" AND sm.name LIKE :name ");
            }else if(StringUtils.equals("2",auditInfoSearch.getSearchType())){
                hqlBuilder.append(" AND sm.userNumber LIKE :userNumber ");
            }else if(StringUtils.equals("3",auditInfoSearch.getSearchType())){
                hqlBuilder.append(" AND sm.credentialNumber LIKE :credentialNumber ");
            }else if(StringUtils.equals("4",auditInfoSearch.getSearchType())){
                hqlBuilder.append(" AND sm.regNumber LIKE :regNumber ");
            }
        }

        if(StringUtils.isNotEmpty(auditInfoSearch.getGradeYear())){
            hqlBuilder.append(" AND YEAR(pscs.graduateDate) = :graduateDate ");
        }
        if(auditInfoSearch.getIncompletion()!=null && auditInfoSearch.getIncompletion()){
            hqlBuilder.append(" AND  EXISTS (SELECT 1 FROM StuUnusualChangeInfo AS ci WHERE ci.isUnderGraduate = true AND ci.checkStatus = 1 AND ci.stuBaseInfo.platformSysUserId = sm.id )");
        }
        if(doType != (byte)0){
            hqlBuilder.append(" ORDER BY cpcs.name,sm.userNumber");
        }
        return hqlBuilder;
    }

    private void buildAdminParam(Query query, BYAuditInfoSearchDTO auditInfoSearch, List<String> orgNoList,List<String> idsList) {
        if(StringUtils.isNotEmpty(auditInfoSearch.getStatus())){
            query.setParameter("status", Byte.valueOf(auditInfoSearch.getStatus()));
        }
        if(StringUtils.isNotEmpty(auditInfoSearch.getReachStandard())){
            query.setParameter("reachStandard",  Byte.valueOf(auditInfoSearch.getReachStandard()));
        }
        if(StringUtils.isNotEmpty(auditInfoSearch.getFixedItemReach())){
            query.setParameter("fixedItemReach",  Byte.valueOf(auditInfoSearch.getFixedItemReach()));
        }
        if(StringUtils.isNotEmpty(auditInfoSearch.getResult())){
            query.setParameter("result",  Byte.valueOf(auditInfoSearch.getResult()));
        }
        if(CollectionUtils.isNotEmpty(orgNoList)){
            query.setParameter("orgNoList", orgNoList);
        }
        if(StringUtils.isNotEmpty(auditInfoSearch.getGradeId())){//年级
            query.setParameter("gradeId", auditInfoSearch.getGradeId());
        }
        if(StringUtils.isNotEmpty(auditInfoSearch.getSpecialtyId())){//专业
            query.setParameter("specialtyId", auditInfoSearch.getSpecialtyId());
        }
        if(auditInfoSearch.getClassIds()!=null && auditInfoSearch.getClassIds().length>0){//班级
            query.setParameter("classList", Arrays.asList(auditInfoSearch.getClassIds()));
        }
        if(CollectionUtils.isNotEmpty(idsList)){
            query.setParameter("idsList", idsList);
        }
        if(StringUtils.isNotEmpty(auditInfoSearch.getRollType())){
            query.setParameter("archivesStatus", auditInfoSearch.getRollType());
        }
        if(StringUtils.isNotEmpty(auditInfoSearch.getSearchValue())){
            if(StringUtils.equals("1",auditInfoSearch.getSearchType())){
                query.setParameter("name", "%"+auditInfoSearch.getSearchValue()+"%");
            }else if(StringUtils.equals("2",auditInfoSearch.getSearchType())){
                query.setParameter("userNumber", "%"+auditInfoSearch.getSearchValue()+"%");
            }else if(StringUtils.equals("3",auditInfoSearch.getSearchType())){
                query.setParameter("credentialNumber", "%"+auditInfoSearch.getSearchValue()+"%");
            }else if(StringUtils.equals("4",auditInfoSearch.getSearchType())){
                query.setParameter("regNumber", "%"+auditInfoSearch.getSearchValue()+"%");
            }
        }
        if(StringUtils.isNotEmpty(auditInfoSearch.getEnrollType())){//招生类型
            query.setParameter("enrollType", auditInfoSearch.getEnrollType());
        }
        if(StringUtils.isNotEmpty(auditInfoSearch.getGradeYear())){
            query.setParameter("graduateDate", Integer.valueOf(auditInfoSearch.getGradeYear()));
        }
    }
}
