package cn.org.citycloud.spider.constant;

/**
 * 类名： Constant
 * 包名： cn.org.citycloud.spider.constant
 * 作者： zxh 
 * 时间： 2014年8月11日 下午5:40:36
 * 描述： 
 */
public class Constant {

	/*********************************************
	 * 											 *
	 *            HttpClient所用常量声明                    *
	 *                                           *
	 *********************************************/

	// Httpclient连接池连接数量
	public static final int HTTPCLIENT_CONNECTION_COUNT = 20;
	// 同一个路由最大链接数量
	public static final int HTTPCLIENT_MAXPERROUTE_COUNT = 20;
	// 设置连接超时
	public static final int HTTPCLIENT_CONNECT_TIMEOUT = 1000 * 4;
	// 设置响应超时
	public static final int HTTPCLIENT_SOCKET_TIMEOUT = 1000 * 4;
	// 设置user-agent
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36";
	// 响应状态码
	public static final int PAGE_200 = 200;
	// (错误请求)服务器不理解请求的语法
	public static final int PAGE_400 = 400;
	// 服务器禁止访问
	public static final int PAGE_403 = 403;
	// 服务器找不到请求的网页
	public static final int PAGE_404 = 404;
	// 网关错误
	public static final int PAGE_502 = 502;
	// 字符编码匹配正则
	public static final String CHARSET_REGEX = "(?:<meta.*)(?:charset=)(?:['\\\"]?)(\\w*-?\\d*)";

	/*********************************************
	 * 											 *
	 *              字符编码所用常量声明                      *
	 *                                           *
	 *********************************************/

	public static final String CHARSET_UTF8 = "UTF-8";
	public static final String CHARSET_GBK = "GBK";
	public static final String CHARSET_ZHCN = "zh-cn";

	// 更新列表页更新周期为3天
	public static final int UPDATE_PERIOD = 1000 * 60 * 5;

	// 列表页线程池大小
	public static final int LIST_THREAD_POOL = 5;

	// 正文页线程池大小
	public static final int CONTENT_THREAD_POOL = 30;

	// solr服务器的URL
	public final static String SOLR_SERVER_URL = "http://10.0.37.23:8080/solr/collection1";

	// bloom配置文件路径
	public static final String BLOOM_FILTER_FILE = "H:\\testdata\\bloom\\bloom.txt";

}
