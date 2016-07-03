package com.xuxue.code.scala.test

import java.net.URL

import scala.util.Try

/**
  * Created by HanHan on 2016/7/3.
  */

object Test{

  def getURL(string:String):Try[URL]={
    Try(new URL(string))
  }

  def main(args: Array[String]) {

    try{
      println(getURL("hello").get)
    }catch {
      case ex:Exception => println("Exception throw");
    }

  }

}
