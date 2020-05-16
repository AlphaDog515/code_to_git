package com.app.course

import org.apache.flink.api.common.state.ValueStateDescriptor
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

object Demo04 {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)


    val resource = getClass.getResource("/OrderLog.csv")
    val orderEventStream: DataStream[OrderEvent] = env.readTextFile(resource.getPath)

      .map(data => {
        val dataArray = data.split(",")
        OrderEvent(dataArray(0).toLong, dataArray(1), dataArray(2), dataArray(3).toLong)
      })
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[OrderEvent](Time.seconds(3)) {
        override def extractTimestamp(element: OrderEvent): Long = element.eventTime * 1000L
      })


    val orderResultStream: DataStream[OrderResult] = orderEventStream
      .keyBy(_.orderId)
      .process(new OrderPayMatchDetect())


    orderResultStream.print("payed")
    orderResultStream.getSideOutput(new OutputTag[OrderResult]("timeout")).print("timeout")

    env.execute("order timeout without cep job")
  }


}

class OrderPayMatchDetect() extends KeyedProcessFunction[Long, OrderEvent, OrderResult] {

  lazy val isPayedState =
    getRuntimeContext.getState(new ValueStateDescriptor[Boolean]("is_payed", classOf[Boolean]))

  lazy val isCreatedState =
    getRuntimeContext.getState(new ValueStateDescriptor[Boolean]("is_create", classOf[Boolean]))

  lazy val timerTsState =
    getRuntimeContext.getState(new ValueStateDescriptor[Long]("timer_ts", classOf[Long]))

  val orderTimerTag = new OutputTag[OrderResult]("timeout")

  override def processElement(value: OrderEvent,
                              ctx: KeyedProcessFunction[Long, OrderEvent, OrderResult]#Context,
                              out: Collector[OrderResult]): Unit = {

    val isPayed = isPayedState.value()
    val isCreated = isCreatedState.value()
    val timerTs = timerTsState.value()

    if (value.eventType == "create") {
      if (isPayed) {
        out.collect(OrderResult(value.orderId, "pay successfully"))
        isPayedState.clear()
        timerTsState.clear()
        ctx.timerService().deleteEventTimeTimer(timerTs)
      } else {
        val ts = value.eventTime * 1000L + 15 * 60 * 1000L
        ctx.timerService().registerEventTimeTimer(ts)
        timerTsState.update(ts)
        isCreatedState.update(true)
      }

    } else if (value.eventType == "pay") {
      if (isCreated) {
        if (value.eventTime * 1000L < timerTs) {
          out.collect(OrderResult(value.orderId, "pay successfully"))
        } else {
          ctx.output(orderTimerTag, OrderResult(value.orderId, "payed but timeout"))
        }
        isCreatedState.clear()
        timerTsState.clear()
        ctx.timerService().deleteEventTimeTimer(timerTs)
      } else {
        val ts = value.eventTime * 1000L
        ctx.timerService().registerEventTimeTimer(ts)
        timerTsState.update(ts)
        isPayedState.update(true)
      }

    }

  }

  override def onTimer(timestamp: Long,
                       ctx: KeyedProcessFunction[Long, OrderEvent, OrderResult]#OnTimerContext,
                       out: Collector[OrderResult]): Unit = {

    if (isPayedState.value()) {
      ctx.output(orderTimerTag, OrderResult(ctx.getCurrentKey, ""))
    } else {
      ctx.output(orderTimerTag, OrderResult(ctx.getCurrentKey, ""))
    }
    isPayedState.clear()
    isCreatedState.clear()
    timerTsState.clear()

  }
}
