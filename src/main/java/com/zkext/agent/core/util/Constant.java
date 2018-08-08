package com.zkext.agent.core.util;

import java.io.File;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
/**
 * 
* @ClassName: Constant 
* @Description: 常量类
*
 */
public class Constant {
	
	/**
	 * 编码模式,无论是模板还是文件,全部通用UTF-8
	 */
	public static final String DEFAULT_ENCODING = "UTF-8";
	
	/**
	 * date默认格式
	 */
	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * webRoot路径(绝对)
	 */
	public static final String WEB_ROOT_PATH = PathHelper.getInstance().getWebRootPathAbs();

	/**
	 * webRoot/upload 文件上传路径 (绝对)
	 */
	public static final String UPLOAD_PATH = WEB_ROOT_PATH + File.separator + "upload";

	/**
	 * webRoot/WEB-INF 路径(绝对)
	 */
	public static final String WEB_INF_PATH = WEB_ROOT_PATH + File.separator + "WEB-INF";
	
	/**
	 * webRoot/WEB-INF/config 路径(绝对)
	 */
	public static final String WEB_CONFIG_PATH = WEB_INF_PATH + File.separator + "config";
	
	/**
	 * classses的路径,即:\WebRoot\WEB-INF\classes (绝对)
	 */
	public static final String ROOT_CLASSPATH = PathKit.getRootClassPath();

	
	/**
	 * config.properties配置文件路径
	 */
	public static final String CONFIG_FILE_PATH = ROOT_CLASSPATH + File.separator + "config.properties";

	/**
	 * 当前是否是 开发者模式
	 */
	public static final boolean DEV_MODE = PropKit.use(new File(CONFIG_FILE_PATH)).getBoolean("devMode", false).booleanValue();

	//public static final String WEB_CONTEXT_PATH = PropKit.use(new File(CONFIG_FILE_PATH)).get("webContextPath","/");
	
	/**
	 * 是
	 */
	public static final String YES = "1";
	
	/**
	 *否 
	 */
	public static final String NO = "0";
	
	/**
	 * 是, int型
	 */
	public static final int YES_INT = 1;
	
	/**
	 * 否, int型
	 */
	public static final int NO_INT = 0;
	
	
	/**
	 * 当发生401错误时跳转的URL
	 */
	public static final String URL_401 = "/WEB-INF/error/401.html";
	/**
	 * 当发生403错误时跳转的URL
	 */
	public static final String URL_403 = "/WEB-INF/error/403.html";
	/**
	 * 当发生404错误时跳转的URL
	 */
	public static final String URL_404 = "/WEB-INF/error/404.html";
	/**
	 * 当发生500错误时跳转的URL
	 */
	public static final String URL_500 = "/WEB-INF/error/500.html";
	
	/**
	 * 400错误时跳转的URL
	 */
	public static final String URL_400 = "/WEB-INF/error/400.html";
	
	/**
	 * 405错误时跳转的URL
	 */
	public static final String URL_405 = "/WEB-INF/error/405.html";
	
	/**
	 * 当前操作系统是否是linux
	 * @return
	 */
	public static boolean currentOsIsLinux() {
		if("/".equals(File.separator)) {
			System.out.println("当前操作系统是linux");
			return true;
		}else {
			System.out.println("当前操作系统不不不不不不不不不不不不不不不不不不不不是linux");
			return false;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(CONFIG_FILE_PATH);
	}
}
