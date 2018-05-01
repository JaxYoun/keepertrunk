package com.troy.keeper.monomer.demo.web.rest.vo;

import com.troy.keeper.core.base.dto.BaseDTO;

import java.util.List;
import java.util.Set;

/**
 * BYAuditInfoSearchDTO
 * Created by ransheng on 2015/9/21.
 */
public class BYAuditInfoSearchDTO extends BaseDTO {

    //模糊查询Key 1：姓名,2:学号,3:证件号,4:报名号
    private String searchType;

    //模糊查询值
    private String searchValue;

    //学籍状态,码表,不传表示全部，0:在读,1:休学,2:退休,3:转学,4: 肄业,5:毕业,6:结业,7: 延长学制,8: 转学转出,9: 开除学籍,10: 取消学籍.
    private String rollType;

    //班级id
    private String[] classIds;

    //组织机构OrgCode数组
    private String[] orgCodes;

    //年级id
    private String gradeId;

    //年份
    private String gradeYear;

    //招生类型
    private String enrollType;

    //操作员id
    private String operatorId;

    //系统Id
    private String systemId;

    //已标肄业 true，false
    private Boolean incompletion;

    //状态 0:未提交  1:返回初审 2:复审中 3:终审通过
    private String status;

    //项目达标情况 0:全部不达标 1:部分达标 2:全部达标
    private String reachStandard;

    //硬性项目达标情况 0:全部不达标 1:部分达标 2:全部达标
    private String fixedItemReach;

    //毕业判定结果 0:离校 1:毕业 2:结业 3:肄业 4:推迟毕业
    private String result;

    //引用基础平台专业（PLATFORM_SYS_SPECIALTY）的ID
    private String specialtyId;

    //用户请求类型: "1" 管理员端    "2" 班主任端
    private String userType;

    //通过姓名获取用户ID
    private List<String> userIdsByName;

    //通过学号获取用户ID
    private List<String> userIdsByNumber;

    //通过证件号获取用户ID
    private List<String> userIdsByIdentityNo;

    //通过报名号获取用户ID
    private List<String> userIdsByRegNumber;

    //通过学籍状态获取用户ID
    private List<String> userIdsByRollType;

    //通过毕业年份获取教学行政班ID
    private List<String> userIdsByGradeYear;

    //通过组织机构获取用户ID
    private Set<String> userIdsByOrgNos;

    //教师教学行政班下的学生
    private Set<String> studentIdSet;

    //审核类型 "1" 管理员端 学生毕业审核  "2" 管理员端 学生毕业终审
    private String auditType;

    //根据状态找到学生ID
    private List<String> userIdsByStatus;

    //根据毕业判定结果找到学生ID
    private List<String> userIdsByResult;

    //根据项目达标情况找到学生ID
    private List<String> userIdsByReachStandard;

    //根据硬性项目达标情况找到学生ID
    private List<String> userIdsByFixedItemReach;

    //根据 已标肄业 获取学生ID
    private List<String> userIdsByIncompletion;

    //根据 教学行政班ID 获取 学生ID
    private List<String> userIdsByTeachClassIdList;

    //根据 年级ID 获取 学生ID
    private Set<String> userIdsByGradeId;

    //根据 专业ID 获取 学生ID
    private Set<String> userIdsBySpecialtyId;

    //根据 班级ID 获取 学生ID
    private Set<String> userIdsByClassIds;

    //根据 招生类型 获取 学生ID
    private Set<String> userIdsByEnrollType;

    //未被删除的学生ID
    private List<String> userIdsByNotDel;

