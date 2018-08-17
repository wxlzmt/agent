package com.zkext.agent.core.util;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

/**
 * 字符串解析工具类
 */
public class StringAnalysisUtil {

	private static Map<String, String> replaceMap = new HashMap<String, String>();
	static {
		replaceMap.put("lt", "<");
		replaceMap.put("gt", ">");
		replaceMap.put("nbsp", " ");
		replaceMap.put("amp", "&");
		replaceMap.put("quot", "\"");
		replaceMap.put("ldquo", "“");
		replaceMap.put("rdquo", "”");
		replaceMap.put("middot", "·");
		replaceMap.put("mdash", "—");
	};

	/**
	 * 根据正则表达式字符串直接从源字符串中提取
	 * @param source 源字符串
	 * @param regStr 正则表达式字符串 
	 * @param isTrimToNotNullAndResolveHtmlCode 是否将返回结果强制转为非null并且过滤掉HTML相关的字符
	 * @param endIndex 要求传入一个长度是1的int型数组(如果为null或者长度不等于1,则忽略处理),方法执行完成后,会将
	 *            "匹配的字符串"的结束位置在源字符串的位置的索引放进去.此参数的目的是为了满足按顺序截取一部分数据之后,继续往下查找截取.
	 * @return ret 可能为null
	 */
	public static String extractByReg(String source, String regStr, boolean isTrimToNotNullAndResolveHtmlCode, int[] endIndex) {
		Pattern pat = Pattern.compile(regStr, Pattern.UNICODE_CASE);
		return extractByReg(source,pat,isTrimToNotNullAndResolveHtmlCode,endIndex);
	}
	
	/**
	 * 根据正则表达式字符串直接从源字符串中提取
	 * @param source 源字符串
	 * @param regStr 正则表达式字符串 
	 * @return ret 可能为null
	 */
	public static String extractByReg(String source, String regStr) {
		return extractByReg(source,regStr,false,null);
	}
	
	/**
	 * 根据正则表达式字符串直接从源字符串中提取
	 * @param source 源字符串
	 * @param reg Pattern对象实例
	 * @return ret 可能为null
	 */
	public static String extractByReg(String source, Pattern reg) {
		return extractByReg(source, reg,false,null);
	}
	
	/**
	 * 根据正则表达式字符串直接从源字符串中提取
	 * @param source 源字符串
	 * @param reg Pattern对象实例
	 * @param isTrimToNotNullAndResolveHtmlCode 是否将返回结果强制转为非null并且过滤掉HTML相关的字符
	 * @param endIndex 要求传入一个长度是1的int型数组(如果为null或者长度不等于1,则忽略处理),方法执行完成后,会将
	 *            "匹配的字符串"的结束位置在源字符串的位置的索引放进去.此参数的目的是为了满足按顺序截取一部分数据之后,继续往下查找截取.
	 * @return ret
	 */
	public static String extractByReg(String source, Pattern reg, boolean isTrimToNotNullAndResolveHtmlCode, int[] endIndex) {
		String ret = null;
		if (source != null) {
			ret = "";
			Matcher mat = reg.matcher(source);
			if (mat.find()) {
				ret = source.substring(mat.start(), mat.end());
				if (endIndex != null && endIndex.length == 1) {
					endIndex[0] = mat.end();
				}
			}
		}
		if (isTrimToNotNullAndResolveHtmlCode) {
			if (ret == null) {
				ret = "";
			} else {
				ret = trimToNotNullAndResolveHtmlCode(ret);
			}
		}
		return ret;
	}

	/**
	 * 两头截取的方式进行提取
	 * @param source 源字符串
	 * @param prefix 正则表达式,起始字符串
	 * @param suffix 正则表达式,结束字符串
	 * @param isTrimToNotNullAndResolveHtmlCode 是否将返回结果强制转为非null并且过滤掉HTML相关的字符
	 * @param endIndex 要求传入一个长度是1的int型数组(如果为null或者长度不等于1,则忽略处理),方法执行完成后,会将
	 *            "结束字符串"在源字符串的结束位置的索引放进去.此参数的目的是为了满足按顺序截取一部分数据之后,继续往下查找截取.
	 * @return 可能为null
	 */
	public static String extractBetween(String source, String prefix, String suffix, boolean isTrimToNotNullAndResolveHtmlCode, int[] endIndex) {
		String ret = null;
		int start = -1, end = -1;
		if (source != null) {
			Matcher mat1 = Pattern.compile(prefix, Pattern.UNICODE_CASE).matcher(source);
			if (mat1.find()) {
				start = mat1.end();
				Matcher mat2 = Pattern.compile(suffix, Pattern.UNICODE_CASE).matcher(source.substring(mat1.end()));
				if (mat2.find()) {
					end = start + mat2.start();
					if (endIndex != null && endIndex.length == 1) {
						endIndex[0] = start + mat2.end();
					}
				}
			}
			if (start >= 0 && end >= 0 && start <= end) {
				ret = source.substring(start, end);
			}
		}
		if (isTrimToNotNullAndResolveHtmlCode) {
			if (ret == null) {
				ret = "";
			} else {
				ret = trimToNotNullAndResolveHtmlCode(ret);
			}
		}
		return ret;
	}

	/**
	 * 两头截取的方式进行提取
	 * @param source 源字符串
	 * @param prefix 正则表达式,起始字符串
	 * @param suffix 正则表达式,结束字符串
	 * @return
	 */
	public static String extractBetween(String source, String prefix, String suffix) {
		return extractBetween(source, prefix, suffix, false, null);
	}

	/**
	 * 去掉字符串两头空白符,如果源字符串是null,则返回空字符串;
	 * @param source 源字符串
	 * @return 如果源字符串是null,则返回空字符串;
	 */
	public static String trimToNotNullAndResolveHtmlCode(String source) {
		String ret = "";
		if (source != null) {
			ret = source.trim();
			if (ret.length() > 0) {
				for (String key : replaceMap.keySet()) {
					ret = ret.replaceAll(key, replaceMap.get(key));
				}
				ret = ret.trim();
			}
		}
		return ret;
	}

	public static void main(String[] args) throws Exception {
		String source = IOUtils.toString(new FileInputStream("D:\\q.txt"), "UTF-8");
		String result = StringAnalysisUtil.extractByReg(source, "<td[^>]*>[^<]*</td>", false, null);
		System.out.println(result);
		System.out.println("end!");

	}

}
