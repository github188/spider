package cn.org.citycloud.spider.load;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.ByteArrayBuffer;

import cn.org.citycloud.spider.constant.Constant;
import cn.org.citycloud.spider.util.Util;

/**
 * 类名： Load
 * 包名： cn.org.citycloud.spider.load
 * 作者： zxh
 * 时间： 2014年8月15日 下午8:04:21
 * 描述： 下载类
 */
public class Load {

	private static CloseableHttpClient httpClient;

	private Load() {
	}

	/**
	 * 初始化HttpClient客户端
	 * 		配置连接池；
	 * 		设置重定向策略；
	 * 		设置连接和读取超时等参数。
	 */
	static {

		// 创建httpclient连接池
		PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager();
		// 最大连接数
		httpClientConnectionManager
				.setMaxTotal(Constant.HTTPCLIENT_CONNECTION_COUNT);
		// 同一个路由最大连接数
		httpClientConnectionManager
				.setDefaultMaxPerRoute(Constant.HTTPCLIENT_MAXPERROUTE_COUNT);
		// 创建全局的requestConfig
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(Constant.HTTPCLIENT_CONNECT_TIMEOUT)
				.setSocketTimeout(Constant.HTTPCLIENT_SOCKET_TIMEOUT)
				.setCookieSpec(CookieSpecs.BEST_MATCH).build();
		// 声明重定向策略对象
		LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();

		// 创建httpclient客户端
		httpClient = HttpClients.custom()
				.setConnectionManager(httpClientConnectionManager)
				.setDefaultRequestConfig(requestConfig)
				.setRedirectStrategy(redirectStrategy)
				.setUserAgent(Constant.USER_AGENT).build();

	}

	public static CloseableHttpClient getHttpClient() {
		return httpClient;
	}

	public static void setHttpClient(CloseableHttpClient httpClient) {
		Load.httpClient = httpClient;
	}

	/**
	 * 
	 * 方法名：load
	 * 作者：zxh
	 * 创建时间：2014年8月15日 下午8:29:37
	 * 描述：下载网页源码
	 * @param url 所需下载网页
	 * @return 网页源码
	 */
	public static String load(String url) {
		if (url == null || "".equals(url))
			return null;
		String src = null;
		CloseableHttpResponse response = null;
		try {
			// 去除url首尾空格
			url = url.trim();
			// 创建请求方式
			HttpGet get = new HttpGet(url);
			// 执行请求
			response = httpClient.execute(get);

			// 得到响应状态码
			int statusCode = response.getStatusLine().getStatusCode();

			// 根据状态码进行逻辑处理
			switch (statusCode) {
			case Constant.PAGE_200:
				// 获得响应实体
				try {
					HttpEntity entity = response.getEntity();

					// 首先获得字符编码
					String charset = null;
					ContentType contentType = null;
					try {
						contentType = ContentType.getOrDefault(entity);
						Charset charsets = contentType.getCharset();
						if (charsets != null) {
							charset = charsets.toString();
						}
					} catch (Exception e1) {
						if (Constant.CHARSET_ZHCN.equals(e1.getMessage())) {
							charset = Constant.CHARSET_ZHCN;
						}
					}

					// 获得响应流
					InputStream is = entity.getContent();
					ByteArrayBuffer buffer = new ByteArrayBuffer(4096);
					byte[] tmp = new byte[4096];
					int count;
					try {
						while ((count = is.read(tmp)) != -1) {
							buffer.append(tmp, 0, count);
						}
					} catch (EOFException e) {
						System.err.println("EOFException : " + url);
					}

					// 如果上一步没有获得字符编码。那直接从网页源码中的meta标签中更具正则匹配
					if (charset == null || "".equals(charset)
							|| Constant.CHARSET_ZHCN.equalsIgnoreCase(charset)
							|| "null".equalsIgnoreCase(charset)) {
						String regEx = Constant.CHARSET_REGEX;
						Pattern p = Pattern.compile(regEx,
								Pattern.CASE_INSENSITIVE);
						Matcher m = p.matcher(new String(buffer.toByteArray()));
						boolean result = m.find();
						if (result) {
							if (m.groupCount() == 1) {
								charset = m.group(1);
							}
						} else {
							// 出现了未解决的字符编码，要记录(目前还未出现这种情况)
							// 在这里先给默认的gbk
							charset = Constant.CHARSET_GBK;
							System.out.println("字符编码未匹配到 : " + url);
						}
					}
					// System.out.println(charset);
					src = new String(buffer.toByteArray(), charset);
					// 替换一些特殊字符串
					src = replaceStr(src);
					// 将源码中的Unicode编码转换成utf-8
					src = Util.decodeUnicode(src);
					break;
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					return null;
				} catch (SocketException e) {
					e.printStackTrace();
					return null;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					return null;
				} catch (UnsupportedCharsetException e) {
					e.printStackTrace();
					return null;
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			case Constant.PAGE_400:
				// page 400 错误，需要记录
				System.out.println("400 : " + url);
				break;
			case Constant.PAGE_403:
				// page 403 错误，需要记录
				System.out.println("403 : " + url);
				break;
			case Constant.PAGE_404:
				// page 404 错误，需要记录
				System.out.println("404 : " + url);
				break;
			case Constant.PAGE_502:
				// page 502 错误，需要记录
				System.out.println("502 : " + url);
				break;
			default:
				System.out.println("未捕获的状态码 : " + url);
				break;
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return src;

	}

	/**
	 * 
	 * 方法名：replaceStr
	 * 作者：zxh
	 * 创建时间：2014年8月19日 下午4:56:33
	 * 描述：替换网页源码中的一些特殊字符
	 * @param src
	 * @return
	 */
	public static String replaceStr(String src) {
		if (src == null || "".equals(src))
			return null;
		src = src.replaceAll("<!--", "");
		src = src.replaceAll("-->", "");
		src = src.replaceAll("&lt;", "<");
		src = src.replaceAll("&gt;", ">");
		src = src.replaceAll("&quot;", "\"");
		src = src.replaceAll("&nbsp;", " ");
		src = src.replaceAll("&amp;", "&");
		return src;
	}

	public static void main(String[] args) {
		System.out.println(load("http://eps.xinjinsteel.com/Custom/News/ViewNews.aspx?id=17496"));
	}
}
