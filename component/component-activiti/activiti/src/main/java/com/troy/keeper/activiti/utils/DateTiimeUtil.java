package com.troy.keeper.activiti.utils;

/**
 * Date:     2017/7/27 10:08<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public class DateTiimeUtil {
    public static String getDateTimeString(long dateTime){
        long days = dateTime / 1000L / (24 * 60* 60);
        long h = dateTime / 1000L % (24 * 60* 60) / (60* 60);
        long m = dateTime / 1000L % (24 * 60* 60) % (60* 60) / (60);
        long s = dateTime / 1000L % (24 * 60* 60) % (60* 60) % (60);
        String format = days==0?"%02d时%02d分%02d秒":"%d天 %02d时%02d分%02d秒";
        return  days==0?String.format(format, h, m, s):String.format(format, days, h, m, s);
    }
}
