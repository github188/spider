/**
  * 项目名：SpiderCrawler
  * 文件名：Common.java
  * 作者：zxh
  * 时间：2014年8月18日 下午8:06:42
  * 描述：工具类 
  */
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Pattern;

import cn.org.citycloud.spider.constant.Constant;

/**
 * 类名： Util
 * 包名： com.boryou.util
 * 作者： zxh
 * 时间： 2014年8月18日 下午8:06:42
 * 描述： 工具类
 */
public class Common {

	/**
	 * 
	 * 方法名：readFileByLine
	 * 作者：zxh
	 * 创建时间：2014年8月18日 下午8:15:32
	 * 描述：读取文件内容封装为集合
	 * @param path 文件路径
	 * 默认utf-8编码
	 * @return 封装后的集合
	 */
	public static ArrayList<String> readFileByLine(String path) {
		return readFileByLine(path, Constant.CHARSET_UTF8);
	}

	/**
	 * 
	 * 方法名：readFileByLine
	 * 作者：zxh
	 * 创建时间：2014年8月18日 下午8:15:32
	 * 描述：读取文件内容封装为集合
	 * @param path 文件路径
	 * @param encoding 文件编码
	 * @return 封装后的集合
	 */
	public static ArrayList<String> readFileByLine(String path, String encoding) {
		ArrayList<String> result = new ArrayList<String>();
		File file = new File(path);
		FileInputStream fis = null;
		BufferedReader br = null;
		try {
			fis = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fis, encoding));
			String line = null;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("#") || line.equalsIgnoreCase(""))
					continue;
				result.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			closeStream(fis, null, br, null);
		}
		return result;
	}

	/**
	 * 
	 * 方法名：readFileByLineForSet
	 * 作者：zxh
	 * 创建时间：2014年9月12日 下午5:45:54
	 * 描述：读取文件内容封装为set集合
	 * @param path 需要读取的文件路径
	 * @return 封装后的集合
	 */
	public static HashSet<String> readFileByLineForSet(String path) {
		HashSet<String> result = new HashSet<String>();
		File file = new File(path);
		FileInputStream fis = null;
		BufferedReader br = null;
		try {
			fis = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fis,
					Constant.CHARSET_UTF8));
			String line = null;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("#") || line.equalsIgnoreCase(""))
					continue;
				result.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
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
	public static HashMap<String, Double> readFileByLineForMap(String path) {

		HashMap<String, Double> result = new HashMap<String, Double>();
		File file = new File(path);
		FileInputStream fis = null;
		BufferedReader br = null;
		try {
			fis = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fis,
					Constant.CHARSET_UTF8));
			String line = null;
			String[] array = null;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("#") || line.equalsIgnoreCase(""))
					continue;
				array = line.split("=");
				if (array != null && array.length == 2) {
					result.put(array[0], Double.parseDouble(array[1]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
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
	public static void writeFile(String filePath, String content) {
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
	public static void writeFile(String filePath, String content,
			String encoding) {
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try {
			fos = new FileOutputStream(filePath, true);
			osw = new OutputStreamWriter(fos, encoding);
			content = content + "\r\n";
			osw.write(content);
			osw.flush();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
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
	public static void writeFile(String filePath, String content,
			String encoding, boolean cover) {
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try {
			fos = new FileOutputStream(filePath, cover);
			osw = new OutputStreamWriter(fos, encoding);
			content = content + "\r\n";
			osw.write(content);
			osw.flush();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
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
	public static void closeStream(InputStream is, OutputStream os,
			Reader reader, Writer writer) {
		try {
			if (is != null) {
				is.close();
			}
			if (os != null) {
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
	 * 方法名：getContent
	 * 作者：zxh
	 * 创建时间：2014年9月28日 下午2:35:44
	 * 描述：读取文件内容封装到String中
	 * @param path
	 * @return 返回封装后的String对象
	 */
	public static String getContent(String path) {

		if (path == null || "".equals(path))
			return null;
		InputStream is = null;
		BufferedReader reader = null;
		try {
			is = new FileInputStream(new File(path));
			reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			return builder.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
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
	public static boolean isWinsOS() {
		String os = System.getProperties().getProperty("os.name").toLowerCase();
		if (os.startsWith("win")) {
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
	public static void cleanObj(Object... objs) {
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
	public static boolean isNumeric(String str) {
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
		if (theString == null)
			return null;
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

}
