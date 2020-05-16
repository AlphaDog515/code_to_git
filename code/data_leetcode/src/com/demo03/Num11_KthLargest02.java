package com.demo03;

import java.util.PriorityQueue;

public class Num11_KthLargest02 {
	public static void main(String[] args) {
		int[] arr = { 3, 2, 3, 1, 2, 4, 5, 5, 6 };
		Solution_11_1 solution = new Solution_11_1();
		int k = 4;
		for (int i = 1; i <= k; i++) {
			int res = solution.findKthLargest(arr, i);
			System.out.println(res);
		}
	}
}
/*
	在未排序的数组中找到第 k 个最大的元素。
	请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。
	
	示例 1:	
	输入: [3,2,1,5,6,4] 和 k = 2
	输出: 5
	示例 2:	
	输入: [3,2,3,1,2,4,5,5,6] 和 k = 4
	输出: 4
	说明:你可以假设 k 总是有效的，且 1 ≤ k ≤ 数组的长度。 
 */

// 时间复杂度 : O(Nlogk),空间复杂度 : O(k)，用于存储堆元素。
class Solution_11_1 {
	public int findKthLargest(int[] nums, int k) {
		// init heap 'the smallest element first'
		PriorityQueue<Integer> heap = new PriorityQueue<Integer>((n1, n2) -> n1 - n2);
		// keep k largest elements in the heap
		for (int n : nums) {
			heap.add(n);
			if (heap.size() > k)
				heap.poll();
		}
		// output
		return heap.poll();
	}
}

/*
	每次随机选取一个基准值arr[pivot]，将数组分成三部分：
		小于arr[pivot]的一部分，等于arr[pivot]的一部分，大于arr[pivot]的一部分。
		然后检查这个基准值最后所在的下标是不是arr.length - K，算法时间复杂度为O(n)。
	具体步骤：	
		在数组区间随机选取pivot = left + random[right-left],根据pivot做partition：
			小于arr[pivot]的部分放在左边
			等于arr[pivot]的部分放在中间
			大于arr[pivot]的部分放在右边
			得到partition的返回值
	返回值是一个人为规定的数组p，数组只有两个值
	p[0]代表等于arr[pivot]的部分的左边界
	p[1]代表等于arr[pivot]的部分的右边界
	分别比较p[0],p[1]与n-k的大小：
		如果p[0] > n - k ，向左寻找，继续寻找数组区间[left,p[0]-1]
		如果p[1] < n - k ，向右寻找，继续寻找数组区间[p[1] + 1,r]
	其他情况直接返回

*/
class Solution_11_2 {

	public int findKthLargest(int[] nums, int k) {
		int len = nums.length;
		return quickSort(nums, 0, nums.length - 1, len - k);
	}

	private int quickSort(int[] arr, int l, int r, int k) {
		if (l == r) {
			return arr[l];
		}

		int pivot = l + (int) (Math.random() * (r - l + 1));
		swap(arr, pivot, r);
		int[] p = partition(arr, l, r);
		if (p[1] < k) {
			return quickSort(arr, p[1] + 1, r, k);
		} else if (p[0] > k) {
			return quickSort(arr, l, p[0] - 1, k);
		} else {
			return arr[p[0]];
		}

	}

	private int[] partition(int[] arr, int l, int r) {
		int less = l - 1;
		int more = r;

		while (l < more) {
			if (arr[l] < arr[r]) {
				swap(arr, ++less, l++);
			} else if (arr[l] > arr[r]) {
				swap(arr, --more, l);
			} else {
				l++;
			}

		}
		swap(arr, more, r);
		return new int[] { less + 1, more };
	}

	private static void swap(int[] arr, int i, int j) {
		int tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}
}

class Solution_11_3 {
	
	public int findKthLargest(int[] nums, int k) {
		// k是要求的第几大(从1开始计数),k-1即是数组中的序号(0开始计数)
		return findKthLargest(nums, 0, nums.length - 1, k - 1);
	}

	public int findKthLargest(int[] nums, int low, int high, int k) {
		int index = partition(nums, low, high, k);
		if (index == k)
			return nums[index];
		else if (index > k)
			return findKthLargest(nums, low, index - 1, k);
		else
			return findKthLargest(nums, index + 1, high, k);
	}

	// 同快排的partation,每次确定一个枢轴的位置,并返回其index
	// 找第k 大 的数就把数组按大->小排列
	private int partition(int[] nums, int low, int high, int k) {
		int pivot = nums[low];

		while (low < high) {
			while (low < high && nums[high] <= pivot) // nums[high]<=pivot 体现出把数组按大->小排列
				high--;
			nums[low] = nums[high];

			while (low < high && nums[low] >= pivot)
				low++;
			nums[high] = nums[low];
		}

		nums[low] = pivot;
		return low;
	}
}