package com.atguigu09.mr.groupcompare;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class OrderBean implements WritableComparable<OrderBean>{
	
	private String orderId;
	private String pId;
	private Double acount;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public Double getAcount() {
		return acount;
	}
	public void setAcount(Double acount) {
		this.acount = acount;
	}
	public OrderBean() {
		
	}
	@Override
	public String toString() {
		return orderId + "\t" + pId + "\t" + acount ;
	}
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(orderId);
		out.writeUTF(pId);
		out.writeDouble(acount);
		
	}
	@Override
	public void readFields(DataInput in) throws IOException {
		orderId=in.readUTF();
		pId=in.readUTF();
		acount=in.readDouble();
	}
	
	// 二次排序，先按照orderid排序(升降序都可以)，再按照acount(降序)排序
	@Override
	public int compareTo(OrderBean o) {
		
		//先按照orderid排序升序排序
		int result=this.orderId.compareTo(o.getOrderId());
		
		if (result==0) {
			//再按照acount(降序)排序
			result=-this.acount.compareTo(o.getAcount());
			
		}
		
		return result;
	}
	
	
	
	

}
