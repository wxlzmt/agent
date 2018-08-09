package com.zkext.agent.core.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 编码/解码工具,<strong>使用JDK1.8相关API;<br/>
 * (java.util.Base64只存在于jdk1.8以及以上版本)</strong>
 */
public class CodingTool {

	public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	/**
	 * 将字节数组 以base64方式编码;<br/>
	 * <strong>(输入字节数组->base64字节数组->String字符串),<br/>
	 * 在 "base64字节数组->String字符串" 的过程中,无论使用UTF-8还是GBK,得到的字符串都是一模一样的; </strong>
	 * @param buffer
	 * @return 字符串
	 */
	public static String base64Encode(byte[] buffer) {
		String ret = null;
		if (buffer != null) {
			// 已经测试确认长度==0的buffer编码之后是空字符串(非null);
			ret = "";
			if (buffer.length > 0) {
				Base64.Encoder encoder = Base64.getEncoder();
				ret = new String(encoder.encode(buffer), DEFAULT_CHARSET);
			}
		}
		return ret;
	}

	/**
	 * 将字符串还原为二进制,<strong>字符串以UTF-8转为字节数组,然后以base64方式解码</strong>
	 * @param str
	 * @return
	 */
	public static byte[] base64Decode(String str) {
		byte[] ret = null;
		if (str != null) {
			if (str.length() > 0) {
				Base64.Decoder decoder = Base64.getDecoder();
				ret = decoder.decode(str.getBytes(DEFAULT_CHARSET));
			} else {
				ret = new byte[0];
			}
		}
		return ret;
	}

}
