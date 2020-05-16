package com.demo04;

import java.util.ArrayList;

public class Recu01_GetLuckluNum {

	public static void main(String[] args) {
		Test_01 test = new Test_01();
		int luck = test.getLuckluNum(8);
		System.out.println(luck);
	}
}

class Test_01 {

	public int getLuckluNum(int num) {
		ArrayList<Integer> list = new ArrayList<>(); // 创建集合存储1到num对象
		for (int i = 1; i <= num; i++) { // 将1到num存储在集合中
			list.add(i);
		}
		int count = 1; // 计数器
		for (int i = 0; list.size() != 1; i++) { 
			if (i == list.size()) { // 如果i增长到集合最大的索引+1时
				i = 0; // 重新归零
			}
			if (count % 3 == 0) { // 3的倍数需要remove
				list.remove(i--);
			}
			count++;
		}
		return list.get(0);
	}
}
