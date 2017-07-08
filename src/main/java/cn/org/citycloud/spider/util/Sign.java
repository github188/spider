package cn.org.citycloud.spider.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * 签名.并提供MD5的byte[16]和string之间的互相转换空串 String b41d8cb98f00d204a98009f8ac98427e
 * byte[] -44 29 -116 -39 -113 0 -78 4 -23 -128 9 -104 -20 -8 66 126
 */
public class Sign {

	private static final int MD5LEN = 16;

	/**
	 * 获取MD5签名.
	 */
	public static byte[] getSign(String text) {
		if (text == null)
			return new byte[] { -44, 29, -116, -39, -113, 0, -78, 4, -23, -128,
					9, -104, -20, -8, 66, 126 };
		byte[] sign = { -44, 29, -116, -39, -113, 0, -78, 4, -23, -128,
				9, -104, -20, -8, 66, 126 };
		try {
			sign = text.getBytes("utf-8");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			MessageDigest digester = MessageDigest.getInstance("MD5");
			digester.update(sign);
			sign = digester.digest();
			if (sign.length == MD5LEN)
				return sign;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	/**
	 * 
	 * 方法名：getMD5
	 * 作者：zxh
	 * 创建时间：2014年9月12日 下午9:49:57
	 * 描述：根据传递的参数生成MD5
	 * @param text 需要生成MD5的文本内容
	 * @return 返回计算之后的MD5码
	 */
	public static long getMD5(String text){
		return new BigInteger(Sign.getSign(text)).longValue();
	}
	
	/**
	 * byte[] to String
	 */
	public static String byteToString(byte[] sign) {
		String digestHexStr = "";
		for (int i = 0; i < sign.length; i++)
			digestHexStr += byteHEX(sign[i]);
		return digestHexStr;
	}
	
	/**
	 * byte to String
	 */
	private static String byteHEX(int ib) {

		byte[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
				'b', 'c', 'd', 'e', 'f' };
		byte[] ob = new byte[2];
		int h = 0;
		int l = 0;
		if (ib < 0) {

			ib = -ib;
			ib = ib - 1;
			// 补码to原码
			h = (ib >> 4) & 0x0f; 
			l = ib & 0x0f; 
			h = h | 0x8; 
		} else {
			h = (ib >> 4) & 0x0f;
			l = ib & 0x0f;
		}

		ob[0] = Digit[h];
		ob[1] = Digit[l];
		String s = new String(ob);
		return s;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(getMD5("http://search.tianya.cn/bbs?q=洪雅"));
	}

	
}
