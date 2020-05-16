package com.demo03;

public class Num04_Reverse {
	public static void main(String[] args) {
		Solution_04 solution = new Solution_04();
		int x = -2147483648;
		int res = solution.reverse(x);
		System.out.println(res);

	}
}

// 给出一个 32 位的有符号整数，你需要将这个整数中每位上的数字进行反转。
// 

/*
	示例 1:
		输入: 123
		输出: 321
	示例 2:	
		输入: -123
		输出: -321
	示例 3:	
		输入: 120
		输出: 21
	注意:
	假设我们的环境只能存储得下 32 位的有符号整数，则其数值范围为 [−2^31,  2^31−1]。
	请根据这个假设，如果反转后整数溢出那么就返回 0。
*/

class Solution_04 {
	public int reverse(int x) {
		int rev = 0;
		while (x != 0) {
			int pop = x % 10;
			x = x / 10;
			if (rev > Integer.MAX_VALUE / 10 || (rev == Integer.MAX_VALUE / 10 && pop > Integer.MAX_VALUE % 10)) {
				rev = 0;
				break;
			} else if (rev < Integer.MIN_VALUE / 10 || (rev == Integer.MIN_VALUE / 10 && x < Integer.MIN_VALUE % 10)) {
				rev = 0;
				break;
			}
			rev = rev * 10 + pop;
		}
		return rev;
	}
}