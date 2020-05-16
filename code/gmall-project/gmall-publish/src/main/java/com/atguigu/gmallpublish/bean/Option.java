package com.atguigu.gmallpublish.bean;

import org.apache.zookeeper.Op;

public class Option {

    private String name;
    private Long value;

    public Option(){}

    public Option(String s, long l) {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
