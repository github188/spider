package cn.org.citycloud.spider.list;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.org.citycloud.spider.Start;
import cn.org.citycloud.spider.bloom.BloomFilterBiz;
import cn.org.citycloud.spider.constant.Constant;
import cn.org.citycloud.spider.load.Load;
import cn.org.citycloud.spider.parser.Parser;
import cn.org.citycloud.spider.queue.ContentQueue;
import cn.org.citycloud.spider.queue.ListQueue;
import cn.org.citycloud.spider.util.Util;

/**
 * 类名： ListDownloadThread
 * 包名： cn.org.citycloud.spider.list
 * 作者： zxh
 * 时间： 2016年7月13日 上午10:11:20
 * 描述： 列表页下载线程类
 */
public class ListDownloadThread extends Thread {

	BloomFilterBiz bloom = BloomFilterBiz.getInstance();

	private Log log = LogFactory.getLog("list");
	// 解析器
	private Parser parser = new Parser();

	@Override
	public void run() {

		try {

			// 网页源码
			String src = null;

			// 本次需要下载的列表页url
			String url = null;

			while (true) {

				url = ListQueue.get();

				// 正文页url集合
				ArrayList<String> urls = null;
				// 如果没有获取到下一页url的话则本次下载结束
				while (url != null && !"".equals(url)) {

					try {

						log.info("*******************开始下载列表页：" + url
								+ "*******************");
						// 下载当前页源码
						src = Load.load(url);
						if (src == null || "".equals(src)) {
							log.info("*******************列表页下载失败：" + url
									+ "*******************");
							Thread.sleep(1000 * 5);
							// 放回队尾
							ListQueue.put(url);
							continue;
						}
						// 解析网页源码并得到正文页url放入队列
						urls = parser.parseList(url, src, Start.getListRules()
								.get(Util.getHost(url)).getRule());

						// 过滤掉已经爬取过得url
						urls = bloom.filterUrls(urls);

						log.info("*******************列表页解析完成，发现新帖子数量"
								+ urls.size() + " " + url
								+ "*******************");

						if (urls.size() == 0) {
							break;
						}

						// 得到下一页url
						url = parser.parseNextPage(url, src);

						log.info("*******************下一页url：" + url
								+ "*******************");

						// 正文页url放入队列
						ContentQueue.put(urls);
						// 清空当前集合
						urls.clear();

						// 休眠5秒，不要求实时性，防止IP被封
						Thread.sleep(1000 * 5);

					} catch (Exception e) {
						log.info("*******************列表页下载失败：" + url
								+ "*******************");
						ListQueue.put(url);
						e.printStackTrace();
					}
				}
				log.info("*******************所有列表页抓取完成，"
						+ Constant.UPDATE_PERIOD
						+ "ms 后重新重新开始*******************");
				Thread.sleep(Constant.UPDATE_PERIOD);
				Start.initSeeds();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
