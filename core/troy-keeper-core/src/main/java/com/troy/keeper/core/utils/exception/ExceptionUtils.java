package com.troy.keeper.core.utils.exception;

import com.troy.keeper.core.error.KeeperException;

/**
 * Created by Administrator on 2017/8/10.
 */
public class ExceptionUtils {
    private static final int EXCEPTION_CODE_VALIDATE=430;

    /**
     * 获取验证异常
     * @param msg
     * @return
     */
    public static KeeperException getValidateException(String msg){
         return  new KeeperException(EXCEPTION_CODE_VALIDATE,msg);
    }
}
