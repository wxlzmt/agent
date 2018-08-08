package com.zkext.agent.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Report implements Serializable {

	private static final long serialVersionUID = 1L;

	private String requestURL = null;

	private String requestMethod = null;

	private String requestParamEncoding = null;

	private Integer statusCode = null;

	private String remoteAddress = null;

	private List<KVBean> requestHeaders = new ArrayList<KVBean>();

	private List<KVBean> requestParams = new ArrayList<KVBean>();

	private String requestStringEntity = null;

	private List<KVBean> responseHeaders = new ArrayList<KVBean>();

	private String responseText = null;

	private String responseEncoding = null;

	public Report() {
		// 由框架构造实体的时候可能需要无参构造方法
	}

	public String getRequestURL() {
		return requestURL;
	}

	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	public List<KVBean> getRequestHeaders() {
		return requestHeaders;
	}

	public void setRequestHeaders(List<KVBean> requestHeaders) {
		this.requestHeaders = requestHeaders;
	}

	public List<KVBean> getResponseHeaders() {
		return responseHeaders;
	}

	public void setResponseHeaders(List<KVBean> responseHeaders) {
		this.responseHeaders = responseHeaders;
	}

	public String getResponseText() {
		return responseText;
	}

	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}

	public String getResponseEncoding() {
		return responseEncoding;
	}

	public void setResponseEncoding(String responseEncoding) {
		this.responseEncoding = responseEncoding;
	}

	public List<KVBean> getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(List<KVBean> requestParams) {
		this.requestParams = requestParams;
	}

	public String getRequestStringEntity() {
		return requestStringEntity;
	}

	public void setRequestStringEntity(String requestStringEntity) {
		this.requestStringEntity = requestStringEntity;
	}

	public String getRequestParamEncoding() {
		return requestParamEncoding;
	}

	public void setRequestParamEncoding(String requestParamEncoding) {
		this.requestParamEncoding = requestParamEncoding;
	}

}
