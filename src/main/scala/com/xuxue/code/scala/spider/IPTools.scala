package com.xuxue.code.scala.spider

/**
  * Created by HanHan on 2016/7/3.
  */
object IPTools {

  def getIPRange(min:String,max:String):List[String]={
    return List("a")
  }

  def ipStringToLong(ip:String):Long={
    val r=ip.split("\\.")
    var index=4;
    (for(number <- r)yield {
      index -=1
      number.toLong << (index*8)
    }).reduce(_ + _)
  }


  def longToIpString(ip:Long):String={
    ((ip & 0xff000000 )>> 24) +"."+((ip & 0x00ff0000) >>16)+ "."+((ip & 0x0000ff00) >> 8)+"."+(ip & 0x000000ff).toString
  }

  def ipStringToBytes(ip:String):Array[Byte]={
    val ipNumber=ipStringToLong(ip)
    val data=new Array[Byte](4)
    data(0)=(ipNumber & 0x000000ff).toByte
    data(1)=((ipNumber & 0x0000ff00) >> 8).toByte
    data(2)=((ipNumber & 0x00ff0000) >> 16).toByte
    data(3)=((ipNumber & 0xff000000) >> 24).toByte
    data
  }

  //TODO
  def bytesToipString(ipData:Array[Byte]):String={
    "TODO"
  }

  def main(args: Array[String]) {
    val a=ipStringToLong("192.168.0.81")
    println(a)
    val b=longToIpString(a)
    println(b)
  }

}
