package com.demo03;


public class Num10_Sqrt {
/*
	实现 int sqrt(int x) 函数。	
	计算并返回 x 的平方根，其中 x 是非负整数。	
	由于返回类型是整数，结果只保留整数的部分，小数部分将被舍去。
	
	示例 1:	
	输入: 4
	输出: 2
	
	示例 2:	
	输入: 8
	输出: 2
	说明: 8 的平方根是 2.82842..., 由于返回类型是整数，小数部分将被舍去。

 */
	public static void main(String[] args) {
		int n = 100;
		int res = sqrt(n);
		System.out.println(res);
	}

	private static int sqrt(int n) {
		int l = 0;
		int r = n;

		while (l < r) {
			int mid = (l + r + 1) >>> 1;
			if (n < mid * mid) {
				r = mid - 1;
			} else {
				l = mid;
			}
		}
		return l;
	}

}

class Solution_10_1 {

	public int mySqrt(int x) {
		// 注意：针对特殊测试用例，例如 2147395599
		// 要把搜索的范围设置成长整型
		// 为了照顾到 0 把左边界设置为 0
		long left = 0;
		// # 为了照顾到 1 把右边界设置为 x // 2 + 1
		long right = x / 2 + 1;
		while (left < right) {
			// 注意：这里一定取右中位数，如果取左中位数，代码会进入死循环
			// long mid = left + (right - left + 1) / 2;
			long mid = (left + right + 1) >>> 1;
			long square = mid * mid;
			if (square > x) {
				right = mid - 1;
			} else {
				left = mid;
			}
		}
		// 因为一定存在，因此无需后处理
		return (int) left;
	}

}

class Solution_10_2 {

	public int mySqrt(int x) {
		if (x == 0) {
			return 0;
		}
		// 注意：针对特殊测试用例，例如 2147395599
		// 要把搜索的范围设置成长整型
		long left = 1;
		long right = x / 2;
		while (left < right) {
			// 注意：这里一定取右中位数，如果取左中位数，代码会进入死循环
			// long mid = left + (right - left + 1) / 2;
			long mid = (left + right + 1) >>> 1;
			long square = mid * mid;
			if (square > x) {
				right = mid - 1;
			} else {
				left = mid;
			}
		}
		// 因为一定存在，因此无需后处理
		return (int) left;
	}
}

class Solution_10_3 {

	public int mySqrt(int a) {
		long x = a;
		while (x * x > a) {
			x = (x + a / x) / 2;
		}
		return (int) x;
	}
}

