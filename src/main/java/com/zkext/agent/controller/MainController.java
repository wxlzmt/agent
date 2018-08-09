package com.zkext.agent.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.core.Controller;
import com.zkext.agent.core.util.HttpClientUtil;
import com.zkext.agent.core.util.ResponseHandler;
import com.zkext.agent.core.util.StreamResponseHandler;
import com.zkext.agent.core.util.TextResponseHandler;
import com.zkext.agent.dto.KVBean;
import com.zkext.agent.dto.Report;
import com.zkext.agent.dto.Response;

public class MainController extends Controller {

	public static final String PREFIX_PARAM = "param.";

	public static final String PREFIX_HEADER = "header.";
	
	public static final String ENCTYPE_FORM_URLENCODED = "x-www-form-urlencoded";
	
	public static final String ENCTYPE_RAW = "raw";
	
	public static final String RESPONSE_TYPE_TEXT = "text";
	
	public static final String RESPONSE_TYPE_BINARY = "binary";

	/**
	 * 重定向至主页
	 */
	public void index() {
		render("/WEB-INF/index.html");
	}

	public void q() {
		HttpServletRequest request = getRequest();
		String url = request.getParameter("url");
		if (url == null || !isAbsUrlPath(url)) {
			Response rep = new Response(false, "参数:url 要求必填而且必须合法!");
			renderJson(rep);
			return;
		}
		String method = getPara("method","GET").toUpperCase();
		if(!"GET".equalsIgnoreCase(method) && !"POST".equalsIgnoreCase(method)) {
			Response rep = new Response(false, "参数:method 取值只能是GET或POST; 如果不传,默认GET");
			renderJson(rep);
			return;
		}
		String paramEncoding = getPara("paramEncoding","UTF-8");
		String responseEncoding = getPara("responseEncoding","UTF-8");
		String responseType = getPara("responseType",RESPONSE_TYPE_TEXT);
		if(!RESPONSE_TYPE_TEXT.equalsIgnoreCase(responseType) && !RESPONSE_TYPE_BINARY.equalsIgnoreCase(responseType)) {
			Response rep = new Response(false, "参数:responseType 取值只能是 "+RESPONSE_TYPE_TEXT+" 或 "+RESPONSE_TYPE_BINARY+" ; 如果不传,默认:"+RESPONSE_TYPE_TEXT);
			renderJson(rep);
			return;
		}
		List<String> paramNameList = new ArrayList<String>();
		Enumeration<String> enumNames = getParaNames();
		while (enumNames.hasMoreElements()) {
			paramNameList.add(enumNames.nextElement());
		}
		List<KVBean> params = genRequestParams(request, paramNameList);
		Map<String, String> headers = genRequestHeaders(request, paramNameList);
		HttpClientUtil hcUtil = new HttpClientUtil();
		ResponseHandler handler = null;
		if(RESPONSE_TYPE_BINARY.equalsIgnoreCase(responseType)) {
			handler = new StreamResponseHandler();
		}else {
			handler = new TextResponseHandler();
		}
		Report report = handler.getReport();
		report.setRequestURL(url);
		report.setRequestMethod(method);
		report.setRemoteAddress(request.getRemoteAddr());
		report.setResponseEncoding(responseEncoding);
		report.setRequestParamEncoding(paramEncoding);
		report.setRequestParams(params);
		boolean result = false;
		if("GET".equalsIgnoreCase(method)) {
			result = hcUtil.doGet(url, params,paramEncoding, headers, handler);
		}else {
			String enctype = getPara("enctype",ENCTYPE_FORM_URLENCODED);
			if(ENCTYPE_FORM_URLENCODED.equals(enctype)) {
				result = hcUtil.doPost(url, params, paramEncoding, headers, handler);
			}else if(ENCTYPE_RAW.equals(enctype)) {
				String stringEntity = getPara(PREFIX_PARAM+"raw");
				report.setRequestStringEntity(stringEntity);
				result = hcUtil.doPost(url, stringEntity, paramEncoding, headers, handler);
			}else {
				Response rep = new Response(false, "当method=POST时,参数:enctype 有效,取值为 : "+ENCTYPE_FORM_URLENCODED+" 或 "+ENCTYPE_RAW+" ; 默认 : "+ENCTYPE_FORM_URLENCODED);
				renderJson(rep);
				return;
			}
		}
		Response rep = new Response(result,handler.getReport());
		renderJson(rep);
	}

	private List<KVBean> genRequestParams(HttpServletRequest request, List<String> paramNameList) {
		List<KVBean> list = new ArrayList<KVBean>();
		if (paramNameList != null && !paramNameList.isEmpty()) {
			for (String name : paramNameList) {
				if (name.startsWith(PREFIX_PARAM)) {
					// 真实的key
					String key = name.substring(PREFIX_PARAM.length());
					if (key.length() > 0) {
						String[] values = request.getParameterValues(name);
						if (values != null && values.length > 0) {
							for (int i = 0; i < values.length; i++) {
								list.add(new KVBean(key, values[i]));
							}
						}
					}
				}
			}
		}
		return list;
	}

	private Map<String, String> genRequestHeaders(HttpServletRequest request, List<String> paramNameList) {
		Map<String, String> map = new HashMap<String, String>();
		if (paramNameList != null && !paramNameList.isEmpty()) {
			for (String name : paramNameList) {
				if (name.startsWith(PREFIX_HEADER)) {
					// 真实的key
					String key = name.substring(PREFIX_HEADER.length());
					if (key.length() > 0) {
						String value = request.getParameter(name);
						map.put(key, value);
					}
				}
			}
		}
		return map;
	}
	
	/**
	 * 是否是绝对路径的url
	 * @param path 
	 * @return 当path的格式正确时,返回true,否则返回false
	 */
	private boolean isAbsUrlPath(String path) {
		String regular = "^(http|https)://[^\\./]+\\.[^\\./]+.*$";
		Pattern p = Pattern.compile(regular, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(path);
		return m.find();
	}

}
