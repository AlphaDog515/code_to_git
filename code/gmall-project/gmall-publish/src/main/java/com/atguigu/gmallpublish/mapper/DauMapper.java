package com.atguigu.gmallpublish.mapper;

import java.util.List;
import java.util.Map;

public interface DauMapper {

    // 获取日活的接口
    Long getDau(String date);


    // 日活 调用两次 就可以返回两天
    List<Map<String, Object>> getHourDau(String date);


}
