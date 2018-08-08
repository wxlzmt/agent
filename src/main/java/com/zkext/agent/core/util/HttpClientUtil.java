package com.zkext.agent.core.util;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.zkext.agent.dto.KVBean;

/**
 * 执行HTTP GET / POST 请求
 */
public class HttpClientUtil {

	private static Logger logger = Logger.getLogger(HttpClientUtil.class);

	/**
	 * 执行 HTTP GET请求
	 * @param url
	 * @param params 请求参数,可以为null
	 * @param encoding 参数编码 . 默认UTF-8
	 * @param headerMap 请求头,可以为null
	 * @param handler 处理http响应数据的接口
	 * @return
	 */
	public boolean doGet(String url, List<KVBean> params, String encoding, Map<String, String> headerMap, ResponseHandler handler) {
		// 创建Httpclient对象
		CloseableHttpClient httpclient = null;
		httpclient = HttpClients.custom().build();
		CloseableHttpResponse response = null;
		String enc1 = (encoding != null && encoding.length() > 0) ? encoding : "UTF-8";
		try {
			// 创建uri
			URIBuilder builder = new URIBuilder(url);
			builder.setCharset(Charset.forName(enc1));
			if (params != null && !params.isEmpty()) {
				for (KVBean bean : params) {
					builder.addParameter(bean.getKey(), bean.getVal());
				}
			}
			URI uri = builder.build();
			// 创建http GET请求
			HttpGet httpGet = new HttpGet(uri);
			Map<String, String> defaultHeaderMap = genDefaultHeaders();
			if (defaultHeaderMap != null && !defaultHeaderMap.isEmpty()) {
				for (String key : defaultHeaderMap.keySet()) {
					httpGet.setHeader(key, defaultHeaderMap.get(key));
				}
			}
			if (headerMap != null && !headerMap.isEmpty()) {
				for (String key : headerMap.keySet()) {
					httpGet.setHeader(key, headerMap.get(key));
				}
			}
			// 执行请求
			response = httpclient.execute(httpGet);
			if (response != null) {
				return handler.handle(httpGet.getAllHeaders(), response);
			} else {
				logger.error("HTTP GET response is null !");
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpclient.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}
		return false;
	}

	public boolean doPost(String url, String stringEntity, String encoding, Map<String, String> headerMap, ResponseHandler handler) {
		return this.doPost(url, null, stringEntity, encoding, headerMap, handler);
	}

	public boolean doPost(String url, List<KVBean> params, String encoding, Map<String, String> headerMap, ResponseHandler handler) {
		return this.doPost(url, params, null, encoding, headerMap, handler);
	}

	/**
	 * 执行POST请求
	 * @param url
	 * @param params 请求参数 , 将会以x-www-form-urlencoded 形式编码
	 * @param stringEntity http post 的body 是raw类型时,用到此参数
	 * @param encoding 针对param进行模拟表单时的编码,可以为空,默认:UTF-8
	 * @param headerMap headerMap
	 * @param handler 处理http响应数据的接口
	 * @return
	 */
	private boolean doPost(String url, List<KVBean> params, String stringEntity, String encoding, Map<String, String> headerMap, ResponseHandler handler) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = null;
		httpClient = HttpClients.custom().build();
		CloseableHttpResponse response = null;
		List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		HttpEntity entity = null;
		String enc1 = (encoding != null && encoding.length() > 0) ? encoding : "UTF-8";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			Map<String, String> defaultHeaderMap = genDefaultHeaders();
			if (defaultHeaderMap != null && !defaultHeaderMap.isEmpty()) {
				for (String key : defaultHeaderMap.keySet()) {
					httpPost.setHeader(key, defaultHeaderMap.get(key));
				}
			}
			// 创建参数列表
			if (params != null && !params.isEmpty()) {
				for (KVBean bean : params) {
					paramList.add(new BasicNameValuePair(bean.getKey(), bean.getVal()));
				}
				// 模拟表单
				entity = new UrlEncodedFormEntity(paramList, enc1);
				httpPost.setEntity(entity);
				httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
			} else if (stringEntity != null && stringEntity.length() > 0) {
				entity = new StringEntity(stringEntity, enc1);
				httpPost.setEntity(entity);
				httpPost.setHeader("Content-type", "text/plain");
			}
			if (headerMap != null && !headerMap.isEmpty()) {
				for (String key : headerMap.keySet()) {
					httpPost.setHeader(key, headerMap.get(key));
				}
			}
			// 执行http请求
			response = httpClient.execute(httpPost);
			if (response != null) {
				return handler.handle(httpPost.getAllHeaders(), response);
			} else {
				logger.error("HTTP POST response is null !");
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}
		return false;
	}

	private Map<String, String> genDefaultHeaders() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
		return map;
	}

}
