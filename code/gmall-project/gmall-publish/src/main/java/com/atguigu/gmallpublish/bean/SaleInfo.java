package com.atguigu.gmallpublish.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SaleInfo {
    private Integer total;
    private List<Map<String,Object>> dedail;
    private List<Stat> stats = new ArrayList<>();

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<Map<String, Object>> getDedail() {
        return dedail;
    }

    public void setDedail(List<Map<String, Object>> dedail) {
        this.dedail = dedail;
    }

    public List<Stat> getStats() {
        return stats;
    }



    public void  addStat(Stat stat){
        stats.add(stat);
    }
}
