
package cn.org.citycloud.spider.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateFormat {
	
	private static SimpleDateFormat httpFormat = new SimpleDateFormat(
			"EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
	private static SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MMM-dd HH:mm:ss");
	private static SimpleDateFormat lastTime = new SimpleDateFormat(
	"yyyy/MM/dd HH:mm");
	private static SimpleDateFormat DATA = new SimpleDateFormat(
	"yyyy/MM/dd");
	private static SimpleDateFormat YEAR = new SimpleDateFormat(
	"yyyy/");
	private static SimpleDateFormat Y = new SimpleDateFormat(
	"yy");
	private static SimpleDateFormat HH = new SimpleDateFormat(
	"HH:mm");
	
	static {
		httpFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	public static synchronized String toString(Date date) {
		return httpFormat.format(date);
	}

	public static synchronized String toString(Calendar cal) {
		return httpFormat.format(cal.getTime());
	}

	public static synchronized String getDate(Calendar cal){
		return DATA.format(cal.getTime());
	}
	
	public static synchronized String getYear(Calendar cal){
		return YEAR.format(cal.getTime());
	}
	
	public static synchronized int getYearBy2Bit(Calendar cal){
		return Integer.parseInt(Y.format(cal.getTime()));
	}
	
	public static synchronized String toString(long time) {
		return httpFormat.format(new Date(time));
	}

	public static synchronized Date toDate(String dateString)
			throws ParseException {
		return httpFormat.parse(dateString);
	}

	public static synchronized long toLong(String dateString){
		try {
			return httpFormat.parse(dateString).getTime();
		} catch (ParseException e) {
			return System.currentTimeMillis();
		}
	}
	
	public static synchronized String getDate(String dateString)
			throws ParseException {
		return format.format(new Date(Long.valueOf(dateString)));
	}
	
	public static String getLastTime(){
		Calendar date = Calendar.getInstance();
		date.add(Calendar.DATE, -3);
		return lastTime.format(date);
	}
	
	public static String getCurrTime(){
		Calendar date = Calendar.getInstance();
		date.add(Calendar.DATE, -1);
		return lastTime.format(date);
	}
	
	public static String getTime(Calendar cal){
		return HH.format(cal.getTime());
	}
	
}
