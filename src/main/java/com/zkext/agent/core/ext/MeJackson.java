package com.zkext.agent.core.ext;

import com.jfinal.json.Jackson;
import com.zkext.agent.core.util.Constant;

/**
 * 解决json反序列化时Date类型出错的问题;
 */
public class MeJackson extends Jackson {

	public static MeJackson me = getJson();
	
	public MeJackson() {
		super();
		this.setDatePattern(Constant.DEFAULT_DATE_PATTERN);
	}
	
	public static MeJackson getJson() {
		return new MeJackson();
	}
	
	
}
