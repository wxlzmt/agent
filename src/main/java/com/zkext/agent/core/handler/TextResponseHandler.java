package com.zkext.agent.core.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.zkext.agent.dto.KVBean;
import com.zkext.agent.dto.Report;

public class TextResponseHandler implements ResponseHandler {

	private static Logger logger = Logger.getLogger(TextResponseHandler.class);

	private Report report = null;

	public TextResponseHandler() {
		this.report = new Report();
	}

	@Override
	public boolean handle(Header[] requestHeaders,HttpResponse response) {
		this.report.setStatusCode(response.getStatusLine().getStatusCode());
		if (requestHeaders != null) {
			List<KVBean> list = new ArrayList<KVBean>();
			for (int i = 0; i < requestHeaders.length; i++) {
				Header header = requestHeaders[i];
				list.add(new KVBean(header.getName(), header.getValue()));
			}
			this.report.setRequestHeaders(list);
		}
		Header[] responseHeaders = response.getAllHeaders();
		if (responseHeaders != null) {
			List<KVBean> list = new ArrayList<KVBean>();
			for (int i = 0; i < responseHeaders.length; i++) {
				Header header = responseHeaders[i];
				list.add(new KVBean(header.getName(), header.getValue()));
			}
			this.report.setResponseHeaders(list);
		}
		HttpEntity entity = response.getEntity();
		try {
			String responseText = EntityUtils.toString(entity, this.report.getResponseEncoding());
			this.report.setResponseText(responseText);
			return true;
		} catch (ParseException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		return false;
	}


	public Report getReport() {
		return this.report;
	}

}
