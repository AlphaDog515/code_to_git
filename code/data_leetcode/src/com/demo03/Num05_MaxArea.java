package com.demo03;

public class Num05_MaxArea {
	// 盛水最多的容器
	// 输入: [1,8,6,2,5,4,8,3,7] 输出: 49
	public static void main(String[] args) {
		int[] arr = { 1, 8, 6, 2, 5, 4, 8, 3, 7 };
		int res = maxArea(arr);
		int res1 = maxArea1(arr);
		System.out.println(res);
		System.out.println(res1);
	}

	private static int maxArea(int[] arr) {
		int maxArea = 0;
		for (int i = 0; i < arr.length - 1; i++) {
			for (int j = i + 1; j < arr.length; j++) {
				int temp = Math.min(arr[i], arr[j]) * (j - i);
				maxArea = maxArea > temp ? maxArea : temp;
			}
		}
		return maxArea;
	}

	// 双指针法，限制面积的有两个因素，一个是短板，一个是距离
	// 开始的时候距离最大，左右指针中短板组成的面积是该短板与所有板子中最大的
	// 要想进一步增大这个面积，只有移动短板的指针
	private static int maxArea1(int[] height) {
		int maxarea = 0, l = 0, r = height.length - 1;
		while (l < r) {
			maxarea = Math.max(maxarea, Math.min(height[l], height[r]) * (r - l));
			if (height[l] < height[r])
				l++;
			else
				r--;
		}
		return maxarea;
	}

}