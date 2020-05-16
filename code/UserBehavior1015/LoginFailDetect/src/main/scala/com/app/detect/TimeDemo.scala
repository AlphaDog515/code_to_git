package com.app.detect

object TimeDemo {
  def main(args: Array[String]): Unit = {
    import java.text.SimpleDateFormat
    import java.util.TimeZone
    val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    dateFormat.setTimeZone(TimeZone.getTimeZone("Etc/GMT-8"))
    System.out.println("方法一:" + dateFormat.format(new Nothing))
  }

}
