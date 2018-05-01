
package com.troy.keeper.util;

import com.google.common.collect.Lists;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * yjm
 */
public class DateUtils {
//    private static final Logger log= LoggerFactory.getLogger(DateUtils.class);
	// 用来全局控制 上一周，本周，下一周的周数变化
	private int weeks = 0;
	private int MaxDate;// 一月最大天数
	private int MaxYear;// 一年最大天数

	// 设置所要取得的格式,需要时在这里设置格式即可
	public static final String StandardDate = "yyyy-MM-dd";
    public static final String MonthDay = "MM-dd";
    public static final String StandardYearMonthDate = "yyyy-MM";

    public static final String StandardDay = "yyyy-MM";

	public static final String StandardDateTime = "yyyy-MM-dd HH:mm:ss";

	public static final String CompactDateTime = "yyyyMMddHHmmss";

	private static final String StandardYear = "yyyy";

	private static final String CompactDate = "yyyyMMdd";

	@SuppressWarnings("unused")
	private static final String FileYearMonth = "yyyy/MM";

    private static String patternYearMonthDay = "yyyy年MM月dd日";
    private static String patternDate = "yyyy-MM-dd";
    private static String patternDateTime = "yyyy-MM-dd HH:mm:ss";
    public static final String patternDateTimeNote = "yyyy-MM-dd HH:mm";
    public static final String patternDateTimeHhMm = "HH:mm";
	// 取得系统当前时间
	public static Date getCurrentDate() {
		return new Date();
	}

	// 根据指定格式将输出时间
	public static String getFdate(Date date, String style) {
		SimpleDateFormat dFormat = new SimpleDateFormat(style);
		if (date == null)
			return null;
		String datetime = dFormat.format(date);
		return datetime;
	}

