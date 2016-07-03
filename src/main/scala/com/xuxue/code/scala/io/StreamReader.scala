package com.xuxue.code.scala.io

import java.io.{ByteArrayOutputStream, IOException, InputStream}

import com.xuxue.code.scala.tools.CpdetectorEncoding
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import scala.util.Try


/**
  * Created by HanHan on 2016/7/3.
  */
object StreamReader {

  def toString(in:InputStream):Try[String]={
      Try{
        val data=toBytes(in).get
        val en=CpdetectorEncoding.getEncoding(data,data.length).get
        println(en)
        new String(data,en)
      }
  }

  def toBytes(in:InputStream):Try[Array[Byte]]={
    Try {
      val out=new ByteArrayOutputStream();
      val temp=new Array[Byte](128);
      def read():Int={
         val read=in.read(temp,0,128);
          if(read!= -1)
            out.write(temp,0,read)
         read
      }
      while(read()!= -1){}
        out.toByteArray
    }
  }

  def toDocument(in:InputStream,url:String):Try[Document]={
    Try{
      val content=toString(in).get
      Jsoup.parse(content,url)
    }
  }

  def main(args: Array[String]) {
    val client=HttpClients.createDefault()
    val a:HttpGet=new HttpGet("http://www.baidu.com")
    val res=client.execute(a)
    println(toDocument(res.getEntity.getContent,"http://www.baidu.com").get)
    println("许继强")
  }

}
