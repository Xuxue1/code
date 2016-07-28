package com.xuxue.code.scala.http

import javax.net.ssl.SSLContext

import org.apache.http.client.CookieStore
import org.apache.http.config.{MessageConstraints, Registry, RegistryBuilder, SocketConfig}
import org.apache.http.conn.routing.HttpRoute
import org.apache.http.conn.socket.{ConnectionSocketFactory, PlainConnectionSocketFactory}
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.conn.{DnsResolver, HttpConnectionFactory, ManagedHttpClientConnection}
import org.apache.http.impl.DefaultHttpResponseFactory
import org.apache.http.impl.client.{BasicCookieStore, BasicCredentialsProvider, CloseableHttpClient, HttpClients}
import org.apache.http.impl.conn._
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory
import org.apache.http.io.{HttpMessageParserFactory, HttpMessageWriterFactory, SessionInputBuffer}
import org.apache.http.message.{BasicHeader, BasicLineParser}
import org.apache.http.ssl.SSLContexts
import org.apache.http.util.CharArrayBuffer
import org.apache.http.{Header, HttpRequest, HttpResponse}

/**
  * Created by HanHan on 2016/7/18.
  */
class ConfigClient {

  val credsProvider:BasicCredentialsProvider=new BasicCredentialsProvider()

  val messageParserFactory:HttpMessageParserFactory[HttpResponse]=new DefaultHttpResponseParserFactory(){

  }

  val requestWriterFactory:HttpMessageWriterFactory[HttpRequest]=new DefaultHttpRequestWriterFactory();

  val httpConnectionFactory:HttpConnectionFactory[HttpRoute,ManagedHttpClientConnection]
  =new ManagedHttpClientConnectionFactory(requestWriterFactory,messageParserFactory)


  val sslContex:SSLContext=SSLContexts.createDefault()

  val dnsResolver:DnsResolver=new SystemDefaultDnsResolver()

  val socketFactoryRegisry:Registry[ConnectionSocketFactory]=RegistryBuilder.create[ConnectionSocketFactory]()
    .register("http", PlainConnectionSocketFactory.INSTANCE)
    .register("https", new SSLConnectionSocketFactory(sslContex))
    .build()

  val connManager:PoolingHttpClientConnectionManager=new PoolingHttpClientConnectionManager(
    socketFactoryRegisry,
    httpConnectionFactory,
    dnsResolver)

  val socketConfig:SocketConfig=SocketConfig.custom().setTcpNoDelay(true).build()

  connManager.setDefaultSocketConfig(socketConfig)

  val cookieStore:CookieStore=new BasicCookieStore()

  val client:CloseableHttpClient=
    HttpClients.custom()
      .setConnectionManager(connManager)
      .setDefaultCookieStore(cookieStore)
      .setDefaultCredentialsProvider(credsProvider)
      .build()
}