	// 获取当天时间并按指定格式输出
	public static String getNowTime(String dateformat) {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);// 可以方便地修改日期格式
		String formatNow = dateFormat.format(now);
		return formatNow;
	}

	// 取得系统当前时间并按yyyy-MM-dd格式化输出，
	public static String getNowTime() {
		SimpleDateFormat dFormat = new SimpleDateFormat(StandardDate);
		String datetime = dFormat.format(getCurrentDate());
		return datetime;
	}

	// 取得系统当前时间的年并按yyyy格式化输出，
	public static String getYear() {
		Date date = new Date();
		SimpleDateFormat dFormat = new SimpleDateFormat(StandardYear);
		String dateYear = dFormat.format(date);
		return dateYear;
	}

	// 取得系统当前时间的年月并按yyyy-MM格式化输出，
	public static String getYMonth() {
		Date date = new Date();
		SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM");
		String dateYM = dFormat.format(date);
		return dateYM;
	}

	// 将date类型转换为String类型
	public static String dateToString(Date date) {
		String dateStr = "";
		if (date != null) {
			SimpleDateFormat sf = new SimpleDateFormat(StandardDate);
			dateStr = sf.format(date);
		}
		return dateStr;
	}

    // 将date类型转换为自定义格式类型
    public static String dateToString(Date date,String formatter) {
        String dateStr = "";
        if (date != null) {
            SimpleDateFormat sf = new SimpleDateFormat(formatter);
            dateStr = sf.format(date);
        }
        return dateStr;
    }

	// 将String类型转换为日期类型
	public static Date stringToDate(String str, String style) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern(style);
		Date date = null;
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

    /**
     *  获取 给定时间的 本周所有时间
     * @param date
     * @return
     */
    public static List<String> getWeekList(String date){
//        log.info("date value is"+date);
        List<String> strings = Lists.newArrayList();
        Calendar c = Calendar.getInstance();
        c.setTime(strToDate(date));
//        c.get(Calendar.DAY_OF_WEEK);
//        log.info("Calendar value is"+c.getTime().toString());
//        String week = getWeek(date);
//        log.info("week value is"+ week );
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                strings.add(dateToString(increaseDay(c.getTime(),-6)));
            case 7:
                strings.add(dateToString(increaseDay(c.getTime(),-5)));
            case 6:
                strings.add(dateToString(increaseDay(c.getTime(),-4)));
            case 5:
                strings.add(dateToString(increaseDay(c.getTime(),-3)));
            case 4:
                strings.add(dateToString(increaseDay(c.getTime(),-2)));
            case 3:
                strings.add(dateToString(increaseDay(c.getTime(),-1)));
            case 2:
                strings.add(dateToString(c.getTime()));
        }
        strings.sort(new Comparator<String>() {
           @Override
           public int compare(String o1, String o2) {
               return (DateUtils.strToDate(o2).getTime() < DateUtils.strToDate(o1).getTime())?-1:1;
           }
       });
        return strings;
    }

	//将String类型转换为日期类型
	public static Date stringToDateForException(String str, String style) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern(style);
		Date date = null;
		if(str==null){
			return null;
		}
		date = sdf.parse(str);
		return date;
	}

	// 根据Private提供的StandarYear返回所需要的格式SimpleDateFormat
	private static SimpleDateFormat getStandardYearFormat() {
		return getDateTimeFormat(StandardYear);
	}

	/**
	 *  根据参数中定义的日期格式来转换
	 * @param pattern
	 * @return SimpleDateFormat
	 */
	private static SimpleDateFormat getDateTimeFormat(String pattern) {
		SimpleDateFormat sf = new SimpleDateFormat(pattern);
		return sf;
	}

	// 返回系统所需时间
	public static String getCurrentYear() {
		SimpleDateFormat sf = getStandardYearFormat();
		return sf.format(getCurrentDate());
	}

	/**
	 * 得到二个日期间的间隔天数
	 */
	public static String getTwoDay(String day1, String day2) {
		SimpleDateFormat myFormatter = new SimpleDateFormat(StandardDate);
		long day = 0;
		try {
			Date date = myFormatter.parse(day1);
			Date mydate = myFormatter.parse(day2);
			day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
		} catch (Exception e) {
			return "";
		}
		return day + "";
	}

	/**
	 * 根据一个日期，返回是星期几的字符串
	 *
	 * @param sdate
	 * @return
	 */
	public static String getWeek(String sdate) {
		// 再转换为时间
		Date date = DateUtils.strToDate(sdate);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// int hour=c.get(Calendar.DAY_OF_WEEK);
		// hour中存的就是星期几了，其范围 1~7
		// 1=星期日 7=星期六，其他类推
		return new SimpleDateFormat("EEEE").format(c.getTime());
	}


    /**
     *  重构  -- 根据一个时间 返回是 星期几 字符串 （ 上面方法 在不同的服务器系统 返回的是 不同的格式）
     * @param date
     * @auth chenlaichun
     * @return String
     */
    public static String getWeek(Date date) {
        // 再转换为时间
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // 1=星期日 7=星期六，其他类推
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                return "星期日";
            case 7:
                return "星期六";
            case 6:
                return "星期五";
            case 5:
                return "星期四";
            case 4:
                return "星期三";
            case 3:
                return "星期二";
            case 2:
                return "星期一";
        }
        return "";
    }

	/**
	 * 将短时间格式字符串转换为时间 yyyy-MM-dd
	 *
	 * @param strDate
	 * @return
	 */
	public static Date strToDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat(StandardDate);
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate,String formater) {
        SimpleDateFormat formatter = new SimpleDateFormat(formater);
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /*
    * 给一个date 和 时间格式 返回一个特定格式的date  如：得到一个yyyy-MM-dd格式date  需要传入date 和 "yyyy-MM-dd"字符串
    * */
    public static Date getDateByDateAndStrDate(Date date ,String strStyle) {
        return  DateUtils.stringToDate(DateUtils.getFdate(date, strStyle), strStyle) ;
    }

	/**
	 * 两个时间之间的天数
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long getDays(String date1, String date2) {
		if (date1 == null || date1.equals(""))
			return 0;
		if (date2 == null || date2.equals(""))
			return 0;
		// 转换为标准时间
		SimpleDateFormat myFormatter = new SimpleDateFormat(StandardDate);
		Date date = null;
		Date mydate = null;
		try {
			date = myFormatter.parse(date1);
			mydate = myFormatter.parse(date2);
		} catch (Exception e) {
		}
		long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
		return day;
	}

	// 计算当月最后一天,返回字符串
	public String getDefaultDay() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat(StandardDate);

		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.YEAR, 2014);
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.set(Calendar.MONTH, 11);
		lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
		lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天

		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 上月第一天
	public String getPreviousMonthFirst() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat(StandardDate);

		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.add(Calendar.MONTH, -1);// 减一个月，变为下月的1号
		// lastDate.add(Calendar.DATE,-1);//减去一天，变为当月最后一天

		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 获取当月第一天
	public String getFirstDayOfMonth() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat(StandardDate);

		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		str = sdf.format(lastDate.getTime());
		return str;
	}

    /**
     * @Title: getFirstDayOfMonthForDate
     * @Description: 获取当前月的第一天
     * @author guohongjin
     * @date 2016-12-22
     * @return Date
     */

    public static Date getFirstDayOfMonthForDate() {
        Calendar firstDate = Calendar.getInstance();
        firstDate.set(Calendar.DATE, 1);// 设为当前月的1号
        return firstDate.getTime();
    }

	// 获得本周星期日的日期
	public String getCurrentWeekday() {
		weeks = 0;
		int mondayPlus = this.getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
		Date monday = currentDate.getTime();

		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	// 获得当前日期与本周日相差的天数
	private int getMondayPlus() {
		Calendar cd = Calendar.getInstance();
		// 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
		if (dayOfWeek == 1) {
			return 0;
		} else {
			return 1 - dayOfWeek;
		}
	}

	// 获得本周一的日期
	public String getMondayOFWeek() {
		weeks = 0;
		int mondayPlus = this.getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus);
		Date monday = currentDate.getTime();

		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	// 获得相应周的周六的日期
	public String getSaturday() {
		int mondayPlus = this.getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks + 6);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	// 获得上周星期日的日期
	public String getPreviousWeekSunday() {
		weeks = 0;
		weeks--;
		int mondayPlus = this.getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + weeks);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	// 获得上周星期一的日期
	public String getPreviousWeekday() {
		weeks--;
		int mondayPlus = this.getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	// 获得下周星期一的日期
	public String getNextMonday() {
		weeks++;
		int mondayPlus = this.getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	// 获得下周星期日的日期
	public String getNextSunday() {

		int mondayPlus = this.getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 + 6);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}
    // 获得上一周周日的日期 注：周六周日需要减一天 因为外国人周日是每周的第一天
    public static String getNextSundayByDate(Date date) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        int dayWeek = cd.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        if(1 == dayWeek) {
            cd.add(Calendar.DAY_OF_MONTH, -1);
        }
        cd.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天
        int day = cd.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        cd.add(Calendar.DATE, cd.getFirstDayOfWeek()-day + 6);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        return dateToString(cd.getTime());
    }

	@SuppressWarnings("unused")
	private int getMonthPlus() {
		Calendar cd = Calendar.getInstance();
		int monthOfNumber = cd.get(Calendar.DAY_OF_MONTH);
		cd.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		cd.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		MaxDate = cd.get(Calendar.DATE);
		if (monthOfNumber == 1) {
			return -MaxDate;
		} else {
			return 1 - monthOfNumber;
		}
	}

	// 获得上月最后一天的日期
	public String getPreviousMonthEnd() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat(StandardDate);

		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.MONTH, -1);// 减一个月
		lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 获得下个月第一天的日期
	public String getNextMonthFirst() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat(StandardDate);

		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.MONTH, 1);// 减一个月
		lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 获得下个月最后一天的日期
	public String getNextMonthEnd() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat(StandardDate);

		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.MONTH, 1);// 加一个月
		lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 获得明年最后一天的日期
	public String getNextYearEnd() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat(StandardDate);

		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.YEAR, 1);// 加一个年
		lastDate.set(Calendar.DAY_OF_YEAR, 1);
		lastDate.roll(Calendar.DAY_OF_YEAR, -1);
		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 获得明年第一天的日期
	public String getNextYearFirst() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat(StandardDate);

		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.YEAR, 1);// 加一个年
		lastDate.set(Calendar.DAY_OF_YEAR, 1);
		str = sdf.format(lastDate.getTime());
		return str;

	}

	// 获得本年有多少天
	@SuppressWarnings("unused")
	private int getMaxYear() {
		Calendar cd = Calendar.getInstance();
		cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
		cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
		int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
		return MaxYear;
	}

	private int getYearPlus() {
		Calendar cd = Calendar.getInstance();
		int yearOfNumber = cd.get(Calendar.DAY_OF_YEAR);// 获得当天是一年中的第几天
		cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
		cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
		int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
		if (yearOfNumber == 1) {
			return -MaxYear;
		} else {
			return 1 - yearOfNumber;
		}
	}

	// 获得本年第一天的日期
	public String getCurrentYearFirst() {
		int yearPlus = this.getYearPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, yearPlus);
		Date yearDay = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preYearDay = df.format(yearDay);
		return preYearDay;
	}

	// 获得本年最后一天的日期 *
	public String getCurrentYearEnd() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(StandardYear);// 可以方便地修改日期格式
		String years = dateFormat.format(date);
		return years + "-12-31";
	}

	// 获得上年第一天的日期 *
	public String getPreviousYearFirst() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(StandardYear);// 可以方便地修改日期格式
		String years = dateFormat.format(date);
		int years_value = Integer.parseInt(years);
		years_value--;
		return years_value + "-1-1";
	}

	// 获得上年最后一天的日期
	public String getPreviousYearEnd() {
		weeks--;
		int yearPlus = this.getYearPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, yearPlus + MaxYear * weeks
				+ (MaxYear - 1));
		Date yearDay = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preYearDay = df.format(yearDay);
		getThisSeasonTime(11);
		return preYearDay;
	}

	// 获得本季度
	public String getThisSeasonTime(int month) {
		int array[][] = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } };
		int season = 1;
		if (month >= 1 && month <= 3) {
			season = 1;
		}
		if (month >= 4 && month <= 6) {
			season = 2;
		}
		if (month >= 7 && month <= 9) {
			season = 3;
		}
		if (month >= 10 && month <= 12) {
			season = 4;
		}
		int start_month = array[season - 1][0];
		int end_month = array[season - 1][2];

		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(StandardYear);// 可以方便地修改日期格式
		String years = dateFormat.format(date);
		int years_value = Integer.parseInt(years);

		int start_days = 1;// years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);
		int end_days = getLastDayOfMonth(years_value, end_month);
		String seasonDate = years_value + "-" + start_month + "-" + start_days
				+ ";" + years_value + "-" + end_month + "-" + end_days;
		return seasonDate;

	}

	/**
	 * 获取某年某月的最后一天
	 *
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @return 最后一天
	 */
	private int getLastDayOfMonth(int year, int month) {
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
				|| month == 10 || month == 12) {
			return 31;
		}
		if (month == 4 || month == 6 || month == 9 || month == 11) {
			return 30;
		}
		if (month == 2) {
			if (isLeapYear(year)) {
				return 29;
			} else {
				return 28;
			}
		}
		return 0;
	}

	/**
	 * 是否闰年
	 *
	 * @param year
	 *            年
	 * @return
	 */
	public boolean isLeapYear(int year) {
		return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
	}

	/**
	 * 计算日期为星期几
	 *
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static int currentDateOfweek(Date date) throws Exception {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}

	/**
	 * 计算两个日期之间相差多少周,包括起始日期，比如起始日期在周六 ，那么这两天一算一周，结束日期在周一，那么这一天也算一周
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	public static int getTotalweeks(Date startDate, Date endDate)
			throws Exception {
		long start = startDate.getTime();
		long end = endDate.getTime();
		int startend = (int) ((end - start) / (1000 * 60 * 60 * 24) + 1);
		startend = startend - (7 - currentDateOfweek(startDate)) - 1;
		if ((7 - currentDateOfweek(endDate)) != 0) {
			startend = startend - currentDateOfweek(endDate);
		}
		int w = startend / 7 + 1;
		if ((7 - currentDateOfweek(endDate)) != 0) {
			w = w + 1;
		}
		return w;
	}

	/**
	 * 根据开始日期计算当前日期是当前年或者当前学期的第几周
	 *
	 * @param beginDate
	 *            开始日期
	 * @param currentDate
	 *            当前日期
	 * @return 周次
	 */
	public static Integer getTermWeek(Date beginDate, Date currentDate) {
		if (beginDate == null || currentDate == null) {
			return -1;
		}
		long currentMills = currentDate.getTime();
		long termweekMills = beginDate.getTime();
		if (currentMills < termweekMills) {
			return -1;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(beginDate);
		int dayofWeek = calendar.get(Calendar.DAY_OF_WEEK);
		// 计算出天数
		float countday = (currentMills - termweekMills) / (1000 * 60 * 60 * 24)
				- (7 - dayofWeek);
		int countweek = (int) (countday / 7) + 1;
		int modcountweek = ((int) countday) % 7;
		if (modcountweek != 0) {
			countweek += 1;
		}
		if (countweek < 1) {
			countweek = 0;
		}
		return countweek;
	}

	/**
	 * 得到date的月份天数
	 *
	 * @desc
	 * @param date
	 * @return
	 */
	public static int getMonthDays(Date date) {
		Calendar time = Calendar.getInstance();
		time.clear();
		time.setTime(date);
		int day = time.getActualMaximum(Calendar.DAY_OF_MONTH);// 本月份的天数
		return day;
	}

	/**
	 * 得到将date增加指定天数后的date
	 *
	 * @param date
	 *            日期
	 * @param intBetween
	 *            增加的天数
	 * @return date 加上intBetween天数后的日期
	 */
	public static Date increaseDay(Date date, int intBetween) {
		Calendar calo = Calendar.getInstance();
		calo.setTime(date);
		calo.add(Calendar.DATE, intBetween);
		return calo.getTime();
		// return addDays(date, intBetween);
	}

	/**
	 * 得到将date增加指定月数后的date
	 *
	 * @param date
	 *            日期
	 * @param intBetween
	 *            增加的月份
	 * @return date 加上intBetween月数后的日期
	 */
	public static Date increaseMonth(Date date, int intBetween) {
		Calendar calo = Calendar.getInstance();
		calo.setTime(date);
		calo.add(Calendar.MONTH, intBetween);
		return calo.getTime();
		// return addMonths(date, intBetween);
	}

	/**
	 * 得到将date增加指定年数后的date
	 *
	 * @param date
	 *            日期
	 * @param intBetween
	 *            增加的年数
	 * @return date加上intBetween年数后的日期
	 */
	public static Date increaseYear(Date date, int intBetween) {
		Calendar calo = Calendar.getInstance();
		calo.setTime(date);
		calo.add(Calendar.YEAR, intBetween);
		return calo.getTime();
		// return addYears(date, intBetween);
	}

	/**
	 * 得到将date增加指定小时数后的date
	 *
	 * @param date
	 *            日期
	 * @param intBetween
	 *            增加的小时数
	 * @return date 加上intBetween小时数后的日期
	 */
	public static Date increaseHours(Date date, int intBetween) {
		Calendar calo = Calendar.getInstance();
		calo.setTime(date);
		calo.add(Calendar.HOUR, intBetween);
		return calo.getTime();
		// return addHours(date, intBetween);
	}

	public static Date addYears(Date date, int amount) {
		return add(date, 1, amount);
	}

	public static Date addMonths(Date date, int amount) {
		return add(date, 2, amount);
	}

	public static Date addWeeks(Date date, int amount) {
		return add(date, 3, amount);
	}

	public static Date addDays(Date date, int amount) {
		return add(date, 5, amount);
	}

	public static Date addHours(Date date, int amount) {
		return add(date, 11, amount);
	}

	public static Date addMinutes(Date date, int amount) {
		return add(date, 12, amount);
	}

	public static Date addSeconds(Date date, int amount) {
		return add(date, 13, amount);
	}

	public static Date addMilliseconds(Date date, int amount) {
		return add(date, 14, amount);
	}

	public static Date add(Date date, int calendarField, int amount) {
		if (date == null)
			throw new IllegalArgumentException("The date must not be null");

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(calendarField, amount);
		return c.getTime();
	}

	/**
	 * 判断一个日期是否在两个日期之间
	 *
	 * @author 滕辉
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @param date
	 *            指定日期
	 * @return
	 */
	public static boolean between(Date startDate, Date endDate, Date date) {
        if(date == null){
            return false;
        }else if (startDate == null && endDate == null) {
			return true;
		} else if (startDate != null && startDate.getTime() <= date.getTime()
				&& endDate == null) {
			return true;
		} else if (endDate != null && endDate.getTime() >= date.getTime()
				&& startDate == null) {
			return true;
		} else if (startDate != null && endDate != null
				&& startDate.getTime() <= date.getTime()
				&& endDate.getTime() >= date.getTime()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取当前日期是星期几的文字描述
	 *
	 * @author cui
	 * @date Jun 26, 2009
	 * @return
	 */
	public static String getWeekText() {
		String txt = "星期";
		int weekday;
		Calendar tmp = Calendar.getInstance();
		weekday = tmp.get(Calendar.DAY_OF_WEEK) - 1;
		switch (weekday) {
		case 1:
			txt += "一";
			break;
		case 2:
			txt += "二";
			break;
		case 3:
			txt += "三";
			break;
		case 4:
			txt += "四";
			break;
		case 5:
			txt += "五";
			break;
		case 6:
			txt += "六";
			break;
		case 0:
			txt += "日";
			break;
		}
		return txt;
	}


	/**
	 * @Title: getCurrentYearAndBeforeTen
	 * @Description: 获取当前年及前十年
	 * @return List<String>
	 */
	public static List<String> getCurrentYearAndBeforeTen(){
		List<String> dataList = new ArrayList<String>();
		Calendar tmp = Calendar.getInstance();
		 int year = tmp.get(Calendar.YEAR);    //获取年
		 for(int y=year;y>(year-10);y--){
			 dataList.add(y+"");
		 }
		return dataList;
	}
	@SuppressWarnings("static-access")
//	public static void main1(String[] args) {
//
//		DateUtils du = new DateUtils();
//		String dt = du.getNowTime();
//		System.out.println(dt);
//		String dd = du.getCurrentYear();
//		System.out.println(dd);
//
//		String df = du.getFdate(new Date(), CompactDate);
//		System.out.println(df);
//		System.out.println("============");
//		String dts = du.dateToString(new Date());
//		System.out.println(dts);
//		String s = "20100101";
//		Date d = du.stringToDate(s, CompactDate);
//		System.out.println(d);
//		DateUtils tt = new DateUtils();
//		System.out.println("获取当天日期:" + tt.getNowTime(StandardDate));
//		System.out.println("获取本周一日期:" + tt.getMondayOFWeek());
//		System.out.println("获取本周日的日期~:" + tt.getCurrentWeekday());
//		System.out.println("获取上周一日期:" + tt.getPreviousWeekday());
//		System.out.println("获取上周日日期:" + tt.getPreviousWeekSunday());
//		System.out.println("获取下周一日期:" + tt.getNextMonday());
//		System.out.println("获取下周日日期:" + tt.getNextSunday());
//		System.out.println("获得相应周的周六的日期:" + tt.getNowTime(StandardDate));
//		System.out.println("获取本月第一天日期:" + tt.getFirstDayOfMonth());
//		System.out.println("获取本月最后一天日期:" + tt.getDefaultDay());
//		System.out.println("获取上月第一天日期:" + tt.getPreviousMonthFirst());
//		System.out.println("获取上月最后一天的日期:" + tt.getPreviousMonthEnd());
//		System.out.println("获取下月第一天日期:" + tt.getNextMonthFirst());
//		System.out.println("获取下月最后一天日期:" + tt.getNextMonthEnd());
//		System.out.println("获取本年的第一天日期:" + tt.getCurrentYearFirst());
//		System.out.println("获取本年最后一天日期:" + tt.getCurrentYearEnd());
//		System.out.println("获取去年的第一天日期:" + tt.getPreviousYearFirst());
//		System.out.println("获取去年的最后一天日期:" + tt.getPreviousYearEnd());
//		System.out.println("获取明年第一天日期:" + tt.getNextYearFirst());
//		System.out.println("获取明年最后一天日期:" + tt.getNextYearEnd());
//		System.out.println("获取本季度第一天到最后一天:" + tt.getThisSeasonTime(11));
//		System.out.println("获取两个日期之间间隔天数2008-12-1~2008-9-29:"
//				+ DateUtils.getTwoDay("2008-12-1", "2008-9-29"));
//		System.out.println(du.getFdate(increaseDay(new Date(), 200),
//				CompactDate));
//		System.out.println(getWeekText());
//		List<String> currentYearAndBeforeTen = DateUtils.getCurrentYearAndBeforeTen();
//		System.out.println(currentYearAndBeforeTen);
//	}

	/**
     * @Title: isLessThanHour
     * @Description:判断某一时间与当前时间相差小时数是否小于hour
     * @param date 时间
     * @param hour 需要比较两个时间相差的小时数
     * @author ransheng
     * @return boolean
     */
    public static boolean isLessThanHour(Date date,int hour){
        long millisecond=new Date().getTime()-date.getTime();
        return millisecond<(1000*60*60*hour)?true:false;
    }

	/**
	 * @Title: getBetweenDateByHour
	 * @Description: 获取两个时间相差多少天零多少小时
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @return int
	 */
	public static String getBetweenDateByHour(Date beginDate,Date endDate){
		//开始时间
		long begDateLong=beginDate.getTime();
		//结束时间+一天的毫秒数
		long endDateLong=endDate.getTime()+86399000;
		long hour=(endDateLong-begDateLong)/(1000*60*60);
		long dayNum=hour/24;
		long hourNum=hour%24;
		//判断天数是否为0
		if (dayNum>=0l && hourNum>=0l) {
			if (dayNum==0l) {
				if (hourNum==0l) {
					return "<font style='color:red;'>时间已过</font>";
				}else {
					return "<font style='color:red;'>仅剩下"+hourNum+"个小时</font>";
				}
			}else {
				if (hourNum==0l) {
					return "还有"+dayNum+"天";
				}else {
					return "还有"+dayNum+"天"+hourNum+"个小时";
				}
			}
		}else {
			return "<font style='color:red;'>时间已过</font>";
		}

	};

    /**
     * @Title: getBetweenDateByHourReturnDouble
     * @Description: 获取两个时间相差多少天零多少小时
     * @param beginDate 开始时间
     * @param endDate 结束时间
     * @return Double
     */
    public static Double getBetweenDateByHourReturnDouble(Date beginDate,Date endDate) {
        //开始时间
        long begDateLong = beginDate.getTime();
        //结束时间+一天的毫秒数
        long endDateLong = endDate.getTime() + 86399000;
        long hour = (endDateLong - begDateLong) / (1000 * 60 * 60);

        //计算两个时间相差的天数
        return (hour*1.00)/24;

    }
	/**
	 * 获取时间的年份
	 */
	public static int getYearByTime(Date date){
		Calendar lastDate = Calendar.getInstance();
		lastDate.setTime(date);
		return lastDate.get(Calendar.YEAR);
	}

	/**
	 * @Title: getTimeByYmd
	 * @Description: 根据年月日生成日期
	 * @param year 年 2014
	 * @param montch 月 5,12
	 * @param day 日 1,10,20
	 * @return Date
	 */
	public static Date getTimeByYmd(int year,int montch,int day){
		String date_Str=""+year;
		if (montch<10) {
			date_Str=date_Str+"-0"+montch;
		}else {
			date_Str=date_Str+"-"+montch;
		}
		if (day<10) {
			date_Str=date_Str+"-0"+day;
		}else {
			date_Str=date_Str+"-"+day;
		}
		//初始化一个时间
		Date date=new Date();
		try {
			date = DateUtils.parseDate(date_Str);
		} catch (ParseException e) {

			e.printStackTrace();
		}
		return date;
	}


    /**
     * 获取当前时间的日期yyyy-MM-dd
     * @Title: getNowDate
     * @Description: 方法描述
     * @return
     * @throws ParseException
     */
    public static Date getNowDate() throws ParseException{
        Date now = new Date();
        String time = formatDate(now);
        return parseDate(time);
    }

    /**
     * @param date 指定日期
     * @param few  指定相距多少个月，可以为正数or负数,0表示当月月份
     * @param day  指定月份中的天数
     * @param flag 日期格式标志,1表示2015-05-25 00:00:00.0；0表示2015-05-25 59:59:59.0格式
     * @return Date 格式为:yyyy-MM-(月份最后一天)
     * @Title: getDateAfterFewMonth
     * @Description: 获取距离指定日期+(-)n个月
     * @Date 2015-05-12
     */
    public static Date getDateAfterFewMonth(Date date, Integer few, Integer day,int flag) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (null != few)
            calendar.add(Calendar.MONTH, few);
        if (null != day)
            calendar.set(Calendar.DAY_OF_MONTH, day);
        if (flag == 0) {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 59);
        }
        return calendar.getTime();
    }


    public static Date parseDate(String date) throws ParseException{
        SimpleDateFormat sdf= new SimpleDateFormat(patternDate);
        return sdf.parse(date);
    }

    public static Date parseDateTime(String dateTime) throws ParseException{
        SimpleDateFormat sdf= new SimpleDateFormat(patternDateTime);
        return sdf.parse(dateTime);
    }

    public static String formatDate(Date date) throws ParseException{
        SimpleDateFormat sdf= new SimpleDateFormat(patternDate);
        return sdf.format(date);
    }

    public static String formatStrDate(Date date) throws ParseException{
        SimpleDateFormat sdf= new SimpleDateFormat(patternYearMonthDay);
        return sdf.format(date);
    }

    public static String formatDateTime(Date date) throws ParseException{
        SimpleDateFormat sdf= new SimpleDateFormat(patternDateTime);
        return sdf.format(date);
    }
    public static Date parseDateTimeNote(String dateTime) throws ParseException{
        SimpleDateFormat sdf= new SimpleDateFormat(patternDateTimeNote);
        return sdf.parse(dateTime);
    }
    public static String formatDateTimeNote(Date date) throws ParseException{
        SimpleDateFormat sdf= new SimpleDateFormat(patternDateTimeNote);
        return sdf.format(date);
    }

    /**
     * @Title: getDaysByDate
     * @Description: 获取两个时间类型的天数差
     * @throw ParseException
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return  long
     */

    public static long getDaysByDate(Date startDate,Date endDate) throws ParseException{
        long day = (endDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000);

        return day;
    }


    /**
     * @Title: getDaysByDateTime
     * @Description: 获取两个时间类型的天数差
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return  Double
     */

    public static Double getDaysByDateTime(Date startDate,Date endDate) {
        Double day = ((double)(endDate.getTime() - startDate.getTime())) / (24 * 60 * 60 * 1000);

        return day;
    }

    /**
     * @Title: getMonthByDate
     * @Description: 获取时间的月份
     * @throw ParseException
     */

    public static int getMonthByDate(Date date)throws ParseException{
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH)+1;
    }

    /**
     * @Title: getDayByDate
     * @Description: 获取指定时间的天数
     * @throw ParseException
     */

    public static int getDayByDate(Date date) throws ParseException{
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }

    /**
     * 返回指定日期的月的最后一天
     *
     * @param date
     * @return Date
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * 返回当前日期 当天最后一秒
     *
     * @param date
     * @return Date
     */
    public static Date getLastNowDate(Date date) {
        String nowDate = DateUtils.dateToString(date);
        nowDate = nowDate + " 23:59:59";
        return DateUtils.strToDate(nowDate,StandardDateTime);
    }

    /**
     * 返回指定年月的月的最后一天
     *
     * @param year
     * @param month
     * @return Date
     */
    public static Date getLastDayOfMonth(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH);
        }
        calendar.set(year, month, 1);
        calendar.add(Calendar.MONTH,-1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * 返回指定日期的月的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), 1);
        return calendar.getTime();
    }

    /**
     * 返回指定年月的月的第一天
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getFirstDayOfMonth(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH);
        }
        calendar.set(year, month, 1);
        calendar.add(Calendar.MONTH,-1);
        return calendar.getTime();
    }

    /**
     *  根据传入的 Date 和 日期 获取对应的时间
     * @param date
     * @param day
     * @return
     */
    public static Date getDateByDateAndDay(Date date, Integer day){
        Calendar calendar = Calendar.getInstance();
        if(Objects.nonNull(date))
            calendar.setTime(date);
        if(Objects.isNull(day))
            day = calendar.DATE;
        calendar.set(Calendar.DATE,day);
        return calendar.getTime();
    }

    /**
     * @Title: getHours
     * @Description: 获取指定时间离当前时间差多少小时
     */

    public static long getHours(Date date){
        long currentDate=new Date().getTime();
        long d=date.getTime();
        return (currentDate-d)/(3600*1000);
    }


   /* 一个日期的后几天的时间， 如：传入一个date 和 int  得到这个date 的后几天  如果int是负数，那么是后几天
   * @param date 日期
   * @param date日期的前n天的日期
   * */
   public static Date getDateAfterDays(Date date, Integer days) {
       Calendar calendar = Calendar.getInstance();
       calendar.setTime(date);
       calendar.add(Calendar.DATE, days);
       return calendar.getTime();
   }

    /**
     * 返回指定日期的上个月的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfLastMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) - 1, 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * 返回指定日期的上个月的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfLastMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) - 1, 1);
        return calendar.getTime();
    }
    /**
     * 返回指定日期的下个月的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfNextMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 1);
        return calendar.getTime();
    }

    /**
     * 返回指定日期的下个月的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfNextMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * 返回指定日期的下个月的最后一天
     *
     * @param date
     * @return
     */
    public static Date getDayOfNextMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * @Title: getDateStringByNumberSs
     * @Description: 根据毫秒大小计算xx天xx小时xx分钟
     * @author guohongjin
     * @date 2015-12-15
     * @throw YnCorpSysException
     * @param ssNumber 毫秒大小
     * @return String
     */

    public static String getDateStringByNumberSs(Long ssNumber){

        long minutiue=ssNumber/(1000*60);
        //分钟余额
        long minutiueNum=minutiue%60;
        //小时
        long hour=(ssNumber)/(1000*60*60);
        //天数
        long dayNum=hour/24;
        //小时余额
        long hourNum=hour%24;
        //判断天数是否为0

        if (dayNum==0l) {
            if (hourNum==0l) {
                return minutiueNum+"分钟";
            }else {
                return hourNum+"小时"+minutiueNum+"分钟";
            }
        }else {
            if (hourNum==0l && minutiueNum==0l) {
                return dayNum+"天";
            }else {
                if (minutiueNum==0l){
                    return dayNum+"天"+hourNum+"小时";
                }else {
                    return dayNum+"天"+hourNum+"小时"+minutiueNum+"分钟";
                }
            }
        }

    }

