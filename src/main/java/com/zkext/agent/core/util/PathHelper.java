package com.zkext.agent.core.util;

import java.io.File;

import com.jfinal.kit.PathKit;

/**
 * 解决找绝对路径找不到的问题,尤其是在maven环境中的文件路径问题;
 *
 */
public class PathHelper {
	
	private static PathHelper singleton = null;
	
	
	
	/**
	 * classes目录;
	 * 对应maven项目的目录\target\classes
	 */
	private static String CLASS_PATH_ABS = null;
	
	private static String WEB_ROOT_PATH = null;
	
	/**
	 * classes目录;
	 * 对应maven项目的目录\target\classes
	 */
	public String getClassPathAbs() {
		return CLASS_PATH_ABS;
	}
	
	/**
	 * 获取webroot目录, 该目录是编译后的target目录下的项目目录,也可能是打包发布之后的war包里面的路径,总之是绝对路径.
	 * @return
	 */
	public String getWebRootPathAbs() {
		return WEB_ROOT_PATH;
	}
	
	private PathHelper() {
		CLASS_PATH_ABS = new File(PathHelper.class.getClassLoader().getResource("").getPath()).getAbsolutePath();
		
		//tmp 可能是 /项目名
		String tmp = PathKit.getWebRootPath();
		File fileTarget = new File(tmp+File.separator+"target");
		if(fileTarget.exists()) {
			String projectName = new File(tmp).getName();
			WEB_ROOT_PATH = new File(tmp+File.separator+"target"+File.separator+projectName).getAbsolutePath();
		}else {
			WEB_ROOT_PATH = tmp;
		}
		
		System.out.println("CLASS_PATH_ABS="+CLASS_PATH_ABS);
		System.out.println("WEB_ROOT_PATH="+WEB_ROOT_PATH);
	}
	/**
	 * 单例模式
	 */
	public static PathHelper getInstance() {
		if (singleton == null) {
	        synchronized (PathHelper.class) {
	            if (singleton == null) {
	            	singleton = new PathHelper();
	            }
	        }
	    }
	    return singleton;
	}

}
