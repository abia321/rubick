package com.sunlands.rubick.core;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;


/**
 * Created by baifan on 2017/11/13.
 */
public class HttpConnectUtil {

    /**
     *  httpclient get
     *  @param urlQuery
     * */
    public String getUrlReturnData(String urlQuery) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(urlQuery);
        HttpResponse response=client.execute(httpGet);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            return EntityUtils.toString(response.getEntity());
        } else {
            return "";
        }
    }
}
