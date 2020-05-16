package com.demo03;

/*
 
	 给定一个 n 个元素有序的（升序）整型数组 nums 和一个目标值 target ，
	 写一个函数搜索 nums 中的 target，如果目标值存在返回下标，否则返回 -1。
	
	示例 1:
	输入: nums = [-1,0,3,5,9,12], target = 9
	输出: 4
	解释: 9 出现在 nums 中并且下标为 4
	
	示例 2:
	输入: nums = [-1,0,3,5,9,12], target = 2
	输出: -1
	解释: 2 不存在 nums 中因此返回 -1
	
	
	给定一个排序数组和一个目标值，在数组中找到目标值，并返回其索引。
	如果目标值不存在于数组中，返回它将会被按顺序插入的位置。
	你可以假设数组中无重复元素。
	
	示例 1:	
	输入: [1,3,5,6], 5
	输出: 2
	
	示例 2:	
	输入: [1,3,5,6], 2
	输出: 1
	
	示例 3:	
	输入: [1,3,5,6], 7
	输出: 4
	
	示例 4:	
	输入: [1,3,5,6], 0
	输出: 0


 */
public class Num09_Search {
	public static void main(String[] args) {
//		int[] nums = { 1, 2, 3, 5, 5, 5, 6 };
		int[] nums = { 1, 3, 5, 6 };
		int target = 5;
//		Solution_09 solution = new Solution_09();
//		Solution_09_1 solution = new Solution_09_1();
//		Solution_09_2 solution = new Solution_09_2();
		Solution_09_3 solution = new Solution_09_3();
		int res = solution.searchInsert(nums, target);
		System.out.println(res);
	}
}

// 这个是二分查找算法
class Solution_09_1 {
	public int searchInsert(int[] nums, int target) {
		int len = nums.length;
		int left = 0;
		int right = len - 1;
		while (left < right) {
			int mid = (left + right + 1) >>> 1; // 选择右边界
			if (nums[mid] > target) {
				right = mid - 1;
			} else {
				left = mid;
			}
		}
		if (nums[left] == target) {
			return left;
		}
		return -1;
	}
}

// 倾向于这种写法
class Solution_09_2 {
	public int searchInsert(int[] nums, int target) {
		int len = nums.length;
		int left = 0;
		int right = len - 1;
		while (left < right) {
			int mid = (left + right) >>> 1; // 选择左边界
			if (nums[mid] < target) {
				left = mid + 1;
			} else {
				right = mid;
			}
		}
		// 最后一个数没有比较,如果目标值是最后一个,循环结束时left=right=len-1
		if (nums[left] == target) { 
			return left;
		}
		return -1;
	}
}

// 参考代码 1：重点理解为什么候选区间的索引范围是 [0, size]
class Solution_09_3 {

	public int searchInsert(int[] nums, int target) {
		int len = nums.length;

		if (len == 0) return 0;

		int left = 0;
		int right = len;

		while (left < right) {
			int mid = left + (right - left) / 2; 
			// 选择左右中位数看的是这儿 与 right=len 没有关系 此处选的是左中位数
			// right=len 循环结束以后如果一直没有找到 left=right=len
			if (nums[mid] < target) {
				left = mid + 1;
			} else {
				right = mid;
			}
		}
		System.out.println("right=" + right);
		return left;
	}
}

// 参考代码 2：对于是否接在原有序数组后面单独判断，不满足的时候，
// 再在候选区间的索引范围 [0, size - 1] 内使用二分查找法进行搜索。
class Solution_09_4 {	

	// 特例： 3 5 5 5 5 5 5 5 5 5
	// 特例： 3 6 7 8

	// 1 2 3 5 5 5 5 5 5 6 ，target = 5
	// 1 2 3 3 5 5 5 6 target = 4

	public int searchInsert(int[] nums, int target) {
		int len = nums.length;
		if (len == 0) {
			return -1;
		}
		if (nums[len - 1] < target) {
			return len;
		}
		
		int left = 0;
		int right = len - 1;
		while (left < right) {
			int mid = left + (right - left) / 2;
			if (nums[mid] < target) {				
				left = mid + 1;
			} else {				
				right = mid;
			}
		}
		System.out.println("left=" + left);
		return right;
	}

}








