package com.demo04;

// 代码不对，有bug
public class ChangeMoney {
	public static void main(String[] args) {
		change(10, 2, 8, 105);
	}

	// 另外一种思路，看余数是奇数还是偶数，直接case，0 2 4 6 8 1 3 5 7 9
	public static void change(int i, int j, int k, int n) {
		System.out.printf("你有%d张10元,%d张5元,%d张2元需要支付%d元.", i, j, k, n);
		System.out.println();

	}
}