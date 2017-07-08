package cn.org.citycloud.spider.bloom;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.BitSet;

import cn.org.citycloud.spider.constant.Constant;
import cn.org.citycloud.spider.util.Common;

/**
 * 类名： BloomFilter 
 * 包名： cn.org.citycloud.spider.bloom
 * 作者： 高威 
 * 时间： 2014年8月21日
 * 上午11:55:14 描述：
 */
public class BloomFilter {

	private static BloomFilter filter = new BloomFilter();

	// 构造函数
	private BloomFilter() {
		// 初始化布隆过滤器
		init();
	}

	public static BloomFilter getInstance() {
		return filter;
	}

	// DEFAULT_SIZE为2的28次方
	private final int DEFAULT_SIZE = 2 << 28;
	/* 不同哈希函数的种子，一般应取质数,seeds数据共有7个值，则代表采用8种不同的HASH算法 */
	private final int[] seeds = new int[] { 5, 7, 11, 13, 31, 37, 61 };

	// BitSet实际是由“二进制位”构成的一个Vector。假如希望高效率地保存大量“开－关”信息，就应使用BitSet.
	// BitSet的最小长度是一个长整数（Long）的长度：64位
	private BitSet bits = new BitSet(DEFAULT_SIZE);

	/* 哈希函数对象 */
	private SimpleHash[] func = new SimpleHash[seeds.length];

	// 将字符串标记到bits中，即设置字符串的8个hash值函数为1
	public synchronized void insert(String value) {
		for (SimpleHash f : func) {
			bits.set(f.hash(value), true);
		}

	}

	// 判断字符串是否已经被bits标记
	public synchronized boolean contains(String value) {
		// 确保传入的不是空值
		if (value == null) {
			return false;
		}

		// 计算7种hash算法下各自对应的hash值，并判断
		for (SimpleHash f : func) {
			if (!bits.get(f.hash(value)))
				return false;
		}
		return true;
	}

	public void init() {

		for (int i = 0; i < seeds.length; i++) {
			// 给出所有的hash值，共计seeds.length个hash值。共8位。
			// 通过调用SimpleHash.hash(),可以得到根据7种hash函数计算得出的hash值。
			// 传入DEFAULT_SIZE(最终字符串的长度），seeds[i](一个指定的质数)即可得到需要的那个hash值的位置。
			func[i] = new SimpleHash(DEFAULT_SIZE, seeds[i]);
		}

		InputStream is = null;
		BufferedReader br = null;
		try {
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			is = new FileInputStream(Constant.BLOOM_FILTER_FILE);
			br = new BufferedReader(new InputStreamReader(is,
					Constant.CHARSET_UTF8));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (!"".equals(line)) {
					insert(line.trim());
				}
			}
		} catch (FileNotFoundException e) {
			// 布隆配置文件未加载到
			// 需要记录日志
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Common.closeStream(is, null, br, null);
		}

	}

	/* 哈希函数类 */
	public static class SimpleHash {
		// cap为DEFAULT_SIZE的值，即用于结果的最大的字符串长度。
		// seed为计算hash值的一个给定key，具体对应上面定义的seeds数组
		private int cap;
		private int seed;

		public SimpleHash(int cap, int seed) {
			this.cap = cap;
			this.seed = seed;
		}

		public int hash(String value) {
			int result = 0;
			int len = value.length();
			for (int i = 0; i < len; i++) {
				result = seed * result + value.charAt(i);
			}
			return (cap - 1) & result;
		}

	}

	public static void main(String[] args) {

		System.out.println(BloomFilter.getInstance().contains(
				"http://www.plating.org/news_info.asp?pid=28&id=2857"));
	}

}
