package com.atguigu;

import java.util.Arrays;

public class Test01 {
	public static void main(String[] args) {

		String p = "wo";
		char[] pArr = p.toCharArray(); // [w,o]
		int[] hash = new int[26];
		for (int i = 0; i < pArr.length; ++i) {
			hash[pArr[i] - 'a']++;
		}
//		[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0]
		System.out.println(Arrays.toString(hash));
	}
}
