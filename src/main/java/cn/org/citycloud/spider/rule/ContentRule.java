package cn.org.citycloud.spider.rule;

/**
 * class： ContentRule
 * package： cn.org.citycloud.spider.rule
 * author：zxh
 * time： 2017年3月8日 下午5:37:16
 * description： 正文页模板规则
 */
public class ContentRule {

	// 网站host
	private String host;

	// 标题规则
	private String title;

	// 正文规则
	private String content;

	// 发布时间规则
	private String publishTime;

	public ContentRule(String host, String title, String content,
			String publishTime) {
		super();
		this.host = host;
		this.title = title;
		this.content = content;
		this.publishTime = publishTime;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

}
