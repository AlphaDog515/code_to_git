package com.demo03;

public class Num03_FindMedian01 {

	public static void main(String[] args) {
		int[] nums1 = { 1, 3, 5, 8, 20, 30 };
		int[] nums2 = { 1, 3, 9 };
		Num03_FindMedian01 so = new Num03_FindMedian01();
//		double res = so.findMedianSortedArrays(nums1, nums2);
//		System.out.println(res);
		
//		int test = so.countSmallOrEqual(10, nums1);
//		System.out.println(test);
		
		double t1 = so.findKthSmall(5, nums1, nums2);
		System.out.println(t1);

	}

	public double findMedianSortedArrays(int[] nums1, int[] nums2) {
		int length = nums1.length + nums2.length;
		if (length % 2 != 0) {
			int mid = length / 2 + 1;
			return findKthSmall(mid, nums1, nums2);
		} else {
			int mid1 = length / 2;
			int mid2 = length / 2 + 1;
			return (findKthSmall(mid1, nums1, nums2) + findKthSmall(mid2, nums1, nums2)) / 2.0;
		}
	}

	public double findKthSmall(int k, int[] nums1, int[] nums2) {
		if (nums1.length == 0)
			return nums2[k - 1];
		if (nums2.length == 0)
			return nums1[k - 1];
		int start = Math.min(nums1[0], nums2[0]);
		int end = Math.max(nums1[nums1.length - 1], nums2[nums2.length - 1]);
		while (start + 1 < end) {
			int mid = start + (end - start) / 2;
			if (countSmallOrEqual(mid, nums1) + countSmallOrEqual(mid, nums2) < k) {
				start = mid;
			} else {
				end = mid;
			}
		}
		if (countSmallOrEqual(start, nums1) + countSmallOrEqual(start, nums2) >= k)
			return start;
		else
			return end;

	}

	// 将num插入到数组arr中应该插入的新位置0 1 2 
	// 相等 往后移 插在最后
 	public int countSmallOrEqual(int num, int[] arr) {
		int start = 0;
		int end = arr.length - 1;
		while (start + 1 < end) {
			int mid = start + (end - start) / 2;
			if (arr[mid] <= num)
				start = mid;
			else
				end = mid;
		}
		if (arr[start] > num)
			return start;
		if (arr[end] > num)
			return end;
		return arr.length;
	}
}
