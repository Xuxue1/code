name := "code"

organization :="com.xuxue.code"

version := "1.0"

scalaVersion := "2.11.8"

val raw: Seq[File] =Seq(file("lib"))

val cp: Classpath = raw.classpath

libraryDependencies += "log4j" % "log4j" % "1.2.17"

libraryDependencies += "org.jsoup" % "jsoup" % "1.8.3"

libraryDependencies += "antlr" % "antlr" % "2.7.2"

libraryDependencies += "net.sourceforge.jchardet" % "jchardet" % "1.0"

libraryDependencies +="org.apache.httpcomponents" % "httpclient" % "4.5.1"