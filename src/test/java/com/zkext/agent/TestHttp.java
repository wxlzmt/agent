package com.zkext.agent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.zkext.agent.core.handler.ResponseHandler;
import com.zkext.agent.core.util.HttpClientUtil;
import com.zkext.agent.dto.KVBean;


public class TestHttp {

    public static void main(String[] args) {
        HttpClientUtil hcu = new HttpClientUtil();

        String url = "https://www.zgny.com.cn/ifm/consultation/3NyZxList1.shtml";
        List<KVBean> params = new ArrayList<KVBean>();
        String encoding = "UTF-8";
        Map<String, String> headerMap = new HashMap<String, String>();
        hcu.doGet(url, params, encoding, headerMap, new ResponseHandler() {
            @Override
            public boolean handle(Header[] requestHeaders, HttpResponse response) {

                int code = response.getStatusLine().getStatusCode();
                System.out.println("code="+code);
                try {
                    String ret = EntityUtils.toString(response.getEntity(),encoding);
                    System.out.println(ret);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;

            }
        });
    }

}
