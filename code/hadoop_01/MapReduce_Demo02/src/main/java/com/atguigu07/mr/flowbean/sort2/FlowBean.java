package com.atguigu07.mr.flowbean.sort2;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

/*
 * 
 */
// 自定义的Bean如果要作为key需要有排序的功能
public class FlowBean implements WritableComparable<FlowBean> {

	private long upFlow;
	private long downFlow;
	private Long sumFlow;

	public FlowBean() {

	}

	public long getUpFlow() {
		return upFlow;
	}

	public void setUpFlow(long upFlow) {
		this.upFlow = upFlow;
	}

	public long getDownFlow() {
		return downFlow;
	}

	public void setDownFlow(long downFlow) {
		this.downFlow = downFlow;
	}

	public long getSumFlow() {
		return sumFlow;
	}

	public void setSumFlow(long sumFlow) {
		this.sumFlow = sumFlow;
	}

	// 序列化 在写出属性时，如果为引用数据类型，属性不能为null
	@Override
	public void write(DataOutput out) throws IOException {

		out.writeLong(upFlow);
		out.writeLong(downFlow);
		out.writeLong(sumFlow);

	}

	// 反序列化 序列化和反序列化的顺序要一致
	@Override
	public void readFields(DataInput in) throws IOException {
		upFlow = in.readLong();
		downFlow = in.readLong();
		sumFlow = in.readLong();

	}

	@Override
	public String toString() {
		return upFlow + "\t" + downFlow + "\t" + sumFlow;
	}

	// 系统封装的比较器在对比key时，调用key的compareTo进行比较
	// 降序比较总流量
	@Override
	public int compareTo(FlowBean o) {

		return -this.sumFlow.compareTo(o.getSumFlow());
	}

}