    /**
     * 是否使用 组织机构、年级、专业、招生类型、教学行政班ID
     */
    private boolean userFiveParam = false;
    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }


    public Boolean getIncompletion() {
        return incompletion;
    }

    public void setIncompletion(Boolean incompletion) {
        this.incompletion = incompletion;
    }

    public String getRollType() {
        return rollType;
    }

    public void setRollType(String rollType) {
        this.rollType = rollType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReachStandard() {
        return reachStandard;
    }

    public void setReachStandard(String reachStandard) {
        this.reachStandard = reachStandard;
    }

    public String getFixedItemReach() {
        return fixedItemReach;
    }

    public void setFixedItemReach(String fixedItemReach) {
        this.fixedItemReach = fixedItemReach;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String[] getClassIds() {
        return classIds;
    }

    public void setClassIds(String[] classIds) {
        this.classIds = classIds;
    }

    public String[] getOrgCodes() {
        return orgCodes;
    }

    public void setOrgCodes(String[] orgCodes) {
        this.orgCodes = orgCodes;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(String specialtyId) {
        this.specialtyId = specialtyId;
    }

    public String getGradeYear() {
        return gradeYear;
    }

    public void setGradeYear(String gradeYear) {
        this.gradeYear = gradeYear;
    }

    public String getEnrollType() {
        return enrollType;
    }

    public void setEnrollType(String enrollType) {
        this.enrollType = enrollType;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<String> getUserIdsByName() {
        return userIdsByName;
    }

    public void setUserIdsByName(List<String> userIdsByName) {
        this.userIdsByName = userIdsByName;
    }

    public List<String> getUserIdsByNumber() {
        return userIdsByNumber;
    }

    public void setUserIdsByNumber(List<String> userIdsByNumber) {
        this.userIdsByNumber = userIdsByNumber;
    }

    public List<String> getUserIdsByIdentityNo() {
        return userIdsByIdentityNo;
    }

    public void setUserIdsByIdentityNo(List<String> userIdsByIdentityNo) {
        this.userIdsByIdentityNo = userIdsByIdentityNo;
    }

    public List<String> getUserIdsByRegNumber() {
        return userIdsByRegNumber;
    }

    public void setUserIdsByRegNumber(List<String> userIdsByRegNumber) {
        this.userIdsByRegNumber = userIdsByRegNumber;
    }

    public List<String> getUserIdsByRollType() {
        return userIdsByRollType;
    }

    public void setUserIdsByRollType(List<String> userIdsByRollType) {
        this.userIdsByRollType = userIdsByRollType;
    }

    public List<String> getUserIdsByGradeYear() {
        return userIdsByGradeYear;
    }

    public void setUserIdsByGradeYear(List<String> userIdsByGradeYear) {
        this.userIdsByGradeYear = userIdsByGradeYear;
    }

    public Set<String> getStudentIdSet() {
        return studentIdSet;
    }

    public void setStudentIdSet(Set<String> studentIdSet) {
        this.studentIdSet = studentIdSet;
    }

    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    public List<String> getUserIdsByStatus() {
        return userIdsByStatus;
    }

    public void setUserIdsByStatus(List<String> userIdsByStatus) {
        this.userIdsByStatus = userIdsByStatus;
    }

    public List<String> getUserIdsByResult() {
        return userIdsByResult;
    }

    public void setUserIdsByResult(List<String> userIdsByResult) {
        this.userIdsByResult = userIdsByResult;
    }

    public List<String> getUserIdsByReachStandard() {
        return userIdsByReachStandard;
    }

    public void setUserIdsByReachStandard(List<String> userIdsByReachStandard) {
        this.userIdsByReachStandard = userIdsByReachStandard;
    }

    public List<String> getUserIdsByFixedItemReach() {
        return userIdsByFixedItemReach;
    }

    public void setUserIdsByFixedItemReach(List<String> userIdsByFixedItemReach) {
        this.userIdsByFixedItemReach = userIdsByFixedItemReach;
    }

    public List<String> getUserIdsByIncompletion() {
        return userIdsByIncompletion;
    }

    public void setUserIdsByIncompletion(List<String> userIdsByIncompletion) {
        this.userIdsByIncompletion = userIdsByIncompletion;
    }

    public List<String> getUserIdsByTeachClassIdList() {
        return userIdsByTeachClassIdList;
    }

    public void setUserIdsByTeachClassIdList(List<String> userIdsByTeachClassIdList) {
        this.userIdsByTeachClassIdList = userIdsByTeachClassIdList;
    }

    public Set<String> getUserIdsByClassIds() {
        return userIdsByClassIds;
    }

    public void setUserIdsByClassIds(Set<String> userIdsByClassIds) {
        this.userIdsByClassIds = userIdsByClassIds;
    }

    public Set<String> getUserIdsByOrgNos() {
        return userIdsByOrgNos;
    }

    public void setUserIdsByOrgNos(Set<String> userIdsByOrgNos) {
        this.userIdsByOrgNos = userIdsByOrgNos;
    }

    public Set<String> getUserIdsByGradeId() {
        return userIdsByGradeId;
    }

    public void setUserIdsByGradeId(Set<String> userIdsByGradeId) {
        this.userIdsByGradeId = userIdsByGradeId;
    }

    public Set<String> getUserIdsBySpecialtyId() {
        return userIdsBySpecialtyId;
    }

    public void setUserIdsBySpecialtyId(Set<String> userIdsBySpecialtyId) {
        this.userIdsBySpecialtyId = userIdsBySpecialtyId;
    }

    public Set<String> getUserIdsByEnrollType() {
        return userIdsByEnrollType;
    }

    public void setUserIdsByEnrollType(Set<String> userIdsByEnrollType) {
        this.userIdsByEnrollType = userIdsByEnrollType;
    }

    public List<String> getUserIdsByNotDel() {
        return userIdsByNotDel;
    }

    public void setUserIdsByNotDel(List<String> userIdsByNotDel) {
        this.userIdsByNotDel = userIdsByNotDel;
    }

    public boolean isUserFiveParam() {
        return userFiveParam;
    }

    public void setUserFiveParam(boolean userFiveParam) {
        this.userFiveParam = userFiveParam;
    }
}
