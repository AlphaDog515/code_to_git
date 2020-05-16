package com.app

import org.apache.flink.api.common.state.ValueStateDescriptor
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

object OrderWithProcess {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val path = getClass.getResource("").getPath

    val inputDS = env.readTextFile(path)

    val dataDS = inputDS
      .map(data => {
        val arr = data.split(",")
        OrderEvent(arr(0).toLong, arr(1), arr(2), arr(3).toLong)
      })
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[OrderEvent](Time.seconds(3)) {
        override def extractTimestamp(element: OrderEvent): Long = element.eventTime * 1000L
      })


    // 一个create 一个pay
    val orderRS: DataStream[OrderResult] = dataDS
      .keyBy(_.orderId)
      .process(new OrderMatch)

    orderRS.print("")
    orderRS.getSideOutput(new OutputTag[OrderResult]("timeout")).print()
    env.execute("job")

  }

}

class OrderMatch extends KeyedProcessFunction[Long, OrderEvent, OrderResult] {
  lazy val isPayedState =
    getRuntimeContext.getState(new ValueStateDescriptor[Boolean]("is_pay", classOf[Boolean]))

  lazy val isCreateState =
    getRuntimeContext.getState(new ValueStateDescriptor[Boolean]("is_create", classOf[Boolean]))

  lazy val timerTsState =
    getRuntimeContext.getState(new ValueStateDescriptor[Long]("timer", classOf[Long]))

  override def processElement(value: OrderEvent,
                              ctx: KeyedProcessFunction[Long, OrderEvent, OrderResult]#Context,
                              out: Collector[OrderResult]): Unit = {
    // 先取出状态
    val isPayed = isPayedState.value()
    val isCreated = isCreateState.value()
    val timerTs = timerTsState.value()
    val orderTag = new OutputTag[OrderResult]("timeout")

    if (value.eventTpye == "create") {
      if (isPayed) {
        out.collect(OrderResult(value.orderId, "payed successfully!"))
        isPayedState.clear()
        timerTsState.clear()
        ctx.timerService().deleteEventTimeTimer(timerTs)
      } else {
        val ts = value.eventTime * 1000L + 15 * 60 * 1000L
        ctx.timerService().registerEventTimeTimer(ts)
        timerTsState.update(ts)
        isCreateState.update(true)
      }


    } else if (value.eventTpye == "pay") {
      if (isCreated) {
        if (value.eventTime * 100L < timerTs) {
          out.collect(OrderResult(value.orderId, "pay successfully!"))
        } else {

          ctx.output(orderTag, OrderResult(value.orderId, "timeout"))
        }

        timerTsState.clear()
        isCreateState.clear()
        ctx.timerService().deleteEventTimeTimer(timerTs)


      } else {
        val ts = value.eventTime * 1000L + 15 * 60 * 1000L
        ctx.timerService().registerEventTimeTimer(ts)
        timerTsState.update(ts)
        isCreateState.update(true)

      }

    }


  }

  override def onTimer(timestamp: Long, ctx: KeyedProcessFunction[Long, OrderEvent, OrderResult]#OnTimerContext, out: Collector[OrderResult]): Unit = {
    if(isPayedState.value()){

    }
  }
}
