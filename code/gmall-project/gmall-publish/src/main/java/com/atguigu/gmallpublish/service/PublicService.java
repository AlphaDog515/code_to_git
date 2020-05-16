package com.atguigu.gmallpublish.service;

import java.io.IOException;
import java.util.Map;

public interface PublicService {

    Long getDau(String date);

    Map<String, Long> getHourDau(String date);


    Double getTotalAmount(String date);


    // map("10" -> 100)
    Map<String, Double> getHourOrderAmount(String date);


    Map<String,Object> getSaleDetailAndAggGroupByField(
            String date,
            String keyword,
            int startPage,
            int sizePerPage,
            String aggField,
            int aggCount) throws IOException;

}
