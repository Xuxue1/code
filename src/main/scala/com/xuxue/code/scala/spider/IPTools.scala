package com.xuxue.code.scala.spider

/**
  * Created by HanHan on 2016/7/3.
  */
object IPTools {

  def getIPRange(min:String,max:String):List={
    val r=min.split(".")
    val a=r.map(_.toInt)
  }

}
