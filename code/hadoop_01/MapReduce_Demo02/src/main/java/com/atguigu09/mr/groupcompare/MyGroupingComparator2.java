package com.atguigu09.mr.groupcompare;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/*
 * 1.继承WritableCompartor  或  实现RawComparator
 * 
 */
public class MyGroupingComparator2 extends WritableComparator {

	public MyGroupingComparator2() {
		super(OrderBean.class, null, true);
	}

	public int compare(WritableComparable a, WritableComparable b) {
		OrderBean o1 = (OrderBean) a;
		OrderBean o2 = (OrderBean) b;
		return o1.getOrderId().compareTo(o2.getOrderId());
	}

}
