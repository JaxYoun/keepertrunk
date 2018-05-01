package com.troy.keeper.excelimport.config;

/**
 * Created by SimonChu on 2017/6/28.
 */
public class Const {
    /**
     * CODE List
     */

    private Const() {
    }

    // 正常状态
    public static final String CODE_200 = "200";
    // 未知系统错误状态
    public static final String CODE_201 = "201";
    // 业务一般错误状态
    public static final String CODE_202 = "202";
    // 未找到
    public static final String CODE_404 = "404";
    // 业务严重错误状态
    public static final String CODE_500 = "500";
    /**
     * CODE MSG
     */
    public static final String MSG_UNKNOWN_SYS_ERROR = "未知的系统错误";
    public static final String MSG_SUCCESSFUL_OPERATION = "操作成功";
    public static final String MSG_MAP_TO_OBJECT_ERROR = "map转对象失败";
}
