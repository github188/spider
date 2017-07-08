package cn.org.citycloud.spider.queue;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 类名： ContentQueue
 * 包名： cn.org.citycloud.spider.queue
 * 作者： zxh
 * 时间： 2016年7月13日 上午9:47:41
 * 描述： 正文页队列
 */
public class ContentQueue {

	// 实例化一个阻塞队列，默认capacity为Integer.MAX_VALUE，put和take操作自带锁
	private static BlockingQueue<String> queue = new LinkedBlockingQueue<String>();

	/**
	 * 
	 * 方法名：put
	 * 作者：zxh
	 * 创建时间：2016年7月13日 上午10:05:24
	 * 描述：将待下载的正文url放入队列
	 * @param url
	 */
	public static void put(String url) {

		try {
			// 如果队列元素满的话会一直阻塞
			queue.put(url);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 方法名：put
	 * 作者：zxh
	 * 创建时间：2016年7月13日 下午12:09:54
	 * 描述：批量入队
	 * @param urls
	 */
	public static void put(ArrayList<String> urls) {

		try {
			
			for(String url : urls){
				// 如果队列元素满的话会一直阻塞
				queue.put(url);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 方法名：get
	 * 作者：zxh
	 * 创建时间：2016年7月13日 上午10:05:46
	 * 描述：从队列中获得需要下载的url
	 * @return 需要下载的url
	 */
	public static String get() {

		try {
			// 如果队列元素为空的话会一直阻塞
			return queue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

}
