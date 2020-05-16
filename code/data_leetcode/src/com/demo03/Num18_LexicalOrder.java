package com.demo03;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Num18_LexicalOrder {

}

/*
	给定一个整数 n, 返回从 1 到 n 的字典顺序。	
	例如，	
	给定 n =1 3，返回 [1,10,11,12,13,2,3,4,5,6,7,8,9]。	
	请尽可能的优化算法的时间复杂度和空间复杂度。 输入的数据 n 小于等于 5,000,000。

*/

class Solution_18_1 {
	public List<Integer> lexicalOrder01(int n) {
		// List<Integer> res = Stream.iterate(1, item -> item +
		// 1).limit(n).collect(Collectors.toList());
		// Collections.sort(res, Comparator.comparing(String::valueOf));
		// return res;
		return Stream.iterate(1, item -> item + 1).limit(n).sorted(Comparator.comparing(String::valueOf))
				.collect(Collectors.toList());
	}

	public List<Integer> lexicalOrder02(int n) {
		List<Integer> list = new ArrayList<>();
		for (int i = 1; i < 10; i++) {
			if (i <= n) {
				list.add(i);
				add(list, n, i);
			} else {
				break;
			}
		}
		return list;
	}

	private void add(List<Integer> list, int n, int startNum) {
		startNum *= 10;
		for (int i = 0; i < 10; i++, startNum++) {
			if (startNum <= n) {
				list.add(startNum);
				add(list, n, startNum);
			} else {
				return;
			}
		}
	}

}