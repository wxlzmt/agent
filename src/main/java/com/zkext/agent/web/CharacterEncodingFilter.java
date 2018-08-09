package com.zkext.agent.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * GET/POST请求 参数编码问题处理
 */
public class CharacterEncodingFilter implements javax.servlet.Filter {

	private String defaultEncoding = "UTF-8";
	/**释义:是否过滤POST请求的参数.*/
	private boolean enableFilterPostMethod = true;
	/**释义:是否过滤GET请求的参数.*/
	private boolean enableFilterGetMethod = true;

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		// 处理请求乱码
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String method = httpServletRequest.getMethod();
		if ((method.equalsIgnoreCase("POST") && enableFilterPostMethod) || (method.equalsIgnoreCase("GET") && enableFilterGetMethod)) {
			HttpServletRequest myRequest = new MyRequest(httpServletRequest);
			filterChain.doFilter(myRequest, response);
		} else {
			filterChain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig arg) {
		String _defaultEncoding = arg.getInitParameter("defaultEncoding");
		String _enableFilterPostMethod = arg.getInitParameter("enableFilterPostMethod");
		String _enableFilterGetMethod = arg.getInitParameter("enableFilterGetMethod");
		if(_defaultEncoding!=null && _defaultEncoding.length()>0) {
			this.defaultEncoding = _defaultEncoding;
		}
		if(_enableFilterPostMethod!=null) {
			if("true".equalsIgnoreCase(_enableFilterPostMethod)) {
				this.enableFilterPostMethod = true;
			}else if("false".equalsIgnoreCase(_enableFilterPostMethod)) {
				this.enableFilterPostMethod = false;
			}
		}
		if(_enableFilterGetMethod!=null) {
			if("true".equalsIgnoreCase(_enableFilterGetMethod)) {
				this.enableFilterGetMethod = true;
			}else if("false".equalsIgnoreCase(_enableFilterGetMethod)) {
				this.enableFilterGetMethod = false;
			}
		}
	}

	// 自定义request对象
	class MyRequest extends HttpServletRequestWrapper {

		private HttpServletRequest request;

		private boolean hasEncode = false;

		/**
		 * 多次调用getParameterMap()会影响性能
		 */
		@SuppressWarnings("rawtypes")
		private Map cachedParameterMap = null;

		public MyRequest(HttpServletRequest request) {
			super(request);
			this.request = request;
		}

		// 对需要增强的方法 进行覆盖
		@SuppressWarnings("rawtypes")
		@Override
		public Map getParameterMap() {
			if (cachedParameterMap != null) {
				return this.cachedParameterMap;
			}
			// 先获得请求方式
			String method = request.getMethod();
			if (method.equalsIgnoreCase("POST")) { // post方式请求
				try {
					request.setCharacterEncoding(defaultEncoding);
					this.cachedParameterMap = request.getParameterMap();
					return this.cachedParameterMap;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else if (method.equalsIgnoreCase("GET")) { // get方式请求
				@SuppressWarnings("unchecked")
				Map<String, String[]> parameterMap = request.getParameterMap();
				if (!hasEncode) {
					for (String parameterName : parameterMap.keySet()) {
						String[] values = parameterMap.get(parameterName);
						if (values != null) {
							for (int i = 0; i < values.length; i++) {
								try {
									values[i] = new String(values[i].getBytes("ISO-8859-1"), defaultEncoding);
								} catch (UnsupportedEncodingException e) {
									e.printStackTrace();
								}
							}
						}
					}
					hasEncode = true;
				}
				this.cachedParameterMap = parameterMap;
				return cachedParameterMap;
			}
			return super.getParameterMap();
		}

		@Override
		public String getParameter(String name) {
			@SuppressWarnings("unchecked")
			Map<String, String[]> parameterMap = getParameterMap();
			String[] values = parameterMap.get(name);
			if (values == null) {
				return null;
			}
			return values[0];
		}

		@Override
		public String[] getParameterValues(String name) {
			@SuppressWarnings("unchecked")
			Map<String, String[]> parameterMap = getParameterMap();
			String[] values = parameterMap.get(name);
			return values;
		}
	}

	public String getDefaultEncoding() {
		return defaultEncoding;
	}

	public void setDefaultEncoding(String defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
	}

	public boolean isEnableFilterPostMethod() {
		return enableFilterPostMethod;
	}

	public void setEnableFilterPostMethod(boolean enableFilterPostMethod) {
		this.enableFilterPostMethod = enableFilterPostMethod;
	}

	public boolean isEnableFilterGetMethod() {
		return enableFilterGetMethod;
	}

	public void setEnableFilterGetMethod(boolean enableFilterGetMethod) {
		this.enableFilterGetMethod = enableFilterGetMethod;
	}

}
