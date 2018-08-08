package com.zkext.agent.core.util;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

public interface ResponseHandler {

	/**
	 * 处理响应的handler
	 * @param requestHeaders 请求头
	 * @param response 响应
	 * @return
	 */
	public boolean handle(Header[] requestHeaders,HttpResponse response);

}