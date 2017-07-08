package cn.org.citycloud.spider.rule;
/**
 * class： ListRule
 * package： cn.org.citycloud.spider.rule
 * author：zxh
 * time： 2017年3月8日 下午5:36:57
 * description： 列表页模板规则
 */
public class ListRule {

	// 网站host
	private String host;

	// 列表页规则，需要符合jsoup语法
	private String rule;

	public ListRule(String host, String rule) {
		super();
		this.host = host;
		this.rule = rule;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	@Override
	public String toString() {
		return "ListTemplate [host=" + host + ", rule=" + rule + "]";
	}

}
