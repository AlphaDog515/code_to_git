package com.app.course


import org.apache.flink.api.common.state.ValueStateDescriptor
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.co.CoProcessFunction
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

case class OrderEvent(orderId: Long, eventType: String, txId: String, eventTime: Long)

case class OrderResult(orderId: Long, resultMsg: String)

case class ReceiptEvent(txId: String, payChannel: String, timestamp: Long)

object Demo01 {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)

    val path1 = getClass.getResource("/OrderLog.csv").getPath
    val inputDS1 = env.readTextFile(path1)
    val dataDS1 = inputDS1
      .map(data => {
        val arr = data.split(",")
        OrderEvent(arr(0).toLong, arr(1), arr(2), arr(3).toLong)
      })
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[OrderEvent](Time.seconds(3)) {
        override def extractTimestamp(element: OrderEvent): Long = element.eventTime * 1000L
      })
      .filter(_.txId != "")
      .keyBy(_.txId)


    val path2 = getClass.getResource("/ReceiptLog.csv").getPath
    val inputDS2 = env.readTextFile(path2)
    val dataDS2 = inputDS2
      .map(data => {
        val arr = data.split(",")
        ReceiptEvent(arr(0), arr(1), arr(2).toLong)
      })
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[ReceiptEvent](Time.seconds(3)) {
        override def extractTimestamp(element: ReceiptEvent): Long = element.timestamp * 1000L
      })
      .filter(_.txId != "")
      .keyBy(_.txId)


    val resDS: DataStream[(OrderEvent, ReceiptEvent)] = dataDS1.connect(dataDS2).process(new OrderTxDetect)

    val unMatchPayTag = new OutputTag[OrderEvent]("unmatch_pays")
    val unMatchReceiptsTag = new OutputTag[ReceiptEvent]("unmatch_receipts")

    resDS.print("resDS")
    resDS.getSideOutput(unMatchPayTag).print("unmatch_pays")
    resDS.getSideOutput(unMatchReceiptsTag).print("unmatch_receipts")
    env.execute("order pay tx job")

  }

}

class OrderTxDetect extends CoProcessFunction[OrderEvent, ReceiptEvent, (OrderEvent, ReceiptEvent)] {

  lazy val payState =
    getRuntimeContext.getState[OrderEvent](new ValueStateDescriptor[OrderEvent]("pay", classOf[OrderEvent]))

  lazy val receiptState =
    getRuntimeContext.getState[ReceiptEvent](new ValueStateDescriptor[ReceiptEvent]("receipt", classOf[ReceiptEvent]))

  val unMatchPayTag = new OutputTag[OrderEvent]("unmatch_pays")
  val unMatchReceiptsTag = new OutputTag[ReceiptEvent]("unmatch_receipts")


  override def processElement1(pay: OrderEvent,
                               ctx: CoProcessFunction[OrderEvent, ReceiptEvent, (OrderEvent, ReceiptEvent)]#Context,
                               out: Collector[(OrderEvent, ReceiptEvent)]): Unit = {

    val receipt = receiptState.value()
    if (receipt != null) {
      out.collect((pay, receipt))
      receiptState.clear()
    } else {
      payState.update(pay)
      ctx.timerService().registerEventTimeTimer(pay.eventTime * 1000L + 5000L)
    }
  }


  override def processElement2(receipt: ReceiptEvent,
                               ctx: CoProcessFunction[OrderEvent, ReceiptEvent, (OrderEvent, ReceiptEvent)]#Context,
                               out: Collector[(OrderEvent, ReceiptEvent)]): Unit = {

    val pay = payState.value()
    if (pay != null) {
      out.collect((pay, receipt))
      payState.clear()
    } else {
      receiptState.update(receipt)
      ctx.timerService().registerEventTimeTimer(receipt.timestamp * 1000L + 3000L)
    }

  }

  override def onTimer(timestamp: Long,
                       ctx: CoProcessFunction[OrderEvent, ReceiptEvent, (OrderEvent, ReceiptEvent)]#OnTimerContext,
                       out: Collector[(OrderEvent, ReceiptEvent)]): Unit = {
    if (payState.value() != null) {
      ctx.output(unMatchPayTag, payState.value())
    }

    if (receiptState.value() != null) {
      ctx.output(unMatchReceiptsTag, receiptState.value())
    }

    payState.clear()
    receiptState.clear()
  }

}
