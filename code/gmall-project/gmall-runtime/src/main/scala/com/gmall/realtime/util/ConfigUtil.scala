package com.gmall.realtime.util

import java.io.InputStream
import java.util.Properties

object ConfigUtil {

  private val is: InputStream = getClass.getClassLoader.getResourceAsStream("config.properties")
  private val properties: Properties = new Properties()
  properties.load(is)

  def getProperty(key: String) = properties.getProperty(key)

  def main(args: Array[String]): Unit = {
    val res: String = getProperty("kafka.servers")
    println(res)
  }

}
