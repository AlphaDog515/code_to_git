package com.demo00;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class Test03_PriorityQueue {
	// peek()//返回队首元素
	// poll()//返回队首元素，队首元素出队列
	// add()//添加元素
	// size()//返回队列元素个数
	// isEmpty()//判断队列是否为空，为空返回true,不空返回false
	public static void main(String[] args) {

		// 不用比较器，默认升序排列
		Queue<Integer> q = new PriorityQueue<>();
		q.add(3);
		q.add(2);
		q.add(4);
		while (!q.isEmpty()) {
			System.out.print(q.poll() + " ");
		}

		// 自定义比较器，降序排列
		Comparator<Integer> cmp = new Comparator<Integer>() {
			public int compare(Integer e1, Integer e2) {
				return e2 - e1;
			}
		};

		Queue<Integer> qq = new PriorityQueue<>(cmp);
		qq.add(3);
		qq.add(2);
		qq.add(4);
		while (!qq.isEmpty()) {
			System.out.print(qq.poll() + " ");
		}
	}
}
