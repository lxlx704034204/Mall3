package com.hxqc.mall.drivingexam.utils;

import android.util.Log;

import com.hxqc.util.DebugLog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtils {

	/** 日期格式：yyyy-MM-dd HH:mm:ss **/
	public static final String DF_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	/** 日期格式：yyyy-MM-dd HH:mm **/
	public static final String DF_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

	/** 日期格式：yyyy-MM-dd **/
	public static final String DF_YYYY_MM_DD = "yyyy-MM-dd";

	/** 日期格式：HH:mm:ss **/
	public static final String DF_HH_MM_SS = "HH:mm:ss";

	/** 日期格式：HH:mm **/
	public static final String DF_HH_MM = "HH:mm";

	private final static long minute = 60 * 1000;// 1分钟
	private final static long hour = 60 * minute;// 1小时
	private final static long day = 24 * hour;// 1天
	private final static long month = 31 * day;// 月
	private final static long year = 12 * month;// 年

	/** Log输出标识 **/
	private static final String TAG = DateUtils.class.getSimpleName();

	long mLoginTime = System.currentTimeMillis();
	
	
 
	
	/**  得到当前时间 yyyy-MM-dd HH:mm:ss
	 *     
	 * @return  string 
	 */
	public static String getNowForFileName() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		return sdf.format(c.getTime());
	}
	

	public static String getNowForFileName(String formater) {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(formater);
		
		return sdf.format(c.getTime());
	}
	
	/**
	 * 将日期以yyyy-MM-dd HH:mm:ss格式化
	 * 
	 * @param dateL
	 *            日期
	 * @return
	 */
	public static String formatDateTime(long dateL) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(dateL);
		return sdf.format(date);
	}

	/**
	 * 将日期以自定义格式化
	 * 
	 * @param dateL
	 *            日期
	 * @return
	 */
	public static String formatDateTime(long dateL, String formater) {
		SimpleDateFormat sdf = new SimpleDateFormat(formater);
		return sdf.format(new Date(dateL));
	}

	/**
	 * 将日期以yyyy-MM-dd HH:mm:ss格式化
	 * 
	 * @param dateL
	 *            日期
	 * @return
	 */
	public static String formatDateTime(Date date, String formater) {
		SimpleDateFormat sdf = new SimpleDateFormat(formater);
		return sdf.format(date);
	}

	
	public static String formatDateTime(String date, String formater) {
		SimpleDateFormat sdf = new SimpleDateFormat(formater);
		
		 
		
		
		return sdf.format(parseDate(date));
	}
	
	
	/**
	 * 将日期字符串转成日期
	 * 
	 * @param strDate
	 *            字符串日期
	 * @return java.util.date日期类型
	 */
	public static Date parseDate(String strDate) {
		DateFormat dateFormat = new SimpleDateFormat(DF_YYYY_MM_DD_HH_MM_SS);
		Date returnDate = null;
		try {
			returnDate = dateFormat.parse(strDate);
		} catch (ParseException e) {
			Log.v(TAG, "parseDate failed !");

		}
		return returnDate;

	}
	
	 
	

	/**
	 * 获取系统当前日期
	 * 
	 * @return
	 */
	public static Date gainCurrentDate() {
		return new Date();
	}

	
	

	/**
	 * @param  "1小时14分钟" 转换成  74
	 * @return
	 */
	public static long hour2min (String str){
		
		long h = Long.parseLong(str.substring(0, str.indexOf("小时")));
		long m = Long.parseLong(str.substring(str.indexOf("时")+1, str.indexOf("分钟")));
		
		return h*60+m;
	}
	
	/**
	 * @param   74 转换成 "1小时14分钟"
	 * @return
	 */
	public static String min2hour(long min) {
		String a = "";
		if(min<0||min==0){
			a = "n/a";
		}
		else if (min < 60) {
			a = min + "分钟";
		} else if (min > 60 || min == 60) {
			long h = min / 60;
			long m = min % 60;
			if (m == 0)
				a = h + "小时";
			else
				a = h + "小时" + m + "分钟";
		}

		return a;

	}

	
	/**
	 * 将日期格式化成友好的字符串：几分钟前、几小时前、几天前、几月前、几年前、刚刚
	 * 
	 * @param date
	 * @return
	 */
	public static String formatFriendly(Date date) {
		if (date == null) {
			return null;
		}
		long diff = new Date().getTime() - date.getTime();
		long r = 0;
		if (diff > year) {
			r = (diff / year);
			return r + "年前";
		}
		if (diff > month) {
			r = (diff / month);
			return r + "个月前";
		}
		if (diff > day) {
			r = (diff / day);
			if(r==1)
				return "昨天";
			return r + "天前";
		}
		if (diff > hour) {
			r = (diff / hour);
			return r + "小时前";
		}
		if (diff > minute) {
			r = (diff / minute);
			return r + "分钟前";
		}
		return "刚刚";
	}

	public static String formatFriendlyDay(Date date) {
		if (date == null) {
			return null;
		}
		long diff = new Date().getTime() - date.getTime();
		long r = 0;
		if (diff > year) {
			r = (diff / year);
			return r + "年前";
		}
		if (diff > month) {
			r = (diff / month);
			return r + "个月前";
		}
		if (diff > day) {
			r = (diff / day);
			if(r==1)
				return "昨天";
			return r + "天前";
		}
		if (diff > hour) {
			r = (diff / hour);
			return "今天";
		}

		return "刚刚";
	}


	public static String getTimeDiff(long time) {
		// Calendar cal = Calendar.getInstance();
		long diff = 0;
		// Date dnow = cal.getTime();
		String str = "";
		diff = System.currentTimeMillis() - time;

		if (diff > 2592000000L) {// 30 * 24 * 60 * 60 * 1000=2592000000 毫秒
			str = "1个月前";
		} else if (diff > 1814400000) {// 21 * 24 * 60 * 60 * 1000=1814400000 毫秒
			str = "3周前";
		} else if (diff > 1209600000) {// 14 * 24 * 60 * 60 * 1000=1209600000 毫秒
			str = "2周前";
		} else if (diff > 604800000) {// 7 * 24 * 60 * 60 * 1000=604800000 毫秒
			str = "1周前";
		} else if (diff > 86400000) { // 24 * 60 * 60 * 1000=86400000 毫秒
			// System.out.println("X天前");
			str = (int) Math.floor(diff / 86400000f) + "天前";
		} else if (diff > 18000000) {// 5 * 60 * 60 * 1000=18000000 毫秒
			// System.out.println("X小时前");
			str = (int) Math.floor(diff / 18000000f) + "小时前";
		} else if (diff > 60000) {// 1 * 60 * 1000=60000 毫秒
			// System.out.println("X分钟前");
			str = (int) Math.floor(diff / 60000) + "分钟前";
		} else {
			str = (int) Math.floor(diff / 1000) + "秒前";
		}
		return str;
	}

	public static int calDays(int year, int month) {
		int day = 0;
		boolean leayyear = false;
		if ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0)) {
			leayyear = true;
		} else {
			leayyear = false;
		}
		for (int i = 1; i <= 12; i++) {
			switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				day = 31;
				break;
			case 2:
				if (leayyear) {
					day = 29;
				} else {
					day = 28;
				}
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				day = 30;
				break;
			}
		}
		return day;

	}
	
	public static String format(long ms) {//将毫秒数换算成x天x时x分x秒x毫秒
		int ss = 1000;
		int mi = ss * 60;
		int hh = mi * 60;
		int dd = hh * 24;

		long day = ms / dd;
		long hour = (ms - day * dd) / hh;
		long minute = (ms - day * dd - hour * hh) / mi;
		long second = (ms - day * dd - hour * hh - minute * mi) / ss;
		long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;
		return getFormatTime(day + "天" + hour + "时" + minute + "分" + second + "秒");
		}
	public static String getFormatTime(String s){
		String a=s.substring(0, s.indexOf("天"));
		String b=s.substring(s.indexOf("天")+1, s.indexOf("时"));
		String c=s.substring( s.indexOf("时")+1, s.indexOf("分"));
		String d=s.substring(s.indexOf("分")+1, s.indexOf("秒"));
		if(!"0".equals(a)){
			return a+"天";
		}else if(!"0".equals(b)){
			return b+"时";
		}
		else if(!"0".equals(c)){
			return c+"分";
		}
		else{
			return d+"秒";
		}
		
	}
	
	
	/**
	 * 验证日期是否比当前日期早
	 * 
	 * @param target1
	 *            比较时间1
	 * @param target2
	 *            比较时间2
	 * @return true 则代表target1比target2晚或等于target2，否则比target2早
	 */
	public static boolean compareDate(Date target1, Date target2) {
		boolean flag = false;
		try {
			String target1DateTime = DateUtils.formatDateTime(target1,
					DF_YYYY_MM_DD_HH_MM_SS);
			String target2DateTime = DateUtils.formatDateTime(target2,
					DF_YYYY_MM_DD_HH_MM_SS);
			if (target1DateTime.compareTo(target2DateTime) <= 0) {
				flag = true;
			}
		} catch (Exception e1) {
			DebugLog.i("Tag","比较失败，原因：" + e1.getMessage());
		}
		return flag;
	}

	/**
	 * 对日期进行增加操作
	 * 
	 * @param target
	 *            需要进行运算的日期
	 * @param hour
	 *            小时
	 * @return
	 */
	public static Date addDateTime(Date target, double hour) {
		if (null == target || hour < 0) {
			return target;
		}

		return new Date(target.getTime() + (long) (hour * 60 * 60 * 1000));
	}

	/**
	 * 对日期进行相减操作
	 * 
	 * @param target
	 *            需要进行运算的日期
	 * @param hour
	 *            小时
	 * @return
	 */
	public static Date subDateTime(Date target, double hour) {
		if (null == target || hour < 0) {
			return target;
		}

		return new Date(target.getTime() - (long) (hour * 60 * 60 * 1000));
	}


