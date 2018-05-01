package com.troy.keeper.system.config;

/**
 * Created by yg on 2017/6/23.
 * Updated By SimonChu On 2017/06/26
 */
public class Const {
    private Const() {

    }

    // 系统管理基础信息
    public static final String VALID_DATA_STATUS = "1";
    public static final String INVALID_DATA_STATUS = "0";
    public static final Integer DEFAULT_PAGE_SIZE_NUM = 20;
    public static final String MSG_COMMON_PAGE_ERROR = "未传入页数参数，请检查后重试！";


    // 系统层错误
    public static final String CODE_200 = "200";
    public static final String CODE_201 = "201";
    public static final String CODE_202 = "202";
    public static final String MSG_CODE_200 = "操作成功";
    public static final String MSG_CODE_201 = "系统内部错误";
    // 业务层错误：码表管理
    public static final String MSG_CODE_400_CODETABLE_DICCODE = "请求参数dicCode错误";
    public static final String MSG_CODE_400_CODETABLE_DICVALUE = "请求参数dicValue错误";
    public static final String MSG_CODE_400_CODETABLE_ORDERCODE = "请求参数orderCode错误";
    public static final String MSG_CODE_400_CODETABLE_MEMO = "请求参数memo错误";
    public static final String MSG_CODE_400_CODETABLE_DICCODE_OR_ORDERCODE_OR_MEMO = "请求参数dicValue、orderCode、memo错误";
    public static final String MSG_CODE_400_CODETABLE_REPORT_ERROR = "参数dicCode,存在重复";
    // 业务层错误：用户管理
    public static final String MSG_CODE_202_USER_PAGE = "page参数为空";
    public static final String MSG_CODE_202_USER_ID = "id参数为空";
    //public static final String MSG_CODE_202_USER_ORGID = "orgId参数为空";
    public static final String MSG_CODE_202_USER_ORGID = "机构参数为空";
    //public static final String MSG_CODE_202_USER_USER_NAME = "userName参数为空";
    public static final String MSG_CODE_202_USER_USER_NAME = "用户名称为空";
    public static final String MSG_CODE_202_USER_TYPE = "type参数为空";
    //public static final String MSG_CODE_202_USER_LOGIN_NAME = "loginName参数为空";
    public static final String MSG_CODE_202_USER_LOGIN_NAME = "用户登录账户为空";
    //public static final String MSG_CODE_202_USER_EMAIL = "email参数为空";
    public static final String MSG_CODE_202_USER_EMAIL = "邮箱地址为空";
    //public static final String MSG_CODE_202_USER_EMAIL_REPEAT = "email参数重复";
    public static final String MSG_CODE_202_USER_EMAIL_REPEAT = "邮箱地址存在重复";
    //public static final String MSG_CODE_202_USER_LOGIN_NAME_REPEAT = "loginName参数重复";
    public static final String MSG_CODE_202_USER_LOGIN_NAME_REPEAT = "用户登录账户重复";
    public static final String MSG_CODE_202_USER_OLD_PASSWORD = "旧密码为空";
    public static final String MSG_CODE_202_USER_NEW_PASSWORD = "新密码为空";
    public static final String MSG_CODE_202_USER_NOT_USER_INFO = "无该id用户信息";
    //public static final String MSG_CODE_202_USER_NEW_AND_OLDPASSWORD_ERROR = "oldPassword参数与当前Password不一致";
    public static final String MSG_CODE_202_USER_NEW_AND_OLDPASSWORD_ERROR = "旧密码与当前使用密码不一致";
    public static final String MSG_CODE_202_USER_ORG_AND_USER_TREE_ERROR = "数据可能存在残缺，用户机构树生成失败";
    // 业务层错误：角色管理
    public static final String MSG_CODE_202_ROLE_PAGE = "page参数为空";
    public static final String MSG_CODE_202_ROLE_ID = "id参数为空";
    public static final String MSG_CODE_202_ROLE_ROLE_NAME = "roleName参数为空";
    public static final String MSG_CODE_202_ROLE_ROLE_ID = "roleId参数为空";
    public static final String MSG_CODE_202_ROLE_MENUS = "menus参数为空";
    public static final String MSG_CODE_202_ROLE_MENU_ID = "menuId参数为空";
    public static final String MSG_CODE_202_ROLE_POST_DEL_ERROR = "该角色与岗位关联，请删除关联后重试操作！";
    public static final String MSG_CODE_202_ROLE_ROLE_CODE = "roleCode参数为空";
    public static final String MSG_CODE_202_ROLE_ROLE_CODE_EXIT_ERROR = "角色编码重复";
    // 业务层错误：岗位管理
    public static final String MSG_CODE_202_POST_ORG_ID = "orgId参数为空";
    public static final String MSG_CODE_202_POST_POST_NAME = "postName参数为空";
    public static final String MSG_CODE_202_POST_ID = "id参数为空";
    public static final String MSG_CODE_202_POST_POST_ID = "postId参数为空";
    public static final String MSG_CODE_202_POST_USER_DEL_ERROR = "该岗位已与用户关联，请删除关联后重试操作！";
    public static final String MSG_CODE_202_POST_CODE_EXIT_ERROR = "该岗位编码已经存在！";
    public static final String MSG_CODE_202_POST_CODE_ERROR = "岗位编码不能为空！";
    // 业务层错误：机构管理
    public static final String MSG_CODE_202_ORG_PAGE = "page参数为空";
    public static final String MSG_CODE_202_ORG_ID = "id参数为空";
    public static final String MSG_CODE_202_ORG_ORG_NAME = "orgName参数未传入";
    public static final String MSG_CODE_202_ORG_ORDER_CODE = "orderCode参数未传入";
    public static final String MSG_CODE_202_ORG_UPDATE_PID_EQUALS_ID = "不能将当前机构设为父机构";
    public static final String MSG_CODE_202_ORG_DEL_DATA_PID = "该机构下存在子机构，请删除子机构后重试";
    public static final String MSG_CODE_202_ORG_DEL_USER = "该机构下存在用户关联，请联系管理员处理";
    // 业务层错误：菜单管理
    public static final String MSG_CODE_202_MENU_PAGE = "page参数为空";
    public static final String MSG_CODE_202_MENU_ID = "id参数未传入";
    public static final String MSG_CODE_202_MENU_MENU_NAME = "menuName参数未传入";
    public static final String MSG_CODE_202_MENU_ORDER_CODE = "orderCode参数未传入";
    public static final String MSG_CODE_202_MENU_TYPE = "menuType参数未传入";
    public static final String MSG_CODE_202_MENU_UPDATE_ID_EQUALS_PID = "不能将当前菜单设为父菜单";
    public static final String MSG_CODE_202_MENU_DEL_DATA_PID = "该菜单下存在子菜单，请删除子菜单后重试";
    public static final String MSG_CODE_202_MENU_CODE = "menuCode参数未传入";
    public static final String MSG_CODE_202_MENU_CODE_EXIT_ERROR = "菜单编码重复";
    // 业务层错误：系统登录
    public static final String MSG_CODE_202_LOGIN_IMAGE_ERROR = "image生成失败";
    public static final String MSG_CODE_202_LOGIN_CODE = "code参数未传入";
    public static final String MSG_CODE_202_LOGIN_LOGIN_OUT = "该用户已登录失效";
    public static final String MSG_CODE_202_LOGIN_CURRENT_POST_ID = "传入的选定岗位ID参数为空";
    public static final String MSG_CODE_202_LOGIN_CODE_CHECK_ERROR = "验证码错误";
    // 业务层错误：权限管理
    public static final String MSG_CODE_202_LIMIT_POST_ID = "postId参数未传入";
    public static final String MSG_CODE_202_LIMIT_USER_ID = "userId参数未传入";

}
