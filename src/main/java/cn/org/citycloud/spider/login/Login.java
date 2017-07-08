package cn.org.citycloud.spider.login;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;

import cn.org.citycloud.spider.load.Load;

/**
 * class： Login
 * package： cn.org.citycloud.spider.login
 * author：zxh
 * time： 2017年3月15日 下午2:05:28
 * description： 
 */
public class Login {

	private static CloseableHttpClient httpClient = Load.getHttpClient();

	/**
	 * name：initLogin
	 * author：zxh
	 * time：2017年3月15日 下午2:11:27
	 * description：模拟登录，使用同一个HTTPclient登录所有需要登录的网站，
	 * 	类似与打开了一个浏览器一样，如果全部登录成功的话后期直接使用这个HTTPclient访问需要登录才能访问的正文页
	 */
	private void initLogin() {
		// 登录千里马
		loginQianlima();
		// 登录中国采购与招标网模拟登陆
		loginChinabidding();
		// 中国电力采购与招标网模拟登陆
		loginChinabiddingzb();
	}

	/**
	 * name：loginQianlima
	 * author：zxh
	 * time：2017年3月15日 上午9:37:43
	 * description：千里马网模拟登陆
	 * 	账号：zhouxiaohu
	 *  密码：zhouxianhu
	 */
	public void loginQianlima() {

		// 登录请求url
		String url = "http://center.qianlima.com/login_post.jsp";
		// post请求设置的header集合
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Host", "center.qianlima.com");

		// post请求填充的表单集合
		Map<String, String> posts = new HashMap<String, String>();
		posts.put("username", "zhouxiaohu");
		posts.put("password", "zhouxianhu");
		posts.put("rem_login", "1");

		// 登录
		login(url, headers, posts);
	}

	/**
	 * name：loginChinabidding
	 * author：zxh
	 * time：2017年3月15日 上午11:01:03
	 * description：中国采购与招标网模拟登陆
	 * 	账号：zhouxianhu
	 *  密码：zhouxiaohu
	 */
	public void loginChinabidding() {

		// 登录请求url
		String url = "https://www.chinabidding.com.cn/cblcn/member.login/logincheck";
		// post请求设置的header集合
		Map<String, String> headers = new HashMap<String, String>();
		// post请求填充的表单集合
		Map<String, String> posts = new HashMap<String, String>();
		posts.put("name", "zhouxianhu");
		posts.put("password", "zhouxiaohu");

		// 登录
		login(url, headers, posts);
	}

	/**
	 * name：loginChinabiddingzb
	 * author：zxh
	 * time：2017年3月15日 上午9:47:19
	 * description：中国电力采购与招标网模拟登陆
	 * 	账号：zhouxiaohu
	 *  密码：zhouxianhu
	 */
	public void loginChinabiddingzb() {

		// 登录请求url
		String url = "http://www.chinabiddingzb.com/user.php";
		// post请求设置的header集合
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Host", "www.chinabiddingzb.com");

		// post请求填充的表单集合
		Map<String, String> posts = new HashMap<String, String>();
		posts.put("username", "zhouxiaohu");
		posts.put("password", "zhouxianhu");

		// 登录
		login(url, headers, posts);
	}

	/**
	 * name：login
	 * author：zxh
	 * time：2017年3月15日 下午2:20:03
	 * description：模拟登录基类
	 * @param url 登录请求的url
	 * @param headers post请求发送时填充的header参数集合
	 * @param posts post请求发送时填充的表单集合
	 */
	public void login(String url, Map<String, String> headers,
			Map<String, String> posts) {

		CloseableHttpResponse response = null;
		try {

			// post方式提交请求
			HttpPost post = new HttpPost(url);

			// 遍历headers集合，填充参数所有header
			Set<Entry<String, String>> headerSet = headers.entrySet();

			for (Entry<String, String> entry : headerSet) {
				post.addHeader(entry.getKey(), entry.getValue());
			}

			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

			// 遍历posts集合，填充所有表单元素到post请求中
			Set<Entry<String, String>> postSet = posts.entrySet();
			for (Entry<String, String> entry : postSet) {
				params.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			}

			// 对表单参数编码
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params);

			// 将编码后的所有表单参数封装到post请求中
			post.setEntity(formEntity);
			// 提交请求
			response = httpClient.execute(post);
			// 得到响应状态码
			int status = response.getStatusLine().getStatusCode();

			// 登录成功
			if (status == 200) {
				System.out.println("登录成功: " + status);
			} else {
				System.out.println("登录失败: " + status);
			}

		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * name：main
	 * author：zxh
	 * time：2017年3月15日 下午2:05:28
	 * description：
	 * @param args
	 */
	public static void main(String[] args) {
		new Login().initLogin();

		// System.out
		// .println(Load
		// .load("http://www.qianlima.com/zb/detail/20170315_52482278.html"));
		//
		// System.out.println(Load
		// .load("http://www.chinabidding.com.cn/mfgg/n5niL.html"));
		//
		// System.out.println(Load
		// .load("http://www.chinabiddingzb.com/display.php?id=1094231"));

	}

}
