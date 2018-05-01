package com.troy.keeper.monomer.demo.utils;

/**
 * util 都写在util工程，这是个错误的
 */
public class StringUtil {
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
