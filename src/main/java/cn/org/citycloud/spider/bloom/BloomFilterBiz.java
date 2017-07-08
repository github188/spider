/**
  * 项目名：SpiderCrawler
  * 文件名：BloomFilterBiz.java
  * 作者：zxh
  * 时间：2014年8月21日 下午2:34:27
  * 描述：布隆过滤器的业务层
  */
package cn.org.citycloud.spider.bloom;

import java.util.ArrayList;

import cn.org.citycloud.spider.constant.Constant;
import cn.org.citycloud.spider.util.Common;

/**
 * 类名： BloomFilterBiz
 * 包名： cn.org.citycloud.spider.bloom
 * 作者： zxh
 * 时间： 2014年8月21日 下午2:34:27
 * 描述： 布隆过滤器的业务层
 */
public class BloomFilterBiz {

	private BloomFilter filter = BloomFilter.getInstance();

	private static BloomFilterBiz bloomBiz = new BloomFilterBiz();

	private BloomFilterBiz() {
	}

	public static BloomFilterBiz getInstance() {
		return bloomBiz;
	}

	/**
	 * name：filterUrls
	 * author：zxh
	 * time：2017年3月9日 下午4:15:50
	 * description：使用bloom高效判重
	 * @param urls 原始url集合
	 * @return 过滤掉已经爬取过得url
	 */
	public synchronized ArrayList<String> filterUrls(ArrayList<String> urls) {

		ArrayList<String> news = new ArrayList<String>();
		
		for (String url : urls) {
			try {
				// 如果布隆配置文件中不包含此url
				if (!filter.contains(url)) {
					// 插入mongodb
					// 插入布隆配置文件
					filter.insert(url);
					Common.writeFile(Constant.BLOOM_FILTER_FILE, url);
					
					news.add(url);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return news;
	}
}