/**
 * 根据月日获取星座
 * 
 * @param month
 *            月
 * @param day
 *            日
 * @return
 */
public static String getConstellation(int month, int day) {
	if ((month == 1 && day >= 20) || (month == 2 && day <= 18)) {
		return "水瓶座";
	} else if ((month == 2 && day >= 19) || (month == 3 && day <= 20)) {
		return "双鱼座";
	} else if ((month == 3 && day >= 21) || (month == 4 && day <= 19)) {
		return "白羊座";
	} else if ((month == 4 && day >= 20) || (month == 5 && day <= 20)) {
		return "金牛座";
	} else if ((month == 5 && day >= 21) || (month == 6 && day <= 21)) {
		return "双子座";
	} else if ((month == 6 && day >= 22) || (month == 7 && day <= 22)) {
		return "巨蟹座";
	} else if ((month == 7 && day >= 23) || (month == 8 && day <= 22)) {
		return "狮子座";
	} else if ((month == 8 && day >= 23) || (month == 9 && day <= 22)) {
		return "处女座";
	} else if ((month == 9 && day >= 23) || (month == 10 && day <= 23)) {
		return "天秤座";
	} else if ((month == 10 && day >= 24) || (month == 11 && day <= 22)) {
		return "天蝎座";
	} else if ((month == 11 && day >= 23) || (month == 12 && day <= 21)) {
		return "射手座";
	} else if ((((month != 12) || (day < 22)))
			&& (((month != 1) || (day > 19)))) {
		return "魔蝎座";
	}
	return "";
}

/**
 * 根据年月日获取年龄
 * 
 * @param year
 *            年
 * @param month
 *            月
 * @param day
 *            日
 * @return
 */
public static int getAge(int year, int month, int day) {
	int age = 0;
	Calendar calendar = Calendar.getInstance();
	if (calendar.get(Calendar.YEAR) == year) {
		if (calendar.get(Calendar.MONTH) == month) {
			if (calendar.get(Calendar.DAY_OF_MONTH) >= day) {
				age = calendar.get(Calendar.YEAR) - year + 1;
			} else {
				age = calendar.get(Calendar.YEAR) - year;
			}
		} else if (calendar.get(Calendar.MONTH) > month) {
			age = calendar.get(Calendar.YEAR) - year + 1;
		} else {
			age = calendar.get(Calendar.YEAR) - year;
		}
	} else {
		age = calendar.get(Calendar.YEAR) - year;
	}
	if (age < 0) {
		return 0;
	}
	return age;
}


  public static String getYear(){
	  
	return new SimpleDateFormat("yyyy").format(new Date());

  }

  public static String getMonth(){

	return new SimpleDateFormat("MM").format(new Date());

  }

  public static String getDay(){

		return new SimpleDateFormat("dd").format(new Date());
		  
	  }
}
