package com.tohier.cartercoin.config;

import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间相距
 * 
 * @author Ben
 * @version 1.0
 * @date 2009-10-21 16:38:51
 */
public class DateDistance {

	private static boolean is_start;

	public static long getDistanceDays(String str1, String str2)
			throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date one;
		Date two;
		long days = 0;
		try {
			one = df.parse(str1);
			two = df.parse(str2);
			long time1 = one.getTime();
			long time2 = two.getTime();
			long diff;
			if (time1 < time2) {
				diff = time2 - time1;
			} else {
				diff = time1 - time2;
			}
			days = diff / (1000 * 60 * 60 * 24);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return days;
	}

	/**
	 * 两个时间相差距离多少天多少小时多少分多少秒
	 * 
	 * @param str1
	 *            时间参数 1 格式：1990-01-01 12:00:00
	 * @param str2
	 *            时间参数 2 格式：2009-01-01 12:00:00
	 * @return long[] 返回值为：{天, 时, 分, 秒}
	 */
	public static long[] getDistanceTimes(String str1, String str2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date one;
		Date two;
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		try {
			one = df.parse(str1);
			two = df.parse(str2);
			long time1 = one.getTime();
			long time2 = two.getTime();
			long diff;
			if (time1 < time2) {
				diff = time2 - time1;
			} else {
				diff = time1 - time2;
			}
			day = diff / (24 * 60 * 60 * 1000);
			hour = (diff / (60 * 60 * 1000) - day * 24);
			min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
			sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long[] times = { day, hour, min, sec };
		return times;
	}

	/**
	 * 两个时间相差距离多少天多少小时多少分多少秒
	 *
	 * @param str1
	 *            时间参数 1 格式：1990-01-01 12:00:00
	 * @param str2
	 *            时间参数 2 格式：2009-01-01 12:00:00
	 * @return String 返回值为：xx天xx小时xx分xx秒
	 */
	public static String getDistanceTime(String str1, String str2,TextView tv) {
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date one;
		Date two;
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		try {
			one = df.parse(str1);
			two = df.parse(str2);
			long time1 = one.getTime();
			long time2 = two.getTime();
			long diff;
			if (time1 < time2) {
				diff = time2 - time1;
			} else {
				diff = time1 - time2;
			}
			day = diff / (24 * 60 * 60 * 1000);
			hour = (diff / (60 * 60 * 1000) - day * 24);
			min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
			sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return day + "天" + hour + "小时" + min + "分" + sec + "秒";
	}


	/**
	 * 两个时间相差距离多少天多少小时多少分多少秒
	 *
	 * @return String 返回值为：xx天xx小时xx分xx秒
	 */
	public static String getDistanceTime(String time,TextView tv) {
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		try {

			long diff = Long.parseLong(time);

			day = diff / (24 * 60 * 60 * 1000);
			hour = (diff / (60 * 60 * 1000) - day * 24);
			min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
			sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return   "00:" + min + ":" + sec;
	}


	public static String getDistanceTime5(String str1, String str2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date one;
		Date two;
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		try {
			one = df.parse(str1);
			two = df.parse(str2);
			long time1 = one.getTime();
			long time2 = two.getTime();
			long diff;
			if (time1 < time2) {
				is_start = false;
				diff = time2 - time1;
			} else {
				is_start = true;
				diff = time1 - time2;
			}
			day = diff / (24 * 60 * 60 * 1000);
			hour = (diff / (60 * 60 * 1000) - day * 24);
			min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
			sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(is_start)
		{
               return "开始";
		}else
		{
			return day + "天" + hour + "小时" + min + "分" + sec + "秒";
		}
	}

	// 2016/9/21 0:00:00
	// 格式：1990-01-01 12:00:00

	/**
	 *
	 * @param currentTime  当前时间
	 * @param startTime  活动开始时间
     * @return
     */
	public static String getDistanceTime2(String currentTime, String startTime , String endTime) {
		DateFormat df2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		DateFormat df3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		DateFormat df4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date one;
		Date two;
		Date three;
		long time1 = 0;
		long time2 = 0;
		long time3 = 0;
		long diff = 0;
		try {
			one = df4.parse(currentTime);
			two = df2.parse(startTime);
			three = df2.parse(endTime);
			time1 = one.getTime(); // 当前时间 147 45 117 78000
			time2 = two.getTime(); // 活动开始时间 147 43 872 00000
			time3 = three.getTime(); // 活动开始时间 147 43 872 00000
			//当前时间小于活动开始时间未开始
			if (time1 < time2) {
				return "未开始";
			}else if(time1>=time2&&time1<time3)	//当前时间大于等于活动开始时间 and 当前时间小于等于活动结束时间
			{
				return "活动中";
			}else if(time1>=time3)
			{
				return "已结束";
			}else
			{
				return null;
			}

		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 *
	 * @param currentTime  当前时间
	 * @param startTime  活动开始时间
     * @return
     */
	public static long getBuyAssetsTimeIsStart(String currentTime, String startTime) {
		DateFormat df2 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		DateFormat df3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		Date one;
		Date two;
		long time1 = 0;
		long time2 = 0;
		long time = 0;
		try {
			one = df3.parse(currentTime);
			two = df2.parse(startTime);
			time1 = one.getTime(); // 当前时间 147 45 117 78000
			time2 = two.getTime(); // 活动开始时间 147 43 872 00000
			//当前时间小于活动开始时间未开始
			time = time2 - time1;
			return time;
//			if (time1 < time2) {
//
//				return "未开始";
//			}else if(time1>=time2)	//当前时间大于等于活动开始时间 and 当前时间小于等于活动结束时间
//			{
//				return "活动中";
//			}else
//			{
//				return null;
//			}

		} catch (ParseException e) {
			e.printStackTrace();
//			return null;
			return time;
		}
	}

	/**
	 *
	 * @param currentTime  当前时间
	 * @param startTime  活动开始时间
	 * @return
	 */
	public static String getDistanceTime8(String currentTime, String startTime) {
		String status = "";
		DateFormat df2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		DateFormat df3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		Date one;
		Date two;
		long time1 = 0;
		long time2 = 0;
		long diff = 0;

		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		long millisecond = 0;
		try {
			one = df3.parse(currentTime);
			two = df2.parse(startTime);
			time1 = one.getTime(); // 当前时间 147 45 117 78000
			time2 = two.getTime(); // 活动开始时间 147 43 872 00000
			//当前时间小于活动开始时间未开始
			if (time1 < time2) {
				status = "未开始";
			}else if(time1>=time2)	//当前时间大于等于活动开始时间 and 当前时间小于等于活动结束时间
			{
				status = "活动中";
			}else
			{
				status = "";
			}

			if(status.equals("未开始"))
			{
				diff = time2 - time1;
				day = diff / (24 * 60 * 60 * 1000);
				hour = (diff / (60 * 60 * 1000) - day * 24);
				min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
				sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
				millisecond = diff % 1000;
				return day + "天" + hour + ":" + min + ":" + sec+" "+millisecond;
			}else if(status.equals("活动中"))
			{
				return "活动中";
			}else
			{
				return null;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}


	}
}
