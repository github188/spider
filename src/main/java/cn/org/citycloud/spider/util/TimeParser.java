package cn.org.citycloud.spider.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TimeParser {

	private SimpleDateFormat normalSF = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	private SimpleDateFormat timeformat = new SimpleDateFormat("kk:mm");
	private SimpleDateFormat westSF = new SimpleDateFormat("ddMMyyyy HH:mm");

	private Pattern time1 = Pattern
			.compile("(((19)[89]\\d)|((20)[01]\\d))(年|[/\\-\\.])[01]?\\d(月|[/\\-\\.])((3[10]|([0-2]?\\d)))[\\S\\s]{0,2}(([0-1]?\\d)|(2[0-4]))[:时]([0-5]?\\d)");
	private Pattern time2 = Pattern
			.compile("(((19)[89]\\d)|((20)[01]\\d))(年|[/\\-\\.])[01]?\\d(月|[/\\-\\.])((3[10]|([0-2]?\\d)))");
	private Pattern westernTime = Pattern
			.compile("[0-3][1-9]\\.[0-1][1-9]\\.(((19)[7-9]\\d)|(20)\\d{2})[\\S\\s]*[0-2]\\d:[0-5]\\d");
	private Pattern[] timePattern = { time1, time2, westernTime };

	private String getCurTime() {
		return timeformat.format(new Date());
	}

	private SimpleDateFormat yearformat = new SimpleDateFormat("yyyy-MM-dd");

	private String getYearTime() {
		return yearformat.format(new Date());
	}

	public String getStrTime(String content) {
		return getTime(format2Normal(filterScripts(content)));
	}

	/****************** 去html标签 *******************/
	private String filterScripts(String content) {
		if (content == null
				|| content.replaceAll("\\s+", "").equalsIgnoreCase(""))
			return "";
		content = clear(content);
		return content;
	}

	private String clear(String content) {

		Document doc = Jsoup.parse(content);
		doc = clear(doc, "a");
		doc = clear(doc, "meta");
		doc = clear(doc, "script");
		doc = clear(doc, "marquee");
		doc = clear(doc, "style");
		content = doc.html();

		content = content.replaceAll("&amp;", "&");
		content = content.replaceAll("&lt;", "<");
		content = content.replaceAll("&gt;", ">");
		content = content.replaceAll("&quot;", "\"");
		content = content.replaceAll("&nbsp;", " ");

		content = content.replaceAll("<!-[\\s\\S]*?-->", "");
		content = content.replaceAll("/\\*[\\s\\S]*?\\*/", "");
		content = content.replaceAll("&nbsp;", " ");
		content = content.replaceAll("<[\\s\\S]*?>", "");
		content = content.replaceAll("</[\\s\\S]*?>", "");
		return content.replaceAll("//.*?", "");

	}

	private boolean clearObj(Element e, String obj) {
		Elements aeles = e.getElementsByTag(obj);
		int asize = aeles.size();
		aeles.remove();
		if (asize > 5 & e.text().length() < 20)
			return true;
		return false;
	}

	private Document clear(Document doc, String obj) {
		Elements eles = doc.getElementsByTag(obj);
		for (Element cur : eles) {
			Element e = cur.parent();
			if (e != null) {
				clearObj(e, obj);
			}
		}
		return doc;
	}

	/****************** 去html标签 end *******************/

	private String[] fromNowKey = { "今天", "昨天", "前天" };
	private Long[] fromNowValue = { 0L, 24 * 60 * 60 * 1000L,
			2 * 24 * 60 * 60 * 1000L };

	private String[] subFromNowKey = { "秒前", "分钟前", "小时前", "天前" };
	private Long[] subFromNowValue = { 0L, 60 * 1000L, 60 * 60 * 1000L,
			24 * 60 * 60 * 1000L };

	private Pattern subTimePattern = Pattern
			.compile("(1[0-9])|(2[0-3])|([1-9])");

	private String format2Normal(String time) {
		String postTime = null;
		for (int i = 0; i < fromNowKey.length; i++) {
			if (time.contains(fromNowKey[i])) {
				postTime = normalSF.format(new Date(System.currentTimeMillis()
						- fromNowValue[i]));
				String[] dateTemp = postTime.split(" ");
				String[] timeTemp = time.split(fromNowKey[i]);
				postTime = dateTemp[0] + " " + timeTemp[1];
				return postTime;
			}
		}

		for (int j = 0; j < subFromNowKey.length; j++) {
			if (time.contains(subFromNowKey[j])) {
				Matcher matcher = subTimePattern.matcher(time);
				String timeTemp = "";
				if (matcher.find()) {
					timeTemp = matcher.group(0);
					if (matcher.find()) {
						timeTemp = "";
					}
				}
				Long value = 0L;
				try {
					value = Long.parseLong(timeTemp) * subFromNowValue[j];
				} catch (NumberFormatException e) {
				}
				postTime = normalSF.format(new Date(System.currentTimeMillis()
						- value));
				return postTime;
			}
		}
		return time;
	}

	private String getTime(String content) {
		int start = content.indexOf("<body");
		if (start < 0)
			start = content.indexOf("<BODY");
		if (start < 0)
			start = 0;

		for (int i = 0; i < timePattern.length; i++) {
			Matcher make = timePattern[i].matcher(content.substring(start));
			while (make.find()) {
				String temp = make.group();
				if (!(temp = parseTime(temp)).equalsIgnoreCase("")) {
					return temp;
				}
			}
		}
		return date.format(new Date());
	}

	private String parseTime(String time) {
		if (time == null || time.equalsIgnoreCase("")) {
			return "";
		} else {
			time = data2en(time);
			try {
				if (time == null)
					return "";
				if (date.parse(time).getTime() > new Date().getTime())
					return "";
			} catch (Exception e) {
				try {
					time = time.replaceAll("/", "");
					if (westSF.parse(time).getTime() < new Date().getTime()) {
						time = date.format(westSF.parse(time));
					}
				} catch (Exception e1) {
					return "";
				}
			}
		}
		return time;
	}

	private String data2en(String time) {
		try {
			time = filter(time.trim());
			String[] temp = time.split("\\s");
			String[] datas = new String[2];

			int i = 0;

			for (String t : temp) {
				if (!t.equalsIgnoreCase("")) {
					if (i < 2) {
						datas[i++] = t;
					} else {
						return null;
					}
				}
			}

			if (i != 2 && i != 1) {
				return null;
			}

			String[] r = guess2Time(datas[0].trim());

			if (r != null) {
				datas[0] = r[0];
				if (datas[1] == null || datas[0].equalsIgnoreCase("")) {
					datas[1] = r[1];
				}
			}

			datas[0] = datas[0].trim();
			datas[1] = datas[1].trim();
			String[] times = datas[1].split(":");
			if (times.length < 2) {
				return null;
			} else {
				datas[1] = times[0] + ":" + times[1];
			}
			if (!datas[0].equalsIgnoreCase(getYearTime()))
				return datas[0] + " " + datas[1];
			else
				return null;
		} catch (Exception e) {
			System.out.println("err date:" + time);
		}
		return null;
	}

	private String filter(String time) {

		time = time.replaceAll("日", " ");
		time = time.replaceAll("　", " ");

		String filterData[] = new String[] { "/", "年", "月", "-", "/\\s*" };
		String filterTime[] = new String[] { ":", "点", "时", "分", "秒", ":\\s*" };

		for (int i = 1; i < filterData.length; i++) {
			time = time.replaceAll(filterData[i], filterData[0]);
		}

		for (int i = 1; i < filterTime.length; i++) {
			time = time.replaceAll(filterTime[i], filterTime[0]);
		}

		time = time.replaceAll("[^\\d/:\\s]", "");
		time = time.replaceAll(" +", " ");
		return time;

	}

	private String[] guess2Time(String time) {
		String[] resIsData = time.split("/");
		String[] resIsTime = time.split(":");
		Calendar cc = Calendar.getInstance();

		if (resIsData.length > 1) {
			if (resIsData.length == 2) {
				if (Integer.parseInt(resIsData[0]) <= 12) {
					return new String[] { DateFormat.getYear(cc) + time,
							getCurTime() };
				} else {
					return new String[] { time + "1", getCurTime() };
				}

			} else if (resIsData.length == 3) {
				if (resIsData[0].length() == 2) {
					if (Integer.parseInt(resIsData[0]) > DateFormat
							.getYearBy2Bit(cc)) {
						resIsData[0] = "19" + resIsData[0];
					} else {
						resIsData[0] = "20" + resIsData[0];
					}
					return new String[] {
							resIsData[0] + "/" + resIsData[1] + "/"
									+ resIsData[2], getCurTime() };
				} else {
					return new String[] {
							resIsData[0] + "/" + resIsData[1] + "/"
									+ resIsData[2], getCurTime() };
				}

			}
		} else if (resIsTime.length > 1) {
			if (DateFormat.getTime(cc).compareTo(time.trim()) >= 0) {
				return new String[] { DateFormat.getDate(cc), time };
			} else {
				cc.add(Calendar.DATE, -1);
				return new String[] { DateFormat.getDate(cc), time };
			}
		}

		return null;
	}

}
