package cn.org.citycloud.spider.parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.org.citycloud.spider.rule.ListRule;
import org.apache.http.client.ClientProtocolException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.org.citycloud.spider.Start;
import cn.org.citycloud.spider.entry.News;
import cn.org.citycloud.spider.load.Load;
import cn.org.citycloud.spider.rule.ContentRule;
import cn.org.citycloud.spider.util.Sign;
import cn.org.citycloud.spider.util.TimeParser;
import cn.org.citycloud.spider.util.Util;

/**
 * 类名： Parser
 * 包名： cn.org.citycloud.spider.parser
 * 作者： zxh
 * 时间： 2016年7月13日 上午11:14:39
 * 描述： 网页源码解析器
 */
public class Parser {

	private TimeParser timeParser = new TimeParser();
	
	// 时间匹配正则
	private static final String TIME_REGEX = "(\\s*\\d{2,4}[/\\-])?\\s*\\d{1,2}[/\\-]\\d{1,2}\\s+(\\d{1,2}:)?(\\d{1,2}:)?(\\d{1,2})?\\s*|\\d{2}:\\d{2}|\\d{1,2}-\\d{1,2}|\\d{4}年\\d{2}月\\d{2}日(\\d{1,2}:)?(\\d{1,2}:)?(\\d{1,2})?|\\d{1,2}小时前|d{1,4}分钟前|d{1,4}秒前";

	private final static String[] NEXT_PAGE_URL = new String[] { "下一页", "下页",
			"后页", "next", "older", "›", "下一页»", "下一页>", "››", "后一页", "下一页 ▸",
			"下一页 >" };

	/**
	 * 
	 * 方法名：parseList
	 * 作者：zxh
	 * 创建时间：2016年7月13日 上午11:15:30
	 * 描述：解析列表页网页源码
	 * @param url 网页url
	 * @param src 网页源码
	 * @param rule 正文规则
	 * @return 返回解析好的正文页url集合
	 */
	public ArrayList<String> parseList(String url, String src, String rule) {

		ArrayList<String> urls = new ArrayList<String>();
		// 根据网页源码封装document树
		Document document = Jsoup.parse(src);
		// 获得所有正文超链接元素
		Elements elements = document.select(rule);
		for (Element e : elements) {
			// 这里url可能是相对的，所以需要补全url
			urls.add(replenishUrl(url, e.attr("href")));
		}
		return urls;
	}

	/**
	 * 
	 * 方法名：parseNextPage
	 * 作者：zxh
	 * 创建时间：2016年7月13日 下午12:12:18
	 * 描述：从网页源码中获得下一页的url
	 * @param url 网页url
	 * @param src 网页源码
	 * @return 返回下一页url
	 */
	public String parseNextPage(String url, String src) {
		// 根据网页源码封装document树
		try {
			Document document = Jsoup.parse(src);
			// 匹配网页源码a标签值包含下一页这个字符串的元素，并取第一个
			Element e = document.select("a:matchesOwn(下一页)").size() < 1 ? null
					: document.select("a:matchesOwn(下一页)").get(0);

			if (e == null) {
				e = document.select("a:matchesOwn(后页)").size() < 1 ? null
						: document.select("a:matchesOwn(后页)").get(0);
			}

			if (e == null) {
				e = document.select("a[title=下一页]").size() < 1 ? null
						: document.select("a[title=下一页]").get(0);
			}

			// 补全相对url
			return replenishUrl(url, e.attr("href"));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * 方法名：getNextPageUrl
	 * 作者：zxh
	 * 创建时间：2014年8月19日 下午8:24:08
	 * 描述：抽取下一页url (目前还很粗糙)
	 * @param title
	 * @param titleURL
	 * @return
	 */
	public String getNextPageUrl(String title, String titleURL) {
		for (String nextPageUrl : NEXT_PAGE_URL) {
			if (title.equalsIgnoreCase(nextPageUrl)) {
				return titleURL;
			}
		}
		return null;
	}

	/**
	 * 
	 * 方法名：replenishUrl
	 * 作者：zxh
	 * 创建时间：2014年8月19日 下午5:43:43
	 * 描述：补全网页源码中的相对路径的URL
	 * @param url 原网页url
	 * @param relativeUrl 原网页url网页源码中获得的各种相对url
	 * @return 补全后的url
	 */
	public String replenishUrl(String url, String relativeUrl) {

		try {
			return new URL(new URL(url), relativeUrl).toString();
		} catch (MalformedURLException e) {
			System.err.println(relativeUrl);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * 方法名：parsePost
	 * 作者：zxh
	 * 创建时间：2016年7月13日 下午2:16:30
	 * 描述：解析正文页源码封装为movie对象
	 * @param url 正文页url
	 * @param src 正文页源码
	 * @return 正文对象集合
	 */
	public News parseContent(String url, String src) {

		News news = new News();

		// host
		news.setHost(Util.getHost(url));

		// 获得该正文页解析规则
		ContentRule rule = Start.getContentRules().get(news.getHost());

		// url
		news.setUrl(url);

		// 根据网页源码封装document树
		Document document = Jsoup.parse(src);

		// title
		String title = document.select(rule.getTitle()).size() > 0 ? document
				.select(rule.getTitle()).get(0).text() : "";
		news.setTitle(title);

		// content
		String content = document.select(rule.getContent()).size() > 0 ? document
				.select(rule.getContent()).get(0).text()
				: "";

		news.setContent(content);
		// 发布时间
		String publishTime = document.select(rule.getPublishTime()).size() > 0 ? document
				.select(rule.getPublishTime()).get(0).text()
				: "";

		publishTime = publishTime.replaceAll("[年月日]", "-");
		// 提取时间
		news.setPublishTime(getTime(publishTime));

		// 抓取时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String crawlerTime = sdf.format(System.currentTimeMillis());
		news.setCrawlerTime(crawlerTime);

		return news;
	}

	/**
	 * name：getTime
	 * author：zxh
	 * time：2017年3月8日 下午5:17:22
	 * description：提取时间字符串
	 * @param text
	 * @return 时间
	 */
	public String getTime(String text) {

		// 时间格式化
		SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		SimpleDateFormat timeFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		// 首先创建时间正则pattern对象
		Pattern pattern = Pattern.compile(TIME_REGEX);
		Matcher matcher = pattern.matcher(text);
		String time = null;

		if (matcher.find()) {
			time = timeParser.getStrTime(text);
		} else {
			time = date.format(new Date());
		}

		// 对日期格式进行调整
		try {
			Date temp = date.parse(time);
			time = timeFormat.format(temp);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return time;
	}

	public static void main(String[] args) throws ClientProtocolException,
			IOException {

		/*ContentRule rule = new ContentRule("news.17huanbao.com",
				"div.condiv>h1", "div.con", "div.source>span.right");

		Start.getContentRules().put("news.17huanbao.com", rule);

		String url = "http://news.17huanbao.com/hyxw/news_124469.html";
		String src = Load.load(url);
		News news = new Parser().parseContent(url, src);

		System.out.println(news);
		if (news.getTitle() == null || "".equals(news.getTitle())
				|| news.getContent() == null || "".equals(news.getContent())) {
			System.out.println("*******************正文页解析失败：" + url
					+ "*******************");
		}*/

		ListRule rule = new ListRule("bbs.tropica.cn",
				"div.bm_c>ul>li>a");
		int i=0;
		++i;
		System.out.println(i);

	}
}
