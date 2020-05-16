package com.demo02;

public class BaseQuestion02 {

	public static void main(String args[]) {
		gcdlcm a = new gcdlcm();
		System.out.println("两个数的最大公约数是：" + a.gcd(10, 16));
		System.out.println("两个数的最小公倍数是：" + a.lcm(16, 10));
	}
}

class gcdlcm {
	int gcd(int m, int n) {
		if (m < n) { // 大的放前面
			int temp = n;
			n = m;
			m = temp;
		}
		if (m % n == 0) {
			return n;
		} else {
			m %= n;
			return gcd(m, n);

		}
	}

	int lcm(int m, int n) {
		int i = 1;
		if (m < n) {
			int temp = n;
			n = m;
			m = temp;
		}
		int lcm = m;
		while (lcm % n != 0) {
			lcm = m * i;
			i++;
		}
		return lcm;
	}
}