package com.demo03;

public class Num19_FindTwoArray {
	public static void main(String[] args) {
		int[][] arr = { { 1, 4, 7, 11, 15 }, { 2, 5, 8, 12, 19 }, { 3, 6, 9, 16, 22 }, { 10, 13, 14, 17, 24 },
				{ 18, 21, 23, 26, 30 } };
		Solution_19 solution = new Solution_19();
		boolean res = solution.findNum(16, arr);
		System.out.println(res);
	}
}

class Solution_19 {
	public boolean findNum(int target, int[][] arr) {
		// 边界条件判断
		if (arr == null || arr.length == 0 || arr[0] == null || arr[0].length == 0) {
			return false;
		}

		// 获取矩阵的行数m，列数n
		int m = arr.length, n = arr[0].length;
		// 初始化一开始元素的位置，设置为矩阵的右上角元素
		int i = 0, j = n - 1;
		while (i < m && j >= 0) {
			// 如果目标值小于右上角的数字，列下标减一
			if (target < arr[i][j])
				j--;
			// 如果目标值大于右上角的数字，行下标加一
			else if (target > arr[i][j])
				i++;
			// 相等返回true
			else
				return true;
		}
		// 循环结束没有找到，返回false
		return false;

	}
}