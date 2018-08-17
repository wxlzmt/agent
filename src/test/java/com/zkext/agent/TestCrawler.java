package com.zkext.agent;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.zkext.agent.core.handler.ResponseHandler;
import com.zkext.agent.core.util.HttpClientUtil;

public class TestCrawler {

	public static void main(String[] args) throws Exception {
		TestCrawler tc = new TestCrawler();
		tc.testCraw();
	}
	
	@Test
	public void testCraw() {
		HttpClientUtil hcu = new HttpClientUtil();
		String url = "https://www.zgny.com.cn/ifm/consultation/3NyZxList1.shtml";
		String encoding = "UTF-8";
		hcu.doGet(url, null, encoding, null, new ResponseHandler() {

			@Override
			public boolean handle(Header[] requestHeaders, HttpResponse response) {

				int code = response.getStatusLine().getStatusCode();
				System.out.println("code=" + code);
				try {
					String ret = EntityUtils.toString(response.getEntity(), encoding);
					assertTrue(ret.length()>0);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;

			}
		});
	}
}
