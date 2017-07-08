package cn.org.citycloud.spider;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.org.citycloud.spider.constant.Constant;
import cn.org.citycloud.spider.content.ContentDownloadThread;
import cn.org.citycloud.spider.list.ListDownloadThread;
import cn.org.citycloud.spider.queue.ListQueue;
import cn.org.citycloud.spider.rule.ContentRule;
import cn.org.citycloud.spider.rule.ListRule;

/**
 * 类名： Start
 * 包名： cn.org.citycloud.spider
 * 作者： zxh
 * 时间： 2016年7月13日 上午9:23:23
 * 描述： 
 */
public class Start {

	private static HashMap<String, ListRule> listRules = new HashMap<String, ListRule>();;

	private static HashMap<String, ContentRule> contentRules = new HashMap<String, ContentRule>();;

	public static void main(String[] args) {

		// 初始化爬虫模块
		init();

		// 初始化列表页线池并启动，负责对列表页进行下载解析，将正文url连接放入队列中，生产者
		ExecutorService listExecutor = Executors
				.newFixedThreadPool(Constant.LIST_THREAD_POOL);

		for (int i = 0; i < Constant.LIST_THREAD_POOL; i++) {
			ListDownloadThread listThread = new ListDownloadThread();
			listExecutor.execute(listThread);
		}

		// 初始化正文页线程池并启动，负责对正文页进行下载解析，封装为结构化的文本对象并发送到solr存储，消费者
		ExecutorService contentExecutor = Executors
				.newFixedThreadPool(Constant.CONTENT_THREAD_POOL);

		for (int i = 0; i < Constant.CONTENT_THREAD_POOL; i++) {
			ContentDownloadThread contentThread = new ContentDownloadThread();
			contentExecutor.execute(contentThread);
		}
	}

	/**
	 * name：init
	 * author：zxh
	 * time：2017年3月8日 下午6:33:43
	 * description：初始化设置
	 */
	public static void init() {

		// 初始化种子队列
		initSeeds();

		// 初始化列表页规则
		initListRule();

		// 初始化正文页规则
		initContentRule();
	}

	/**
	 * name：initSeeds
	 * author：zxh
	 * time：2017年3月8日 下午6:31:57
	 * description：初始化种子队列
	 */
	public static void initSeeds() {
		ListQueue.put("http://bbs.tropica.cn/forum.php?mod=guide");
	}

	/**
	 * name：initListRule
	 * author：zxh
	 * time：2017年3月8日 下午6:16:38
	 * description：封装列表页规则集合
	 */
	public static void initListRule() {

		ListRule rule = new ListRule("bbs.tropica.cn",
				"div.content-list>ul>li>a");

		listRules.put("bbs.tropica.cn", rule);
	}

	/**
	 * name：initContentRule
	 * author：zxh
	 * time：2017年3月8日 下午6:16:41
	 * description：封装正文页规则集合
	 */
	public static void initContentRule() {

		ContentRule rule = new ContentRule("news.chemnet.com",
				"div.line35.bold.font24.fontblack",
				"div.detail-text.line25.font14px", "p.line22.fontgrey");

		ContentRule rule2 = new ContentRule("www.chemn.com",
				"div.newsContent>h1>font", "div.newsdetailMain",
				"div.newsdetailTop");

		ContentRule rule3 = new ContentRule("www.chemcp.com", "div.bt",
				"td.newsbox>table>tbody>tr>td.da",
				"td.newsbox>table>tbody>tr>td[height=20]");

		//ContentRule rule4 = new ContentRule("www.china-ep.com",
		//		"h1.title>strong", "div.content", "div.info");

		ContentRule rule5 = new ContentRule("news.17huanbao.com",
				"div.condiv>h1", "div.con", "div.source>span.right");

		contentRules.put("news.chemnet.com", rule);
		contentRules.put("www.chemn.com", rule2);
		contentRules.put("www.chemcp.com", rule3);
		//contentRules.put("www.china-ep.com", rule4);
		contentRules.put("news.17huanbao.com", rule5);
	}

	public static HashMap<String, ListRule> getListRules() {
		return listRules;
	}

	public static HashMap<String, ContentRule> getContentRules() {
		return contentRules;
	}

}
