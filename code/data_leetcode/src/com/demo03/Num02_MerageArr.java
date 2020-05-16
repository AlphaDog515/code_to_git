package com.demo03;

import java.util.Arrays;

public class Num02_MerageArr {
	public static void main(String[] args) {
		Solution_02 so = new Solution_02();
		int[] arr1 = { 1, 2, 3, 0, 0, 0 };
		int[] arr2 = { 2, 5, 6 };
		so.merge(arr1, 3, arr2, 3);
		System.out.println(Arrays.toString(arr1));
		
		
		int[] arr3 = {1,1,1,2,2,2,2,11,13,13,20,20};
		int res = so.removeDuplicates(arr3);
		System.out.println(Arrays.toString(arr3));
		System.out.println(res);
	}
}
/*
给定两个有序整数数组nums1和nums2，将nums2合并到nums1中，使得num1成为一个有序数组。
说明:
	初始化nums1和nums2的元素数量分别为 m 和 n。
	你可以假设 nums1 有足够的空间（空间大小大于或等于 m + n）来保存 nums2 中的元素。
示例:
	输入:
	nums1 = [1,2,3,0,0,0], m = 3
	nums2 = [2,5,6],       n = 3	
	输出: [1,2,2,3,5,6]

*/
class Solution_02 {
	public void merge(int[] nums1, int m, int[] nums2, int n) {
		int len1 = m - 1;
		int len2 = n - 1;
		int len = m + n - 1;
		while (len1 >= 0 && len2 >= 0) {
			// 注意--符号在后面，表示先进行计算再减1，这种缩写缩短了代码
			nums1[len--] = nums1[len1] > nums2[len2] ? nums1[len1--] : nums2[len2--];
		}
		// 表示将nums2数组从下标0位置开始，拷贝到nums1数组中，从下标0位置开始，长度为len2+1
		System.arraycopy(nums2, 0, nums1, 0, len2 + 1);
	}

	/*	 
	 给定一个排序数组，你需要在原地删除重复出现的元素，使得每个元素最多出现两次，返回移除后数组的新长度。
	不要使用额外的数组空间，你必须在原地修改输入数组并在使用 O(1) 额外空间的条件下完成。	 
	 */	
	public int removeDuplicates(int[] nums) {
		int m = 0;
		for (int i = 0; i < nums.length;) {
			if (i < nums.length - 1 && nums[i] == nums[i + 1]) {
				int val = nums[i];
				nums[m++] = nums[i++];
				nums[m++] = nums[i++];
				while (i < nums.length && nums[i] == val)
					i++;
			} else
				nums[m++] = nums[i++];
		}
		return m;
	}
}
