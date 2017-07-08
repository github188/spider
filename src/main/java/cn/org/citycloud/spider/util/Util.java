package cn.org.citycloud.spider.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.org.citycloud.spider.constant.Constant;


/**
 * 类名： Util
 * 包名： com.boryou.util
 * 作者： zxh
 * 时间： 2014年8月18日 下午8:06:42
 * 描述： 工具类
 */
public class Util {
	
	/**
	 * 
	 * 方法名：getList
	 * 作者：zxh
	 * 创建时间：2014年8月18日 下午8:15:32
	 * 描述：读取文件内容封装为集合
	 * @param path 文件路径
	 * 默认utf-8编码
	 * @return 封装后的集合
	 */
	public static ArrayList<String> getList(String path){
		return getList(path, Constant.CHARSET_UTF8);
	}
	
	public static String getHost(String url){
		try {
			URL u  = new URL(url);
			return u.getHost();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url;
	
	}
	/**
	 * 
	 * 方法名：getList
	 * 作者：zxh
	 * 创建时间：2014年8月18日 下午8:15:32
	 * 描述：读取文件内容封装为集合
	 * @param path 文件路径
	 * @param encoding 文件编码
	 * @return 封装后的集合
	 */
	public static ArrayList<String> getList(String path,String encoding){
		ArrayList<String> result = new ArrayList<String>();
		File file = new File(path);
		FileInputStream fis = null;
		BufferedReader br = null;
		try{
			fis = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fis,encoding));
			String line = null;
			while((line = br.readLine()) != null){
				line = line.trim();
				if(line.startsWith("#") || line.equalsIgnoreCase("")) continue;
				result.add(line);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			closeStream(fis, null, br, null);
		}
		return result;
	}
	
