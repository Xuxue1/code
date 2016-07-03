package com.xuxue.code.scala.tools

import java.io.ByteArrayInputStream
import java.nio.charset.Charset

import info.monitorenter.cpdetector.io._
import org.apache.log4j.Logger

import scala.util.{Failure, Try}

/**
  * Created by HanHan on 2016/7/3.
  */
private class CpdetectorEncoding {



}


object CpdetectorEncoding{

  val logger=Logger.getLogger(getClass)
  private val parsingDetector: ParsingDetector = new ParsingDetector(false)
  private val byteOrderMarkDetector: ByteOrderMarkDetector = new ByteOrderMarkDetector

  private lazy val detector={
    val detector = CodepageDetectorProxy.getInstance
    detector.add(byteOrderMarkDetector)
    println("Hello")
    detector.add(parsingDetector)
    detector.add(JChardetFacade.getInstance)
    detector.add(ASCIIDetector.getInstance)
    detector
  }

  def getEncoding(byteArray:Array[Byte],size:Int):Try[Charset]={
      val byteArrayIn=Try(new ByteArrayInputStream(byteArray,0,size))
      if(byteArrayIn.isFailure){
        logger.info("fail",byteArrayIn.failed.get)
        Failure(byteArrayIn.failed.get)
      }else{
        Try(detector.detectCodepage(byteArrayIn.get,size))
      }
  }

  def main(args: Array[String]) {
    println(getEncoding("HelloWord".getBytes,10))
    println(getEncoding("HelloWord".getBytes,5))
  }


}
