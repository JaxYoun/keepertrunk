package com.troy.keeper.system.service.impl;

import com.troy.keeper.core.base.repository.CommonRepository;
import com.troy.keeper.core.base.service.BaseServiceImpl;
import com.troy.keeper.system.domain.*;
import com.troy.keeper.system.service.SmInitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * * * * * * * * * * * * *
 * 系统管理初始化服务实现层
 * * * * * * * * * * * * *
 * Class Name:SmInitServiceImpl
 * <p>
 * 系统管理启动时将检测系统管理的所有表中是否存在数据，如果不存在数据将默认自动创建一套初始数据
 *
 * @author SimonChu By Troy
 * @create 2017-09-15 14:35
 **/
@Service
@Transactional
public class SmInitServiceImpl extends BaseServiceImpl implements SmInitService {

    @Autowired
    private CommonRepository commonRepository;

    private final Logger log = LoggerFactory.getLogger(SmInitServiceImpl.class);

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 有效数据状态
    private static final String VALID_DATA_STATUS = "1";

    private final static String DEFAULT_INIT_SM_ORG_NAME = "四川创意信息技术股份有限公司";
    private final static Long DEFAULT_INIT_SM_ORG_ORDER_CODE = Long.valueOf(1);

    private final static String DEFAULT_INIT_SM_USER_USER_NAME = "系统管理员";
    private final static String DEFAULT_INIT_SM_USER_LOGIN_NAME = "admin";
    private final static String DEFAULT_INIT_SM_USER_EMAIL = "admin@sc-troy.com";
    private final static Integer DEFAULT_INIT_SM_USER_TYPE = 1;

    private final static String DEFAULT_INIT_SM_POST_NAME = "系统管理员";
    private final static String DEFAULT_INIT_SM_POST_DESC = "由系统自动创建的初始化岗位（可删除）";


    private final static String DEFAULT_INIT_SM_ROLE_NAME = "系统管理";
    private final static String DEFAULT_INIT_SM_ROLE_DESC = "由系统自动创建的初始化角色（可删除）";

    // 菜单管理
    private final static Integer DEFAULT_INIT_SM_MENU_TYPE = 1;
    // 系统管理-父级Default
    private final static String DEFAULT_INIT_SM_MENU_P_MENU_NAME = "系统管理";
    private final static Integer DEFAULT_INIT_SM_MENU_P_ORDER_CODE = 1;

    // 菜单角色关联
    private final static Integer DEFAULT_INIT_SM_ROLE_MENU_LEVEL = 1;

    // 新增用户默认密码
    private static final String DEFAULT_PASSWORD = "111111";


    /**
     * 检测系统管理是否存在数据
     *
     * @return
     */
    public boolean checkSystemDataNotNull() {
        log.info("============= 系统启动：正在检测系统管理数据 =============");
        if (checkSmOrg() && checkSmUser() && checkSmPost() && checkSmRole() && checkSmMenu()) {
            log.info("****** System Manager Data Is Not Null,System Manager Data Initialization Is Not Performed!!!");
            log.info("============= 系统启动：完成检测系统管理数据 =============");
            return true;
        } else {
            log.warn("****** System Manager Data Is Null,Prepare Initialization For System Manager!!!");
            log.info("============= 系统启动：完成检测系统管理数据 =============");
            return false;
        }
    }

    /**
     * 检测机构
     */
    private boolean checkSmOrg() {
        log.info("----- 正在检测机构表数据");
        List<SmOrg> orgList = commonRepository.findAll(SmOrg.class);
        return checkList(orgList);
    }

    /**
     * 检测用户
     *
     * @return
     */
    private boolean checkSmUser() {
        log.info("----- 正在检测用户表数据");
        List<SmUser> userList = commonRepository.findAll(SmUser.class);
        return checkList(userList);
    }

    /**
     * 检测岗位
     *
     * @return
     */
    private boolean checkSmPost() {
        log.info("----- 正在检测岗位表数据");
        List<SmPost> postList = commonRepository.findAll(SmPost.class);
        return checkList(postList);
    }

    /**
     * 检测角色
     *
     * @return
     */
    private boolean checkSmRole() {
        log.info("----- 正在检测角色表数据");
        List<SmRole> roleList = commonRepository.findAll(SmRole.class);
        return checkList(roleList);
    }

