package com.atguigu.gmallpublish.controller;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmallpublish.bean.Option;
import com.atguigu.gmallpublish.bean.SaleInfo;
import com.atguigu.gmallpublish.bean.Stat;
import com.atguigu.gmallpublish.service.PublicService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class PublicController {

    @Autowired
    PublicService publicService;

    @GetMapping("/realtime-total")
    public String realtimeTotal(@RequestParam("date") String date) {

        ArrayList<Map<String, String>> result = new ArrayList<>();
        Map<String, String> map1 = new HashMap<>();

        map1.put("id", "dau");
        map1.put("name", "新增日活");
        map1.put("value", publicService.getDau(date).toString());
        result.add(map1);

        Map<String, String> map2 = new HashMap<>();
        map2.put("id", "dau");
        map2.put("name", "新增设备");
        map2.put("value", "123");
        result.add(map2);


        Map<String, String> map3 = new HashMap<>();
        map3.put("id", "order_amount");
        map3.put("name", "新增交易额");
        map3.put("value", publicService.getTotalAmount(date).toString());
        result.add(map3);


        return JSON.toJSONString(result);
    }

    @GetMapping("/realtime-hour")
    public String realtimeHour(@RequestParam("id") String id, @RequestParam("date") String date) {

        if ("dau".equals(id)) {

            Map<String, Long> today = publicService.getHourDau(date);
            Map<String, Long> yesterday = publicService.getHourDau(getYesterday(date));

            Map<String, Map<String, Long>> result = new HashMap<>();
            result.put("today", today);
            result.put("yesterday", yesterday);

            return JSON.toJSONString(result);

        } else if ("order_amount".equals(id)) {
            Map<String, Double> today = publicService.getHourOrderAmount(date);
            Map<String, Double> yesterday = publicService.getHourOrderAmount(getYesterday(date));

            Map<String, Map<String, Double>> result = new HashMap<>();
            result.put("today", today);
            result.put("yesterday", yesterday);

            return JSON.toJSONString(result);
        }

        return "";

    }


    private String getYesterday(String today) {
        String yesterday = LocalDate.parse(today).plusDays(-1).toString();
        return yesterday;
    }


    @GetMapping("/sale_detail")
    public String SaleDetail(@RequestParam("date") String date,
                             @RequestParam("startPage") int startPage,
                             @RequestParam("size") int size,
                             @RequestParam("keyword") String keyword) throws IOException {


        Map<String, Object> resGender = publicService.getSaleDetailAndAggGroupByField(date, keyword, startPage, size, "user_gender", 2);
        Map<String, Object> resAge = publicService.getSaleDetailAndAggGroupByField(date, keyword, startPage, size, "user_age", 2);


        SaleInfo saleInfo = new SaleInfo();

        // 总数
        saleInfo.setTotal((Integer) resAge.get("total"));

        // 明细
        List<Map<String,Object>> detail = (List<Map<String,Object>>)resAge.get("detail");
        saleInfo.setDedail(detail);


        // 封装饼图

        Stat genderStat = new Stat();
        genderStat.setTitle("用户性别占比");
        Map<String,Long> genderAgg = (Map<String,Long>)resGender.get("agg");
        for(String key:genderAgg.keySet()){
            Option opt = new Option();
            opt.setName(key.replace("F","女").replace("M","男"));
            opt.setValue(genderAgg.get(key));
            genderStat.addOption(opt);
        }
        saleInfo.addStat(genderStat);

        Stat ageStat = new Stat();
        ageStat.addOption(new Option("20岁以下",0L));
        ageStat.addOption(new Option("20-30岁",0L));
        ageStat.addOption(new Option("30岁以上",0L));
        genderStat.setTitle("用户性别占比");
        Map<String,Long> ageAgg = (Map<String,Long>)resGender.get("agg");
        for(String key:ageAgg.keySet()){
            int age = Integer.parseInt(key);
            Long value = ageAgg.get(key);
            if(age <20){
                Option opt = ageStat.getOptions().get(0);
                opt.setValue(opt.getValue()+value);
            }else if(age <30){
                Option opt = ageStat.getOptions().get(1);
                opt.setValue(opt.getValue()+value);
            }else{
                Option opt = ageStat.getOptions().get(2);
                opt.setValue(opt.getValue()+value);
            }

        }
        saleInfo.addStat(ageStat);




        return JSON.toJSONString(saleInfo);

    }


}