//	/**
//	 * @Title: getLastDateByTime
//	 * @Description: 获取指定时间
//	 * @param date 时间对象
//	 * @return int
//	 */
//	public static int  getLastDateByTime(Date date){
////		String str = "";
//		SimpleDateFormat sdf = new SimpleDateFormat(StandardDate);
//		Calendar lastDate = Calendar.getInstance();
//		lastDate.setTimeInMillis(date.getTime());
//		//lastDate.set(Calendar.YEAR, date.getYear());
//		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
//		//lastDate.set(Calendar.MONTH, date.getMonth());// 加一个月，变为下月的1号
//		lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
//		System.out.println(sdf.format(lastDate.getTime()));
//		return lastDate.getTime().getDate();
//
//	}
//	public static void main(String[] args) throws ParseException {
//		try {
////			Date begDate= DateUtils.stringToDateForException("2014-06-04 00:00:00", DateUtils.StandardDateTime);
////			Date endDate= DateUtils.stringToDateForException("2014-05-06 23:00:59", DateUtils.StandardDateTime);
//////            System.out.print(patternDateTimeHhMm);
//////            Date date=DateUtils.parseDateTimeNote("2014-05-06 02:12");
//////            System.out.println(DateUtils.getFdate(date,patternDateTimeHhMm));
////            long days=DateUtils.getDaysByDate(begDate,endDate);
////			System.out.println(days);
//            Date date=new Date();
//            Calendar calendar=Calendar.getInstance();
//            calendar.setTime(date);
//            calendar.getLeastMaximum(Calendar.MONTH);
//            calendar.getActualMaximum(Calendar.MONDAY);
//            System.out.print(calendar.get(Calendar.DATE)+"PPP"+calendar.getActualMaximum(Calendar.MONDAY));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}

