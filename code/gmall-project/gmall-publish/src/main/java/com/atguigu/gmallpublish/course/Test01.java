package com.atguigu.gmallpublish.course;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmallpublish.mapper.DauMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class Test01 {

    @Autowired
    private DauMapper dauMapper;


    // 1 日活总数
    @GetMapping("/realtime-total")
    public String getRealtimeTotal(@RequestParam("date") String date) {
        Long total = dauMapper.getDau(date);
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map1 = new HashMap<>();
        map1.put("id", "dau");
        map1.put("name", "新增日活");
        map1.put("value", total + "");

        Map<String, String> map2 = new HashMap<>();
        map1.put("id", "dau");
        map1.put("name", "新增日活");
        map1.put("value", "233");

        list.add(map1);
        list.add(map2);

        return JSON.toJSONString(list);
    }


    // 2 日活分时
}