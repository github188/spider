package cn.org.citycloud.spider.content;

import cn.org.citycloud.spider.es.EsService;
import cn.org.citycloud.spider.util.JsonUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.org.citycloud.spider.entry.News;
import cn.org.citycloud.spider.load.Load;
import cn.org.citycloud.spider.parser.Parser;
import cn.org.citycloud.spider.queue.ContentQueue;

/**
 * 类名： ContentDownloadThread
 * 包名： cn.org.citycloud.spider.content
 * 作者： zxh
 * 时间： 2016年7月13日 上午10:12:10
 * 描述： 正文页下载线程类
 */
public class ContentDownloadThread extends Thread {

	private Log log = LogFactory.getLog("list");

	// 解析器
	private Parser parser = new Parser();

	@Override
	public void run() {

		// 正文页url
		String url = null;
		// 正文源码
		String src = null;
		// movie对象
		News news = null;
		while (true) {

			try {
				url = ContentQueue.get();
				if (url != null && !"".equals(url)) {

					log.info("*******************开始下载正文页：" + url
							+ "*******************");
					// 下载源码
					src = Load.load(url);

					if (src == null || "".equals(src)) {
						log.error("*******************正文页下载失败：" + url
								+ "*******************");
						continue;
					}
					news = parser.parseContent(url, src);
					log.info("*******************正文页解析完成：" + url
							+ "*******************");

					if (news.getTitle() == null || "".equals(news.getTitle())
							|| news.getContent() == null
							|| "".equals(news.getContent())) {
						log.error("*******************正文页解析失败：" + url
								+ "*******************");
						continue;
					}
					log.info(JsonUtils.obj2json(news));
					// 发送索引
					EsService.getInstance().createDoc("iusofts","fish",JsonUtils.obj2json(news));
					//SolrJ.insert(news);
					// 休眠5秒，不要求实时性，防止IP被封
					Thread.sleep(1000 * 1);
				}
				// 睡眠
				Thread.sleep(1000 * 5);
			} catch (Exception e) {
				log.error("*******************正文页解析失败：" + url
						+ "*******************");
				e.printStackTrace();
				ContentQueue.put(url);
			}
		}
	}
}
