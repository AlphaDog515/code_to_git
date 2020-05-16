package com.demo21.greedy;

import java.util.HashSet;

public class Test01 {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashSet<String> hashSet1 = new HashSet<String>();
		HashSet<String> hashSet2 = new HashSet<String>();
		hashSet1.add("1");
		hashSet1.add("2");
		hashSet1.add("100");
		
		hashSet2.add("1");
		hashSet2.add("2");
		hashSet2.add("200");
		hashSet1.retainAll(hashSet2);// 取交集的方法
		System.out.println("hashSet1=" + hashSet1);//[1,2]		
	}

}
