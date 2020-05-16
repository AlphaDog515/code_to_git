package com.demo00;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Test03_PriorityQueue01 {

	public static void main(String[] args) {
		int[] arr = { 10, 2, 4, 3, 1 };

		PriorityQueue<Integer> heap = new PriorityQueue<Integer>(new Comparator<Integer>() {
			@Override
			public int compare(Integer n1, Integer n2) {
				return n1 - n2; // 小顶堆，poll返回值是最小的元素：-1< 0= 1>
			}
		});
		for (int n : arr) {
			heap.add(n);
		}
		System.out.println(heap); // [1, 2, 4, 10, 3]
		System.out.println(heap.poll()); // 1
		heap.add(8);
		System.out.println(heap.toString()); // [2, 3, 4, 10, 8]
	}
}