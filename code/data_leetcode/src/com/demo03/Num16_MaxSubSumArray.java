package com.demo03;

public class Num16_MaxSubSumArray {
	public static void main(String[] args) {
		int[] arr = { -2, 1, -3, 4, -1, 2, 1, -5, 4 };
		Solution_16_1 solution = new Solution_16_1();
		int res = solution.maxSubArray(arr);
		System.out.println(res);
	}
}

/*
	给定一个整数数组 nums ，找到一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。	
	示例:	
	输入: [-2,1,-3,4,-1,2,1,-5,4],
	输出: 6
	解释: 连续子数组 [4,-1,2,1] 的和最大，为 6。
	进阶:	
	如果你已经实现复杂度为 O(n) 的解法，尝试使用更为精妙的分治法求解。
 */

// 动态规划算法
// 时间复杂度：O(N)。只遍历了一次数组。
// 空间复杂度：O(1)，使用了常数的空间。
class Solution_16_1 {

	public int maxSubArray(int[] nums) {
		int n = nums.length, maxSum = nums[0];
		for (int i = 1; i < n; ++i) {
			if (nums[i - 1] > 0)
				nums[i] += nums[i - 1];
			maxSum = Math.max(nums[i], maxSum);
		}
		return maxSum;
	}
}

// 贪心算法
class Solution_16_2 {
	public int maxSubArray(int[] nums) {
		int n = nums.length;
		int currSum = nums[0], maxSum = nums[0];
		for (int i = 1; i < n; ++i) {
			currSum = Math.max(nums[i], currSum + nums[i]);
			maxSum = Math.max(maxSum, currSum);
		}
		return maxSum;
	}
}

class Solution_16_3 {
	public int crossSum(int[] nums, int left, int right, int p) {
		if (left == right)
			return nums[left];

		int leftSubsum = Integer.MIN_VALUE;
		int currSum = 0;
		for (int i = p; i > left - 1; --i) {
			currSum += nums[i];
			leftSubsum = Math.max(leftSubsum, currSum);
		}

		int rightSubsum = Integer.MIN_VALUE;
		currSum = 0;
		for (int i = p + 1; i < right + 1; ++i) {
			currSum += nums[i];
			rightSubsum = Math.max(rightSubsum, currSum);
		}

		return leftSubsum + rightSubsum;
	}

	public int helper(int[] nums, int left, int right) {
		if (left == right)
			return nums[left];

		int p = (left + right) / 2;

		int leftSum = helper(nums, left, p);
		int rightSum = helper(nums, p + 1, right);
		int crossSum = crossSum(nums, left, right, p);

		return Math.max(Math.max(leftSum, rightSum), crossSum);
	}

	public int maxSubArray(int[] nums) {
		return helper(nums, 0, nums.length - 1);
	}
}
//	复杂度分析	
//	时间复杂度：O(NlogN)。
//	空间复杂度：O(logN)，递归时栈使用的空间。
