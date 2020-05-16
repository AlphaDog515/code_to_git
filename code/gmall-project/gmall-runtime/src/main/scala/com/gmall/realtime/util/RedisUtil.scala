package com.gmall.realtime.util

import redis.clients.jedis.Jedis

object RedisUtil {
  // 不用连接池，有问题
  private val host: String = ConfigUtil.getProperty("redis.host")
  private val port = ConfigUtil.getProperty("redis.port").toInt

  def getClient = {
    val jedis: Jedis = new Jedis(host, port, 60 * 1000)
    jedis.connect()
    jedis
  }

  def getClient01 = {
    val jedis: Jedis = new Jedis(host, port, 60 * 1000)
    jedis.connect()
    jedis
  }

}
