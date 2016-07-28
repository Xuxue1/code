package com.xuxue.code.http;


import com.xuxue.code.scala.http.ConfigClient;
import org.apache.http.HttpRequest;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLContext;
import java.io.IOException;

/**
 * Created by HanHan on 2016/7/21.
 */
public class ConfiguredHttpClient {

    private final ConfigClient configclient=new ConfigClient();

    public final CloseableHttpClient client=configclient.client();

    public final BasicCredentialsProvider credsProvider=configclient.credsProvider();

    public final HttpMessageParserFactory messageParserFactory=configclient.messageParserFactory();

    public final HttpMessageWriterFactory<HttpRequest> requestWriterFactory=configclient.requestWriterFactory();

    public final HttpConnectionFactory<HttpRoute,ManagedHttpClientConnection> httpConnectionFactory=configclient.httpConnectionFactory();

    public final SSLContext sslContext=configclient.sslContex();

    public final DnsResolver dnsResolver=configclient.dnsResolver();

    public final Registry<ConnectionSocketFactory> socketFactoryRegisry=configclient.socketFactoryRegisry();

    public final PoolingHttpClientConnectionManager connManager=configclient.connManager();

    public final SocketConfig socketConfig=configclient.socketConfig();

    public final CookieStore cookieStore=configclient.cookieStore();

    public CloseableHttpResponse execute(HttpUriRequest request)throws IOException{
        return client.execute(request);
    }

    public CloseableHttpResponse execute(HttpUriRequest request, HttpContext contex)throws IOException{
        return client.execute(request, contex);
    }
}
