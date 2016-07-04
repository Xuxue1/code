package com.xuxue.code.scala.spider

import java.net.{InetAddress, InetSocketAddress, Socket}

import scala.util.{Failure, Success, Try}

/**
  * Created by HanHan on 2016/7/4.
  */
class IPList(minNumber:Long,maxNumber:Long) {

  val size=maxNumber-minNumber;

  def this(min:String,max:String)=this(IPList.ipStringToLong(min),IPList.ipStringToLong(max))

  def map[A](f : (Array[Byte])=>A):List[A]={
    (for(ip <- minNumber until  maxNumber)yield {
      f(IPList.longToBytes(ip))
    }).toList
  }

  /**
    *
    * @param port
    * @param timeout
    * @return
    */
  def ?(port:Int,timeout:Int):List[Try[String]]={
    def scan(array: Array[Byte],prot:Int):Try[String]={
      Try{
        val address=InetAddress.getByAddress(array)
        val socketAddress=new InetSocketAddress(address,80)
        val socket=new Socket()
        socket.connect(socketAddress,timeout)
        address.getHostAddress
      }
    }
    map((data:Array[Byte])=>{
        scan(data,port)
    })
  }

  /**
    * 默认500毫秒扫描ip
    * @param port
    * @return
    */
  def ?(port:Int):List[Try[String]]={
    ?(port,500)
  }



  def fork(forkNum:Int):List[IPList]={
    val fork=(math.ceil(size/forkNum.toDouble)).toInt
    println("fork="+fork)
    (for(num <- 0 until  forkNum)yield {
      val min=minNumber+fork*num;
      val max=minNumber+(fork*(num+1));
      if(max>maxNumber) IPList(min,maxNumber) else IPList(min,max)
    }).toList
  }




}

object IPList{


  def apply(minNumber: Long, maxNumber: Long): IPList = new IPList(minNumber, maxNumber)

  def apply(minNumber: String, maxNumber: String): IPList = new IPList(minNumber, maxNumber)
  /**
    *
    * @param min
    * @param max
    * @return
    */
  def getIPRange(min:String,max:String):List[String]={
    return List("a")
  }

  /**
    * 将一个ip地址转换为长整形数据
    *
    * @param ip
    * @return
    */
  def ipStringToLong(ip:String):Long={
    val r=ip.split("\\.")
    var index=4;
    (for(number <- r)yield {
      index -=1
      number.toLong << (index*8)
    }).reduce(_ + _)
  }

  /**
    * 将一个长整形数据转换为ip
    *
    * @param ip
    * @return
    */
  def longToIpString(ip:Long):String={
    ((ip & 0xff000000 )>> 24) +"."+((ip & 0x00ff0000) >>16)+ "."+((ip & 0x0000ff00) >> 8)+"."+(ip & 0x000000ff).toString
  }

  def longToBytes(ip:Long):Array[Byte]={
    val data=new Array[Byte](4)
    data(3)=(ip & 0x000000ff).toByte
    data(2)=((ip & 0x0000ff00) >> 8).toByte
    data(1)=((ip & 0x00ff0000) >> 16).toByte
    data(0)=((ip & 0xff000000) >> 24).toByte
    data
  }

  /**
    * 将字符串转换为字节数组
    *
    * @param ip
    * @return
    */
  def ipStringToBytes(ip:String):Array[Byte]={
    longToBytes(ipStringToLong(ip))
  }

  /**
    * 将字节数组转换为ip字符串
    *
    * @param ipData
    * @return
    */
  def bytesToipString(ipData:Array[Byte]):String={
    (ipData(0) & 0xff)+"."+(ipData(1)&0xFF)+"." +
      "" +(ipData(2) & 0xff)+"."+(ipData(3)& 0xff)
  }


  def main(args: Array[String]) {
      val a=IPList("192.168.0.81","192.168.0.255")
            val r=a ?(80,100)
            val data=r.filter((data:Try[String])=>{
            data match {
              case Success(ms)=>true
              case Failure(ms)=>false
            }
      })
    data.map(println)
  }
}
