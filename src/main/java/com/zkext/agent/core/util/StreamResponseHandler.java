package com.zkext.agent.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;

import com.zkext.agent.dto.KVBean;
import com.zkext.agent.dto.Report;

public class StreamResponseHandler implements ResponseHandler {

	private static Logger logger = Logger.getLogger(StreamResponseHandler.class);

	private Report report = null;

	public StreamResponseHandler() {
		this.report = new Report();
	}

	@Override
	public boolean handle(Header[] requestHeaders, HttpResponse response) {
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
			InputStream is = entity.getContent();
			File tempFile = File.createTempFile("agent", ".tmp");
			FileOutputStream fos = new FileOutputStream(tempFile);
			int len = -1;
			byte [] arr = new byte[1024];
			while((len=is.read(arr))>0) {
				fos.write(arr, 0, len);
			}
			fos.flush();
			fos.close();
			is.close();
			
			FileInputStream fis = new FileInputStream(tempFile);
			byte [] buffer = new byte[Long.valueOf(tempFile.length()).intValue()];
			IOUtils.read(fis, buffer);
			fis.close();
			
			//buffer 转 base64
			String txt = CodingTool.base64Encode(buffer);
			this.report.setResponseText(txt);

			tempFile.delete();
			tempFile.deleteOnExit();
			return true;
		} catch (IOException e) {
			logger.error(e);
		}
		return false;
	}

	@Override
	public Report getReport() {
		return this.report;
	}
}
