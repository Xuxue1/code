package com.xuxue.code.http;


import com.xuxue.code.io.StreamRead;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by HanHan on 2016/7/21.
 */
public class ConfiguredHttpClientTest {
    Logger logger=Logger.getLogger(getClass());
    @Test
    public void test()throws IOException {
        ConfiguredHttpClient client=new ConfiguredHttpClient();
        HttpGet get=new HttpGet("http://www.baidu.com");
        CloseableHttpResponse res=client.execute(get);
        System.out.println(StreamRead.toString(res.getEntity().getContent()));
    }
}
