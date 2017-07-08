package cn.org.citycloud.spider.entry;

/**
 * 类名： News
 * 包名： cn.org.citycloud.spider.entry
 * 作者： zxh
 * 时间： 2016年7月13日 下午1:53:07
 * 描述： movie实体
 * 
 */
/**
 * 类名： Movie
 * 包名： com.movie.entry
 * 作者： zxh
 * 时间： 2016年7月13日 下午6:05:22
 * 描述： 
 */
public class News {

	// 站点host
	private String host;

	// url地址
	private String url;

	// 标题
	private String title;

	// 正文內容
	private String content;

	// 信息发布时间
	private String publishTime;

	// 数据采集时间
	private String crawlerTime;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getCrawlerTime() {
		return crawlerTime;
	}

	public void setCrawlerTime(String crawlerTime) {
		this.crawlerTime = crawlerTime;
	}

	@Override
	public String toString() {
		return "News host=" + host + ", url=" + url
				+ ", title=" + title + ", content=" + content
				+ ", publishTime=" + publishTime + ", crawlerTime="
				+ crawlerTime;
	}
	
}