    /**
     * 检测菜单
     *
     * @return
     */
    private boolean checkSmMenu() {
        log.info("----- 正在检测菜单表数据");
        List<SmMenu> menuList = commonRepository.findAll(SmMenu.class);
        return checkList(menuList);
    }

    /**
     * 检测List
     *
     * @param list
     * @return
     */
    private static boolean checkList(List<?> list) {
        if (list.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 初始化系统管理数据
     */
    public void initSystemData() {
        Long orgId = initSmOrg();
        Long userId = initSmUser(orgId);
        Long postId = initSmPost(orgId);
        Long roleId = initSmRole();
        List<Long> menuIds = initSmMenu();
        saveRoleMenu(roleId, menuIds);
        savePostRole(roleId, postId);
        savePostUser(postId, userId);
    }

    /**
     * 初始化机构
     *
     * @return
     */
    private Long initSmOrg() {
        log.info("System Manager Data Initialization By SmOrg");
        SmOrg smOrg = new SmOrg();
        smOrg.setOrgName(DEFAULT_INIT_SM_ORG_NAME);
        smOrg.setOrderCode(DEFAULT_INIT_SM_ORG_ORDER_CODE);
        smOrg.setStatus(VALID_DATA_STATUS);
        commonRepository.add(smOrg);
        smOrg.setRelationship(smOrg.getId().toString());
        commonRepository.add(smOrg);
        return smOrg.getId();
    }

    /**
     * 初始化用户
     *
     * @param orgId
     * @return
     */
    private Long initSmUser(Long orgId) {
        log.info("System Manager Data Initialization By SmUser");
        SmUser smUser = new SmUser();
        smUser.setStatus(VALID_DATA_STATUS);
        smUser.setActivated(true);
        smUser.setPassword(getDefaultPassword());
        smUser.setOrgId(orgId);
        smUser.setUserName(DEFAULT_INIT_SM_USER_USER_NAME);
        smUser.setLoginName(DEFAULT_INIT_SM_USER_LOGIN_NAME);
        smUser.setLogin(DEFAULT_INIT_SM_USER_LOGIN_NAME);
        smUser.setEmail(DEFAULT_INIT_SM_USER_EMAIL);
        smUser.setType(DEFAULT_INIT_SM_USER_TYPE);
        commonRepository.add(smUser);
        return smUser.getId();
    }

    /**
     * 初始化岗位
     *
     * @param orgId
     * @return
     */
    private Long initSmPost(Long orgId) {
        log.info("System Manager Data Initialization By SmPost");
        SmPost smPost = new SmPost();
        smPost.setOrgId(orgId);
        smPost.setPostName(DEFAULT_INIT_SM_POST_NAME);
        smPost.setPostDesc(DEFAULT_INIT_SM_POST_DESC);
        smPost.setStatus(VALID_DATA_STATUS);
        commonRepository.add(smPost);
        return smPost.getId();
    }

    /**
     * 初始化角色
     *
     * @return
     */
    private Long initSmRole() {
        log.info("System Manager Data Initialization By SmRole");
        SmRole smRole = new SmRole();
        smRole.setStatus(VALID_DATA_STATUS);
        smRole.setRoleName(DEFAULT_INIT_SM_ROLE_NAME);
        smRole.setRoleDesc(DEFAULT_INIT_SM_ROLE_DESC);
        commonRepository.add(smRole);
        return smRole.getId();
    }

    /**
     * 初始化系统管理子菜单
     *
     * @return
     */
    private List<Long> initSmMenu() {
        log.info("System Manager Data Initialization By SmMenu");
        List<Long> menuIds = new ArrayList<>();
        // 创建系统管理父级
        Long parentMenuId = saveInitSystemMenuDataByParent();
        menuIds.add(parentMenuId);
        // 创建子级菜单
        menuIds.add(saveSystemMenuDataBySub(parentMenuId, "基础信息", DEFAULT_INIT_SM_MENU_TYPE, 1, "/system/org","sys_01"));
        menuIds.add(saveSystemMenuDataBySub(parentMenuId, "菜单管理", DEFAULT_INIT_SM_MENU_TYPE, 2, "/system/menu","sys_02"));
        menuIds.add(saveSystemMenuDataBySub(parentMenuId, "岗位管理", DEFAULT_INIT_SM_MENU_TYPE, 3, "/system/post","sys_03"));
        menuIds.add(saveSystemMenuDataBySub(parentMenuId, "角色管理", DEFAULT_INIT_SM_MENU_TYPE, 4, "/system/role","sys_04"));
        menuIds.add(saveSystemMenuDataBySub(parentMenuId, "权限管理", DEFAULT_INIT_SM_MENU_TYPE, 5, "/system/limit","sys_05"));
        menuIds.add(saveSystemMenuDataBySub(parentMenuId, "码表管理", DEFAULT_INIT_SM_MENU_TYPE, 6, "/system/codeTable","sys_06"));
        menuIds.add(saveSystemMenuDataBySub(parentMenuId, "批量岗位管理", DEFAULT_INIT_SM_MENU_TYPE, 7, "/system/batchpost","sys_07"));
        return menuIds;
    }

    /**
     * 创建系统管理父级主菜单
     *
     * @return
     */
    private Long saveInitSystemMenuDataByParent() {
        SmMenu smMenu = new SmMenu();
        smMenu.setMenuName(DEFAULT_INIT_SM_MENU_P_MENU_NAME);
        smMenu.setMenuType(DEFAULT_INIT_SM_MENU_TYPE);
        smMenu.setOrderCode(DEFAULT_INIT_SM_MENU_P_ORDER_CODE);
        smMenu.setStatus(VALID_DATA_STATUS);
        commonRepository.add(smMenu);
        smMenu.setRelationship(smMenu.getId().toString());
        commonRepository.add(smMenu);
        return smMenu.getId();
    }

    /**
     * 创建系统管理子级主菜单
     *
     * @param parentMenuId
     * @param menuName
     * @param menuType
     * @param orderCode
     * @return
     */
    private Long saveSystemMenuDataBySub(Long parentMenuId, String menuName, Integer menuType, Integer orderCode, String menuUrl,String menuCode) {
        SmMenu smMenu = new SmMenu();
        smMenu.setpId(parentMenuId);
        smMenu.setMenuName(menuName);
        smMenu.setMenuType(menuType);
        smMenu.setOrderCode(orderCode);
        smMenu.setMenuUrl(menuUrl);
        smMenu.setMenuCode(menuCode);
        smMenu.setStatus(VALID_DATA_STATUS);
        commonRepository.add(smMenu);
        StringBuffer sb = new StringBuffer(parentMenuId.toString());
        sb.append("-");
        sb.append(smMenu.getId());
        smMenu.setRelationship(sb.toString());
        commonRepository.add(smMenu);
        return smMenu.getId();
    }

    /**
     * 保存角色菜单关联
     */
    private void saveRoleMenu(Long roleId, List<Long> menusIds) {
        log.info("System Manager Data Initialization By SmRoleMenu");
        for (int i = 0; i < menusIds.size(); i++) {
            SmRoleMenu smRoleMenu = new SmRoleMenu();
            smRoleMenu.setRoleId(roleId);
            smRoleMenu.setMenuId(menusIds.get(i));
            smRoleMenu.setLevel(DEFAULT_INIT_SM_ROLE_MENU_LEVEL);
            commonRepository.add(smRoleMenu);
        }
    }

    /**
     * 保存角色岗位关联
     *
     * @param roleId
     * @param postId
     */
    private void savePostRole(Long roleId, Long postId) {
        log.info("System Manager Data Initialization By SmPostRole");
        SmPostRole smPostRole = new SmPostRole();
        smPostRole.setRoleId(roleId);
        smPostRole.setPostId(postId);
        commonRepository.add(smPostRole);
    }

    /**
     * 保存岗位用户关联
     *
     * @param postId
     * @param userId
     */
    private void savePostUser(Long postId, Long userId) {
        log.info("System Manager Data Initialization By SmPostUser");
        SmPostUser smPostUser = new SmPostUser();
        smPostUser.setPostId(postId);
        smPostUser.setUserId(userId);
        commonRepository.add(smPostUser);
    }

    /**
     * 获取默认初始密码
     *
     * @return
     */
    private String getDefaultPassword() {
        return passwordEncoder.encode(DEFAULT_PASSWORD);
    }
}
