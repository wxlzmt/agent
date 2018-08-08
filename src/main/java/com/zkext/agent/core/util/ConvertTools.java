package com.zkext.agent.core.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 数据转换工具
 *
 */
public class ConvertTools {

	/**
	 * 按优先级排序的有序集, 预尝试获取时间, 时间字符串格式位置.
	 */
	private static final String [] AVAIABLE_DATE_FORMAT_SRT_ARR = new String[]{
			"yyyy-MM-dd HH:mm:ss",
			"yyyy-MM-dd HH:mm",
			"yyyy-MM-dd",
			"yyyy/MM/dd",
			"yyyy年MM月dd日 HH:mm:ss",
			"yyyy年MM月dd日 HH:mm"
			};
	
	/**
	 * 尝试将 时间字符串 转换为日期, 如果失败,返回null
	 * @param dateStr
	 * @return 可能为null
	 */
	public static Date getDateFromString(String dateStr) {
		Date date = null;
		SimpleDateFormat sdf = null;
		if(StringUtils.isNotBlank(dateStr)) {
			for (int i = 0; i < AVAIABLE_DATE_FORMAT_SRT_ARR.length; i++) {
				sdf = new SimpleDateFormat(AVAIABLE_DATE_FORMAT_SRT_ARR[i]);
				try{
					date = sdf.parse(dateStr);
				}catch (Exception e) {
					continue;
				}
				break;
			}
		}
		return date;
	}
	
	/**
	 * 这是一个优化版的提取图片URL的方法
	 * 
	 * @param htmlCode
	 * @return
	 */
	public static List<String> getImageSrc(String htmlCode) {
	    List<String> imageSrcList = new ArrayList<String>();
	    String regular = "< *img ([^>]*)src *= *(\"[^\"]+\"|'[^']+')";
	    Pattern p = Pattern.compile(regular, Pattern.CASE_INSENSITIVE);
	    Matcher m = p.matcher(htmlCode);
	    String src = null;
	    while (m.find()) {
	        int start = m.start(2);// group0是整个匹配的字符串,获取到的url是group2
	        int end = m.end(2);
	        if ((end - start) > 1) {
	            src = htmlCode.substring(start + 1, end - 1);
	            imageSrcList.add(src);	            	
	        }
	    }
	    return imageSrcList;
	}
	
	/**
	 * 是否是绝对路径的url
	 * @param path 
	 * @return 当path的格式正确时,返回true,否则返回false
	 */
	public static boolean isAbsUrlPath(String path) {
		String regular = "^(http|https|ftp)://[^\\./]+\\.[^\\./]+.*$";
		Pattern p = Pattern.compile(regular, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(path);
		return m.find();
	}
	
	
	public static String readFromFile(String fileName) {
		String txt = null;
		File file = new File(fileName);
		try {
			txt = FileUtils.readFileToString(file, "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return txt;
	}
	
	/**
	 * 根据字符串值,将之转换为指定的数据类型的对象值
	 * @param val
	 * @param type
	 * @return
	 */
	public static Object genValFromString(String val, String type) {
		if ("int".equalsIgnoreCase(type) || "integer".equalsIgnoreCase(type)) {
			return genIntegerFromString(val);
		} else if ("boolean".equalsIgnoreCase(type) || "bool".equalsIgnoreCase(type)) {
			return genBooleanFromString(val);
		} else if ("long".equalsIgnoreCase(type)) {
			return genLongFromString(val);
		} else if ("double".equalsIgnoreCase(type)) {
			return genDoubleFromString(val);
		}
		return val;
	}

	public static Integer genIntegerFromString(String val) {
		Integer ret = null;
		if (StringUtils.isNotBlank(val)) {
			ret = Integer.valueOf(val);
		}
		return ret;
	}

	public static Boolean genBooleanFromInteger(Integer i) {
		Boolean ret = null;
		if (i != null) {
			if (i == 0) {
				ret = Boolean.FALSE;
			} else if (i == 1) {
				ret = Boolean.TRUE;
			}
		}
		return ret;
	}

	public static Boolean genBooleanFromString(String val) {
		Boolean ret = null;
		if (StringUtils.isNotBlank(val)) {
			String v = val.trim();
			if ("true".equalsIgnoreCase(v) || "1".equalsIgnoreCase(v)) {
				ret = Boolean.TRUE;
			} else if ("false".equalsIgnoreCase(v) || "0".equalsIgnoreCase(v)) {
				ret = Boolean.FALSE;
			}
		}
		return ret;
	}

	public static Long genLongFromString(String val) {
		Long ret = null;
		if (StringUtils.isNotBlank(val)) {
			ret = Long.valueOf(val);
		}
		return ret;
	}

	public static Double genDoubleFromString(String val) {
		Double ret = null;
		if (StringUtils.isNotBlank(val)) {
			ret = Double.valueOf(val);
		}
		return ret;
	}
	
	public static void main(String[] args) {
		
		String fileName = "D:\\test.txt";
		String txt = readFromFile(fileName);
		System.out.println(txt); 
		
		@SuppressWarnings("unused")
		List<String> list = getImageSrc(txt);
		
		
		System.out.println("--------------end------------");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sdf.format(getDateFromString("2017-12-06 10:07")));
		System.out.println(sdf.format(getDateFromString("2017-12-06 10:07:34")));
		System.out.println(sdf.format(getDateFromString("2017-12-06")));
	}
	
}
