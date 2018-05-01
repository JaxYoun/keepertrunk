package com.troy.keeper.util;

/**
 * Created by yg on 2017/4/18.
 */
public class StringUtils {
    /**
     * 某字符串包含另一个字符串的个数
     * @param str1 str2
     * @return
     */
    public static int stringNumbers(String str1, String str2, int counter)
    {
        if (str1.indexOf(str2) == -1)
        {
            return counter;
        }
        else
        {
            return stringNumbers(str1.substring(0, str1.lastIndexOf(str2)), str2, counter + 1);
        }
    }
}
