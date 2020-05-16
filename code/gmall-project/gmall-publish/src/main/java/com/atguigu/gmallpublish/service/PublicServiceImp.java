package com.atguigu.gmallpublish.service;


import com.atguigu.gmallpublish.mapper.DauMapper;
import com.atguigu.gmallpublish.mapper.OrderInfoMapper;

import com.gmall.common.Constant;
import com.gmall.scala.util.ESUtil;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.TermsAggregation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PublicServiceImp implements PublicService {

    @Autowired
    DauMapper dauMapper;

    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Override
    public Long getDau(String date) {
        return dauMapper.getDau(date);
    }

    @Override
    public Map<String, Long> getHourDau(String date) {
        List<Map<String, Object>> hourDauList = dauMapper.getHourDau(date);

        Map<String, Long> resMap = new HashMap<>(); // 保存

        for (Map<String, Object> map : hourDauList) {
            String hour = (String) map.get("HOUR");
            Long count = (Long) map.get("COUNT");
            resMap.put(hour, count);
        }


        return resMap;
    }


    // 总的销售额
    @Override
    public Double getTotalAmount(String date) {
        Double res = orderInfoMapper.getTotalAmount(date);

        return res == null ? 0 : res;
    }

    @Override
    public Map<String, Double> getHourOrderAmount(String date) {
        List<Map<String, Object>> hourAmount = orderInfoMapper.getHourAmount(date);
        Map<String, Double> res = new HashMap<>();

        for (Map<String, Object> map : hourAmount) {
            String hour = (String) map.get("CREATE_HOUR");
            Double amount = ((BigDecimal) map.get("SUM")).doubleValue();
            res.put(hour, amount);
        }


        return res;
    }

    @Override
    public Map<String, Object> getSaleDetailAndAggGroupByField(String date,
                                                               String keyword,
                                                               int startPage,
                                                               int sizePerPage,
                                                               String aggField,
                                                               int aggCount) throws IOException {


        // 获取es客户端  查询数据  解析结果
        // 总数  聚合结果  明细

        Map<String,Object> result = new HashMap<>();


        JestClient client = ESUtil.getClient();  // 访问Scala的代码

        String dsl = DSLs.getDSL(date,keyword,startPage,sizePerPage,aggField,aggCount);
        Search search = new Search.Builder(dsl)
                .addIndex(Constant.INDEX_SALE_DETAIL)
                .addType("_doc")
                .build();

        SearchResult searchResult = client.execute(search);  // 异常抛出

        Integer total = searchResult.getTotal();
        result.put("total",total);

        List<HashMap> detail = new ArrayList<>();
        List<SearchResult.Hit<HashMap, Void>> hits = searchResult.getHits(HashMap.class);
        for (SearchResult.Hit<HashMap, Void> hit : hits) {
            HashMap source = hit.source;
            detail.add(source);

        }
        result.put("detail",detail);


        List<TermsAggregation.Entry> buckets = searchResult.getAggregations().getTermsAggregation("group_by" + aggField).getBuckets();
        for (TermsAggregation.Entry bucket : buckets) {
            String key = bucket.getKey();
            Long count = bucket.getCount();
        }


        return null;
    }
}
