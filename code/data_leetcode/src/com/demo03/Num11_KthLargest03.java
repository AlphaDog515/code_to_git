package com.demo03;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Random;

public class Num11_KthLargest03 {
	public static void main(String[] args) {
		int[] arr = { 2, 3, 5, 5, 1, 4, 0 };
		Solution3_11_2 solution = new Solution3_11_2();
		int res = solution.findKthLargest(arr, 2);
		System.out.println(res);
	}
}

class Solution3_11_1 {
	public int findKthLargest(int[] nums, int k) {
		Arrays.sort(nums);
		return nums[nums.length - k];
	}
}

class Solution3_11_2 {

	public int findKthLargest(int[] nums, int k) {
		buildHeap(nums, k);
		// 遍历剩下元素，比堆顶小，跳过；比堆顶大，交换后重新堆化
		for (int i = k; i < nums.length; i++) {
			if (nums[i] < nums[0]) {
				continue;
			}
			swap(nums, i, 0);
			heapify(nums, 0, k);
		}
		return nums[0];
	}

	private void buildHeap(int[] arr, int length) {
		for (int i = length / 2 - 1; i >= 0; i--) {
			heapify(arr, i, length);
		}
	}

	// 堆化函数,父节点下标i，左右子节点的下标分别为 2*i+1 和 2*i+2
	private void heapify(int[] arr, int i, int length) {
		// 临时变量 minPos 用于存储最小值的下标，先假设父节点最小
		int minPos = i;
		while (true) {
			if (2 * i + 1 < length && arr[2 * i + 1] < arr[minPos]) {
				minPos = 2 * i + 1;
			}
			if (2 * i + 2 < length && arr[2 * i + 2] < arr[minPos]) {
				minPos = 2 * i + 2;
			}
			if (minPos == i) {
				break;
			}
			swap(arr, i, minPos);
			i = minPos;
		}
	}

	private void swap(int[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
}

class Solution3_11_3 {

	public int findKthLargest(int[] nums, int k) {
		int len = nums.length;
		int targetIndex = len - k;
		int left = 0, right = len - 1;
		while (true) {
			int i = partition01(nums, left, right);
			if (i == targetIndex) {
				return nums[i];
			} else if (i < targetIndex) {
				left = i + 1;
			} else {
				right = i - 1;
			}
		}
	}

	/*
		分区函数，将 arr[high]作为 pivot 分区点，i、j 两个指针，
		i作为标记“已处理区间”和“未处理区间”的分界点，也即 i 左边的（low~i-1）都是“已处理区”。
		j指针遍历数组，当 arr[j] 小于 pivot 时，
		就把 arr[j] 放到“已处理区间”的尾部，也即是 arr[i] 所在位置，
		因此 swap(arr, i, j) 然后 i 指针后移，i++，
		直到 j 遍历到数组末尾 arr[high]，将 arr[i] 和 arr[high]（pivot点） 进行交换，
		返回下标 i，就是分区点的下标。
	 */
	// https://leetcode-cn.com/problems/kth-largest-element-in-an-array/solution/java-jie-zhu-partitionfen-qu-jian-zhi-on-by-yankua/
	private int partition01(int[] arr, int left, int right) {
		int i = left;
		int pivot = arr[right];
		for (int j = left; j < right; j++) {
			if (arr[j] < pivot) {
				swap(arr, i, j);
				i++;
			}
		}
		swap(arr, i, right);
		return i;
	}

	private int partition02(int[] arr, int left, int right) {
		if (right > left) {
			int random = left + new Random().nextInt(right - left);
			swap(arr, right, random);
		}
		int i = left;
		int pivot = arr[right];
		for (int j = left; j < right; j++) {
			if (arr[j] < pivot) {
				swap(arr, i, j);
				i++;
			}
		}
		swap(arr, i, right);
		return i;
	}

	private void swap(int[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

}

class Solution3_11_4 {
	public int findKthLargest(int[] nums, int k) {
		PriorityQueue<Integer> heap = new PriorityQueue<Integer>((n1, n2) -> n1 - n2);
		for (int n : nums) {
			heap.add(n);
			if (heap.size() > k)
				heap.poll();
		}
		return heap.poll();
	}
}
