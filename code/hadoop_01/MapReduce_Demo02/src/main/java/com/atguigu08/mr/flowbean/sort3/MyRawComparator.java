package com.atguigu08.mr.flowbean.sort3;

import java.io.IOException;

import org.apache.hadoop.io.DataInputBuffer;
import org.apache.hadoop.io.RawComparator;

public class MyRawComparator implements RawComparator<FlowBean> {

	private FlowBean key1 = new FlowBean();
	private FlowBean key2 = new FlowBean();
	private DataInputBuffer buffer = new DataInputBuffer();

	// 负责从缓冲区中解析出要比较的两个key对象，调用 compare(Object o1, Object o2)对两个key进行对比
	@Override
	public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {

		try {
			buffer.reset(b1, s1, l1); // parse key1
			key1.readFields(buffer);

			buffer.reset(b2, s2, l2); // parse key2
			key2.readFields(buffer);

			buffer.reset(null, 0, 0); // clean up reference
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return compare(key1, key2);
	}

	// Comparable的compare(),实现最终的比较
	@Override
	public int compare(FlowBean o1, FlowBean o2) {
		return -o1.getSumFlow().compareTo(o2.getSumFlow());
	}

}
