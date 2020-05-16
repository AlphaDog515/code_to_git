package com.demo03;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

import org.junit.Test;

public class Num11_KthLargest {
	@Test
	public void add() {
		int k = 3;
		int[] arr = new int[] { 4, 5, 8, 2 };
		KthLargest kthLargest = new KthLargest(k, arr);
		List<Integer> list = new ArrayList<>();
		list.add(kthLargest.add(3));
		list.add(kthLargest.add(5));
		list.add(kthLargest.add(10));
		list.add(kthLargest.add(9));
		list.add(kthLargest.add(4));

		System.out.println(list);
	}

}
	/*
	
		设计一个找到数据流中第K大元素的类。注意是排序后的第K大元素，不是第K个不同的元素。		
		你的 KthLargest 类需要一个同时接收整数 k 和整数数组nums 的构造器，
		它包含数据流中的初始元素。每次调用 KthLargest.add，返回当前数据流中第K大的元素。
		
		示例:		
		int k = 3;
		int[] arr = [4,5,8,2];
		KthLargest kthLargest = new KthLargest(3, arr);
		kthLargest.add(3);   // returns 4
		kthLargest.add(5);   // returns 5
		kthLargest.add(10);  // returns 5
		kthLargest.add(9);   // returns 8
		kthLargest.add(4);   // returns 8
		说明:
		你可以假设 nums 的长度≥ k-1 且k ≥ 1。
	
	
	*/

// 关于 Java 的 PriorityQueue 优先级队列
// 1 是线程不安全的队列
// 2 存储使用数组实现
// 3 利用比较器做优先级比较
//
// 实现说明
// 1 最小堆的特性就是最小的值在最上面，每次取O(1)，插入O(n)
// 2 初始化的时候，注意如何添加元素，并给队列一个合适大小的初值
// 3 每次添加元素，能添加到队列的有两种情况，一种未到k个，另一种比堆顶大

class KthLargest {

	private PriorityQueue<Integer> queue;
	private int limit;

	public KthLargest(int k, int[] nums) {
		limit = k;
		queue = new PriorityQueue<>(k);
		for (int num : nums) {
			add(num);
		}
	}

	public int add(int val) {
		if (queue.size() < limit) {
			queue.add(val);
		} else if (val > queue.peek()) {
			queue.poll();
			queue.add(val);
		}
		return queue.peek();
	}

}


// 使用小顶堆完成队优先队列的模拟
// 构建小顶堆，并且root为我们需要的第K个数
// 如果元素不够K个，对剩余的元素填充最小值，然后对堆进行从下到上的堆化
// 添加元素时，与堆顶元素进行比较。如果大于，则进行替换，进行从上到下的堆化；如果小于，直接返回堆顶元素
// 使用小顶堆维护前K大的元素
class KthLargest_01 {

	// 最小堆
	private int[] minHeap;
	private int k;

	public KthLargest_01(int k, int[] nums) {

		this.k = k;
		minHeap = new int[k];
		Arrays.sort(nums);

		int i = 0;
		for (; i < k && i < nums.length; i++) {
			minHeap[i] = nums[nums.length - 1 - i];
		}

		// 当数组不足以填充K个元素时,使用int的最小数填充满
		while (i < k) {
			minHeap[i++] = Integer.MIN_VALUE;
		}

		// 生成堆
		generaterMinHeap();
	}

	// 生成堆
	private void generaterMinHeap() {

		// 从最后一个非叶子节点依次往前调整数
		for (int i = k / 2 - 1; i >= 0; i--) {
			autoAdjustMinHeap(i, k);
		}
	}

	private void autoAdjustMinHeap(int node, int lenth) {

		int left = node * 2 + 1;
		int right = left + 1;

		if (left > lenth - 1) {
			return;
		}

		int min = left;
		if (right > lenth - 1) {
			min = left;
		} else {
			if (minHeap[min] > minHeap[right]) {
				min = right;
			}
		}

		if (minHeap[min] > minHeap[node]) {
			return;
		}

		int t = minHeap[node];
		minHeap[node] = minHeap[min];
		minHeap[min] = t;

		autoAdjustMinHeap(min, lenth);
	}

	public int add(int val) {

		if (val < minHeap[0]) {
			return minHeap[0];
		}

		minHeap[0] = val;
		autoAdjustMinHeap(0, k);
		return minHeap[0];

	}
}
