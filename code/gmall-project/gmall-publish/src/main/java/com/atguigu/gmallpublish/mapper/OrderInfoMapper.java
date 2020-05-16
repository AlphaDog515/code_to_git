package com.atguigu.gmallpublish.mapper;

import java.util.List;
import java.util.Map;

public interface OrderInfoMapper {

    // 总的销售额
    Double getTotalAmount(String date);

    // 小时销售额 1行数据有两列
    List<Map<String, Object>> getHourAmount(String date);
}