	/**
	 * 
	 * 方法名：getSet
	 * 作者：zxh
	 * 创建时间：2014年9月12日 下午5:45:54
	 * 描述：读取文件内容封装为set集合
	 * @param path 需要读取的文件路径
	 * @return 封装后的集合
	 */
	public static HashSet<String> getSet(String path){
		HashSet<String> result = new HashSet<String>();
		File file = new File(path);
		FileInputStream fis = null;
		BufferedReader br = null;
		try{
			fis = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fis,Constant.CHARSET_UTF8));
			String line = null;
			while((line = br.readLine()) != null){
				line = line.trim();
				if(line.startsWith("#") || line.equalsIgnoreCase("")) continue;
				result.add(line);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			closeStream(fis, null, br, null);
		}
		return result;
	}
	
	/**
	 * 
	 * 方法名：readFileByLineForMap
	 * 作者：zxh
	 * 创建时间：2014年9月13日 上午11:57:08
	 * 描述：读取文件内容封装为map集合
	 * @param path
	 * @return
	 */
	public static HashMap<String, Double> getMap(String path){
		
		HashMap<String, Double> result = new HashMap<String, Double>();
		File file = new File(path);
		FileInputStream fis = null;
		BufferedReader br = null;
		try{
			fis = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fis,Constant.CHARSET_UTF8));
			String line = null;
			String[] array = null;
			while((line = br.readLine()) != null){
				line = line.trim();
				if(line.startsWith("#") || line.equalsIgnoreCase("")) continue;
				array = line.split("=");
				if (array != null && array.length ==2) {
					result.put(array[0], Double.parseDouble(array[1]));
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			closeStream(fis, null, br, null);
		}
		return result;
	}
	
	/**
	 * 
	 * 方法名：writeFile
	 * 作者：zxh
	 * 创建时间：2014年8月21日 上午11:49:23
	 * 描述：将content内容写到文件中
	 * @param filePath 输出文件路径
	 * @param content 要输出的内容(如果不指定则使用UTF-8编码)
	 */
	public static void writeFile(String filePath, String content){
		writeFile(filePath, content, Constant.CHARSET_UTF8);
	}
	
	/**
	 * 
	 * 方法名：writeFile
	 * 作者：zxh
	 * 创建时间：2014年8月21日 上午11:48:42
	 * 描述：将content内容写到文件中
	 * @param filePath 输出文件路径
	 * @param content 要输出的内容
	 * @param encoding 字符编码
	 */
	public static void writeFile(String filePath, String content ,String encoding){
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try{
			fos = new FileOutputStream(filePath,true);
			osw = new OutputStreamWriter(fos,encoding);
			content = content + "\r\n";
			osw.write(content);
			osw.flush();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			closeStream(null, fos, null, osw);
		}
	}
	
	/**
	 * 
	 * 方法名：writeFile
	 * 作者：zxh
	 * 创建时间：2014年8月21日 上午11:48:42
	 * 描述：将content内容写到文件中
	 * @param filePath 输出文件路径
	 * @param content 要输出的内容
	 * @param encoding 字符编码
	 * @param cover 是否覆盖
	 */
	public static void writeFile(String filePath, String content ,String encoding, boolean cover){
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try{
			fos = new FileOutputStream(filePath,cover);
			osw = new OutputStreamWriter(fos,encoding);
			content = content + "\r\n";
			osw.write(content);
			osw.flush();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			closeStream(null, fos, null, osw);
		}
	}
	
	
	/**
	 * 
	 * 方法名：closeStream
	 * 作者：zxh
	 * 创建时间：2014年8月18日 下午8:15:52
	 * 描述：关闭打开的流对象
	 * @param is 可以是一切实现了InputStream接口的对象
	 * @param os 可以是一切实现了OutputStream接口的对象
	 * @param reader 可以是一切实现了Reader抽象类的对象
	 * @param writer 可以是一切实现了Writer抽象类的对象
	 */
	public static void closeStream(InputStream is, OutputStream os, Reader reader, Writer writer){
		try {
			if (is != null) {
				is.close();
			}
			if (os != null){
				os.close();
			}
			if (reader != null) {
				reader.close();
			}
			if (writer != null) {
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
   
	/**
	 * 
	 * 方法名：getText
	 * 作者：zxh
	 * 创建时间：2014年9月28日 下午2:35:44
	 * 描述：读取文件内容封装到String中
	 * @param path
	 * @return 返回封装后的String对象
	 */
	public static String getText(String path){
		
		if(path == null || "".equals(path)) return null;
		InputStream is = null;
		BufferedReader reader = null;
		try {
			is = new FileInputStream(new File(path));
			reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder builder = new StringBuilder();
			String line = null;
			while((line = reader.readLine()) != null){
				builder.append(line);
				
				
			}
			return builder.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			closeStream(is, null, reader, null);
		}
		
		return null;
	}
	
	/**
	 * 
	 * 方法名：isWinsOS
	 * 作者：lq
	 * 创建时间：2014年9月26日 下午4:54:27
	 * 描述：判断系统是否为windows系统
	 * @return
	 */
	 public static boolean isWinsOS(){
		 String os = System.getProperties().getProperty("os.name").toLowerCase();
		 if(os.startsWith("win")){
			 return true;
		 } else {
			 return false;
		 }
		 
	 }
	 
	/**
	 * 
	 * 方法名：cleanObj
	 * 作者：zxh
	 * 创建时间：2014年8月18日 下午8:16:16
	 * 描述：手动将对象置空，便于GC回收内存
	 * @param objs 可变长参数数组
	 */
	public static void cleanObj(Object ...objs){
		for (int i = 0; i < objs.length; i++) {
			objs[i] = null;
		}
	}
	/**
	 * 
	 * 方法名：isNumeric
	 * 作者：lq
	 * 创建时间：2014年10月15日 下午2:57:53
	 * 描述：判断字符串是否为纯数字组成
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){ 
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    return pattern.matcher(str).matches();    
	 } 

	/**
	 * 
	 * 方法名：decodeUnicode
	 * 作者：lq
	 * 创建时间：2014年9月22日 下午5:20:26
	 * 描述：将源码中的Unicode编码转换成utf-8
	 * @param theString
	 * @return
	 */
	public static String decodeUnicode(String theString) {
		if(theString == null) return null;
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException(
									"Malformed   \\uxxxx   encoding.");
						}

					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';

					else if (aChar == 'n')

						aChar = '\n';

					else if (aChar == 'f')

						aChar = '\f';

					outBuffer.append(aChar);

				}

			} else

				outBuffer.append(aChar);

		}

		return outBuffer.toString();

	}


	/**
	 * 
	 * 方法名：hexToString 
	 * 作者：zxh 
	 * 创建时间：2014年12月18日 上午8:44:15 
	 * 描述：将十六进制编码转换为字符串
	 * 
	 * @param content 需要转换的十六进制编码
	 * @return 转换后的字符串
	 */
	public static String hexToString(String content) {
		// 先将十六进制的\x替换掉
		content = content.replaceAll("\\\\x", "");
		if ("0x".equals(content.substring(0, 2))) {
			content = content.substring(2);
		}
		// 创建字节数组，长度是字符串长度除以2
		// 因为在将字符串转换为十六进制编码的时候是先将字符串转成字节数组，再将每一个字节转换为十六进制
		// 相当于字节数组中的每一个字节都对应着长度为2的字符串，（如-21对应E5）
		// 最后结果长度是字节数组长度乘以2，所以在这里还原字节数组的时候需要将字符串长度除以2
		byte[] b = new byte[content.length() / 2];
		for (int i = 0; i < b.length; i++) {
			try {
				// 每移动两位取出一个十六进制数，跟0xFF进行&操作，再将操作的结果转换为十进制字节，存放到对应的位置上
				b[i] = (byte) (0xFF & Integer.parseInt(
						content.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			content = new String(b, "UTF-8");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return content;
	}

	/**
	 * 
	 * 方法名：stringToHex 
	 * 作者：zxh 
	 * 创建时间：2014年12月18日 上午8:40:28 
	 * 描述：字符串转换十六进制
	 * 
	 * @param content 需要转换的字符串
	 * @return 转换后的结果
	 */
	public static String stringToHex(String content) {
		StringBuilder builder = new StringBuilder();
		// 首先将字符转换成字节数组
		byte[] b = content.getBytes();
		for (int i = 0; i < b.length; i++) {
			// 取出字节数组中的每一个元素进行，和0xFF进行&操作运算（注意负数需要取反加1），&操作需要将两个数都转换为二进制，同1得1，有0得0，再将位操作的结果转换为十六进制数
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			builder.append("\\x" + hex.toUpperCase());
		}

		return builder.toString();
	}
	
	/**
	 * 
	 * 方法名：match
	 * 作者：zxh
	 * 创建时间：2016年7月13日 下午6:18:54
	 * 描述：使用正则表达式来匹配需要的数据
	 * @param str 原始字符串
	 * @param regex 正则
	 * @return 匹配到的字符串
	 */
	public static String match(String str, String regex) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		boolean result = m.find();
		if (result) {
			if (m.groupCount() == 1) {
				return m.group(1);
			}
		}
		return str;
	}
	
	
	public static void main(String[] args) {
		String str = "冬/七天/冬.2015";
		System.out.println(match(str,"(.*?)/.*"));
	}
}
