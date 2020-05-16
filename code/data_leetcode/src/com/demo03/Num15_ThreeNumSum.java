package com.demo03;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Num15_ThreeNumSum {
	public static void main(String[] args) {
		int[] nums = { -1, 0, 1, 2, -1, -4 };
		Solution_15_1 solution = new Solution_15_1();
		List<List<Integer>> res = solution.threeSum(nums);
		System.out.println(res);
	}
}

/*
 
	解题思路：
	暴力法搜索为 O(N^3) 时间复杂度，可通过双指针动态消去无效解来优化效率。
	双指针法铺垫： 先将给定 nums 排序，复杂度为 O(NlogN)。
	双指针法思路： 
	
	固定 3个指针中最左（最小）数字的指针 k，双指针 i，j 分设在数组索引(k,len(nums)) 两端，
	通过双指针交替向中间移动，记录对于每个固定指针 k 的所有满足 nums[k] + nums[i] + nums[j] == 0 的 i,j 组合：
	当 nums[k] > 0 时直接break跳出：因为 nums[j] >= nums[i] >= nums[k] > 0，即 3 个数字都大于 0 ，
	在此固定指针 k 之后不可能再找到结果了。
	
	当 k > 0且nums[k] == nums[k - 1]时即跳过此元素nums[k]：
		因为已经将 nums[k - 1] 的所有组合加入到结果中，本次双指针搜索只会得到重复组合。
	
	i，j 分设在数组索引 (k, len(nums))两端，
	当i < j时循环计算s = nums[k] + nums[i] + nums[j]，并按照以下规则执行双指针移动：
		当s < 0时，i += 1并跳过所有重复的nums[i]；
		当s > 0时，j -= 1并跳过所有重复的nums[j]；
		当s == 0时，记录组合[k, i, j]至res，执行i += 1和j -= 1并跳过所有重复的nums[i]和nums[j]，
			防止记录到重复组合。
	
	复杂度分析：
	时间复杂度 O(N^2)：其中固定指针k循环复杂度 O(N)，双指针 i，j 复杂度 O(N)。
	空间复杂度 O(1)：指针使用常数大小的额外空间。


 
 */


class Solution_15_1 {
	public List<List<Integer>> threeSum(int[] nums) {
		Arrays.sort(nums);
		List<List<Integer>> res = new ArrayList<>();
		for (int k = 0; k < nums.length - 2; k++) {
			if (nums[k] > 0)
				break;
			if (k > 0 && nums[k] == nums[k - 1])
				continue;
			int i = k + 1, j = nums.length - 1;
			while (i < j) {
				int sum = nums[k] + nums[i] + nums[j];
				if (sum < 0) {
					while (i < j && nums[i] == nums[++i]);
				} else if (sum > 0) {
					while (i < j && nums[j] == nums[--j]);
				} else {
					res.add(new ArrayList<Integer>(Arrays.asList(nums[k], nums[i], nums[j])));
					while (i < j && nums[i] == nums[++i]);						
					while (i < j && nums[j] == nums[--j]);
				}
			}
		}
		return res;
	}
}
