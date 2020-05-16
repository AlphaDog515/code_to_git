package com.demo06.recursion;

public class TestHanoi {
	// 成员变量又叫全局变量，作用域为整个类，有默认初始化值，优先级低；
	// 局部变量作用域所在方法，默认没有初始化值，优先级高；
	public static int c = 0; // 统计递归调用从次数

	public static void main(String[] args) {
		int c = hanoi(3, 'A', 'B', 'C');
		System.out.println("递归调用次数为:" + c);
	}

	/**
	 * @param n    共有N个盘子
	 * @param from 开始的柱子
	 * @param in   中间的柱子
	 * @param to   目标柱子 无论有多少个盘子，都认为只有两个。上面的所有盘子和最下面一个盘子。
	 */
//	public static void hanoi(int n, char from, char in, char to) {
//
//		// 只有一个盘子。
//		if (n == 1) {
//			System.out.println("第1个盘子从" + from + "移到" + to);
//
//			// 无论有多少个盘子，都认为只有两个。上面的所有盘子和最下面一个盘子。
//		} else {
//			// 移动上面所有的盘子到中间位置
//			hanoi(n - 1, from, to, in);
//			// 移动下面的盘子
//			System.out.println("第" + n + "个盘子从" + from + "移到" + to);
//			// 把上面的所有盘子从中间位置移到目标位置
//			hanoi(n - 1, in, from, to);
//		}
//
//	}

	public static int hanoi(int n, char from, char in, char to) {
		c++;
		// 只有一个盘子。
		if (n == 1) {
			System.out.println("第1个盘子从" + from + "移到" + to);
			// 无论有多少个盘子，都认为只有两个。上面的所有盘子和最下面一个盘子。
		} else {
			// 移动上面所有的盘子到中间位置
			hanoi(n - 1, from, to, in);
			// 移动下面的盘子
			System.out.println("第" + n + "个盘子从" + from + "移到" + to);
			// 把上面的所有盘子从中间位置移到目标位置
			hanoi(n - 1, in, from, to);
		}
		return c;
	}

}
