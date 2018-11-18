package com.thinkgem.jeesite.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.activiti.engine.impl.util.json.JSONObject;

import com.google.zxing.FormatException;
import com.thinkgem.jeesite.common.utils.ZxingHandler;

public class TimeUtil {
	public String getCurrentTime() {
		String now = System.currentTimeMillis() + "";
		return now;
	}

	public String getDateTime(String s) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long now = Long.parseLong(s);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(now);
		return formatter.format(calendar.getTime());
	}

	public String getUpSeconds(String year, String month, String date) {
		String u = " " + year + "-" + month + "-" + date + " " + "00:00:00 ";
		System.out.println("uptime:  " + u);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = null;
		try {
			time = format.parse(u).getTime() + "";
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}

	public String getLowSeconds(String year, String month, String date) {
		String u = " " + year + "-" + month + "-" + date + " " + "23:59:59 ";
		System.out.println("uptime:  " + u);
		SimpleDateFormat format = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");
		String time = null;
		try {
			time = format.parse(u).getTime() + "";
			System.out.println(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}

	/*public static void main(String[] args) {
		JSONObject json = new JsonUtil().codeJson("测试", "测试", "测试", "测试", "1", "1", "1");
		String imgPath = "archive/src/main/resources/img/" + json.get("id") + ".jpg";
		new ZxingHandler().encode(json.getString("id"), 100, 50, imgPath);
	}
*/
	public String getStringTime() {
		Calendar nowtime = new GregorianCalendar();
		int year = nowtime.get(Calendar.YEAR);
		int month = nowtime.get(Calendar.MONTH) + 1;
		int date = nowtime.get(Calendar.DAY_OF_MONTH);
		int hour = nowtime.get(Calendar.HOUR);
		int min = nowtime.get(Calendar.HOUR_OF_DAY);
		int sec = nowtime.get(Calendar.SECOND);
		int millsec = nowtime.get(Calendar.MILLISECOND);
		int st = (int) (Math.random()*9000+1000);
		String y, m, d, h, M, s, ss;
		y = "" + year;
		if (month > 0 && month < 10) {
			m = "0" + month;
		} else {
			m = "" + month;
		}
		if (date > 0 && date < 10) {
			d = "0" + date;
		} else {
			d = "" + date;
		}
		if (hour >= 0 && hour < 10) {
			h = "0" + hour;
		} else {
			h = "" + hour;
		}
		if (min >= 0 && min < 10) {
			M = "0" + min;
		} else {
			M = "" + min;
		}
		if (sec >= 0 && sec < 10) {
			s = "0" + sec;
		} else {
			s = "" + sec;
		}
		if (millsec >= 0 && millsec < 10) {
			ss = "00" + millsec;
		} else if (millsec >= 10 && millsec < 100) {
			ss = "0" + millsec;
		} else {
			ss = "" + millsec;
		}
		String str = y+m+d+h+M+s+ss+st;
		System.out.println(str);
		return str;
	}
}