//    public static void main2(String[] args) {
//        System.out.println(DateUtils.getDateStringByNumberSs(10000000l));
//    }


    public static List<Date> getDateRange(Date start,Date end){
        List<Date> list=new ArrayList<Date>();
        Calendar calendarStart=Calendar.getInstance();
        calendarStart.setTime(start);
        Calendar calendarEnd=Calendar.getInstance();
        calendarEnd.setTime(end);
        getDateList(calendarStart,calendarEnd,list);
        return list;
    }

    /**
     * 递归获取时间范围集合
     * @param calendarStart
     * @param calendarEnd
     * @param list
     */
    private static  void getDateList(Calendar calendarStart,Calendar calendarEnd,List<Date> list){
        if(calendarEnd.compareTo(calendarStart)>=0){
            list.add(calendarEnd.getTime());
            calendarEnd.add(Calendar.MONTH,-1);//月份减1
            getDateList(calendarStart,calendarEnd,list);
        }
    }

    /**
     * 计算开始和结束日期在一个查询时间范围内交集有多少天
     * @author yangyuan
     * @date 2016-04-20
     * @param begin
     * @param end
     * @param beginSel
     * @param endSel
     * @return
     */
    public static int findDateCommonDay(Date begin,Date end,Date beginSel,Date endSel){
        int days = 0;
        try {
            begin = DateUtils.parseDate(DateUtils.formatDate(begin));
            end = DateUtils.parseDate(DateUtils.formatDate(end));
            beginSel = DateUtils.parseDate(DateUtils.formatDate(beginSel));
            endSel = DateUtils.parseDate(DateUtils.formatDate(endSel));
            long dd = 0l;
            //只有四种情况才能产生交集，分别计算如下：
            if (!beginSel.after(begin) && !end.after(endSel)){
                //开始时间和结束时间都在查询时间范围内
                dd = (end.getTime() - begin.getTime()) / (24 * 60 * 60 * 1000);
            } else if ((!beginSel.after(begin) && !begin.after(endSel) && !endSel.after(end))){
                //开始时间在查询时间范围内,结束时间大于查询结束时间
                dd = ((endSel.getTime() - begin.getTime())) / (24 * 60 * 60 * 1000);
            } else if (!begin.after(beginSel) && !beginSel.after(end) && !end.after(endSel)){
                //开始时间小于查询开始时间，结束时间在查询时间范围内
                dd = ((end.getTime() - beginSel.getTime())) / (24 * 60 * 60 * 1000);
            } else if (!begin.after(beginSel) && !endSel.after(end)){
                //开始时间和结束时间包含了查询时间范围
                dd = ((endSel.getTime() - beginSel.getTime())) / (24 * 60 * 60 * 1000);
            }
            days = (int)dd;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * @Title: getMinutes
     * @Description: 获取指定时间离当前时间差多少分钟
     * @author guohongjin
     * @date 2016-06-07
     */

    public static long getMinutes(Date date){
        long currentDate=new Date().getTime();
        long d=date.getTime();
        return (currentDate-d)/(60*1000);
    }

    public static int getHoursByDate(Date date)throws ParseException{
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinutesByDate(Date date)throws ParseException{
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    public static int getSecondByDate(Date date)throws ParseException{
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.SECOND);
    }



//    public static void main(String[] arg){
//        String showDate="2016-12-19";
////        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
////        Date date = DateUtils.strToDate(showDate);
////        showDate = sdf.format
////        Date daa
//
//        System.out.println(DateUtils.getWeekList("2016-12-22").size());
////        for(int i=0;i<3;i++){
////            System.out.println(DateUtils.getNextSundayByDate(new Date()));
////            System.out.println("i____"+i+" size "+stringList.size());
////            Date date = DateUtils.strToDate("2016-12-20");
////            showDate = DateUtils.getNextSundayByDate(DateUtils.increaseDay(date,-1));
////        }
//
//    }

}
