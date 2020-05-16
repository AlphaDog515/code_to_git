/*
package com.atguigu.gmalllogger.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.gmall.common.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoggerTest01 {

    @PostMapping("/log")
    public String Logger01(@RequestParam("log") String log) {
        //    log = addTs(log);
        //    saveToFile(log);
        //    sendToKafka(log);
        System.out.println("hello world");
        return "ok";
    }

    private String addTs(String log) {
        JSONObject obj = JSON.parseObject(log);
        obj.put("ts", System.currentTimeMillis());
        return obj.toJSONString();
    }

    private Logger logger = LoggerFactory.getLogger(LoggerTest01.class);

    private void saveToFile(String log) {
        logger.info(log);
    }

    @Autowired
    KafkaTemplate template;

    private void sendToKafka(String log) {
        String topic = Constant.TOPIC_STARTUP;
        if (log.contains("event")) {
            topic = Constant.TOPIC_EVENT;
        }
        template.send(topic, log);
    }
}
*/
