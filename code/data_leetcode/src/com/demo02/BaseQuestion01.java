package com.demo02;

import java.util.Random;
import java.util.Scanner;

public class BaseQuestion01 {
	public static void main(String[] args) {
		BaseQuestion01 bq = new BaseQuestion01();
//		bq.result01();
//		bq.result02();
//		bq.result03();
//		bq.result04();
//		bq.result05();
//		bq.result06();
//		bq.result07();
//		bq.result08();
//		bq.result09();
//		bq.result10();
//		bq.result11();
//		bq.result12();
		bq.result13();
//		bq.result14();
//		bq.result15();
	}

	// 求一元二次方程的根
	public void result01() {
		Scanner sc = new Scanner(System.in);
		System.out.println("输入2次方的系数");
		int a = sc.nextInt();
		System.out.println("输入1次方的系数");
		int b = sc.nextInt();
		System.out.println("输入0次方的系数");
		int c = sc.nextInt();
		sc.close();
		if ((b * b - 4 * a * c) < 0) { // 判断方程是否有解
			System.out.println("方程无解！");
			return;
		} else {
			System.out.println("方程有解！");
		}
		double x1 = (-b + Math.sqrt(b * b - 4 * a * c)) / 2 * a;
		double x2 = (-b - Math.sqrt(b * b - 4 * a * c)) / 2 * a;
		System.out.println("根分别是 " + x1 + "\t" + x2);
	}

	// 输入一个字符，判断它是否为小写字母，如果是，将它转换成大写字母，否则，不转换。
	public void result02() {
		// 小写字母的ascll值为97-122
		// 大写字母的ascll值为65-90
		System.out.println("请输入一个字母：");
		Scanner input = new Scanner(System.in);
		char c = input.next().charAt(0);
		if (c >= 97 && c <= 122) { // 判断是否是小写字母
			System.err.println("该字母是小写字母");
			c = (char) (c - 32); // 如果是小写字母则 将其转换成大写字母
			System.err.println("转换之后的大写字母是：" + c);
		} else {
			System.out.println("该字母不是小写字母！");
		}
	}

	public void result03() {
		int a;
		int b;
		int c;
		System.out.println("请输入三个正整数：");
		Scanner in = new Scanner(System.in);
		a = in.nextInt();
		b = in.nextInt();
		c = in.nextInt();

		if (a <= 0 || b <= 0 || c <= 0) {
			System.out.println("输入的必须是正整数！");
		}
		if ((a + b) > c && (a + c) > b && (b + c) > a) {
			System.out.println("能构成三角形！");
		} else {
			System.out.println("不能构成三角形！");
		}
	}

	// 输入年月日判断是第几天？
	public void result04() {
		Scanner sc = new Scanner(System.in);
		System.out.print("年");
		int year = sc.nextInt();
		System.out.print("月");
		int month = sc.nextInt();
		System.out.print("日");
		int day = sc.nextInt();
		int days = 0;
		switch (month) {
		case 12:
			days += 30;
		case 11:
			days += 31;
		case 10:
			days += 30;
		case 9:
			days += 31;
		case 8:
			days += 31;
		case 7:
			days += 30;
		case 6:
			days += 31;
		case 5:
			days += 30;
		case 4:
			days += 31;
		case 3:
			if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
				days += 29;
			} else {
				days += 28;
			}
		case 2:
			days += 31;
		case 1:
			days += day;
		}
		System.out.print("第" + days + "天");
	}

	// 判断0-99999位数
	public void result05() {
		Scanner sc = new Scanner(System.in);
		System.out.print("请输入一个0~99999 之间的任意数");
		int number = sc.nextInt();
		sc.close();
		if (number / 10000 >= 1 && number / 10000 < 10) {
			System.out.println(number + "\t是5位数");
		} else if (number / 1000 >= 1) {
			System.out.println(number + "\t是4位数");
		} else if (number / 100 >= 1) {
			System.out.println(number + "\t是3位数");
		} else if (number / 10 >= 1) {
			System.out.println(number + "\t是2位数");
		} else if (number / 1 >= 1) {
			System.out.println(number + "\t是1位数");
		}
	}

	public void result06() {
		Scanner sc = new Scanner(System.in);
		System.out.println("请输入学生成绩：");
		int a = sc.nextInt();
		if (a >= 90 && a <= 100) {
			System.out.println("该学生的成绩是" + a + "\t成绩优秀");
		} else if (a >= 80 && a < 90) {
			System.out.println("该学生的成绩是" + a + "\t成绩良好");
		} else if (a >= 70 && a < 80) {
			System.out.println("该学生的成绩是" + a + "\t成绩中等");
		} else if (a >= 60 && a < 70) {
			System.out.println("该学生的成绩是" + a + "\t成绩及格");
		} else {
			System.out.println("该学生的成绩是" + a + "\t成绩不及格");
		}
	}

	// 将数字倒着输出
	public void result07() {
		Scanner sc = new Scanner(System.in);
		System.out.println("请输入一个整数：");
		int read = sc.nextInt();
		// 方法一 reverse()API
		System.out.println("方法一：");
		StringBuilder sb = new StringBuilder(String.valueOf(read));
		System.out.println(sb.reverse());
		// 方法二 将字符串转换成字符数组，反序输出
		String str = read + "";
		char help[] = str.toCharArray();
		String temp = "";
		for (int a = help.length - 1; a >= 0; a--) {
			temp = temp + help[a];
		}
		System.out.println("方法二：");
		System.out.println(temp);
	}

	public void result08() {
		// 用while循环，计算1~200之间所有3的倍数之和。
		int a = 1;
		int sum = 0;
		while (a <= 200) {
			if (a % 3 == 0) {
				sum = sum + a;
			}
			a++;
		}
		System.out.println("1~200之间所有3的倍数之和为:" + sum);
	}

	// 200-500之间的素数
	public void result09() {
		int num = 200;
		while (num <= 500) {
			boolean tag = true; // 素数标记
			for (int d = 2; d <= Math.sqrt(num); d++) {
				if (num % d == 0) {
					tag = false;
					break;
				}
			}
			if (tag) { // 如果是素数
				System.out.println(num);
			}
			num++;
		}
	}

	/*
	 * 编写程序解决"百钱买百鸡"问题。 
	 * 公鸡五钱一只，母鸡三钱一只， 小鸡 一钱三只， 现有百钱欲买百鸡，共有多少种买法？
	 */
	public void result10() {
		for (int g = 0; g <= 20; g++) {
			for (int m = 0; m <= 33; m++) {
				for (int x = 0; x <= 100 - g - m; x++) {
					if (x % 3 == 0 && 5 * g + m * 3 + x / 3 == 100 && g + m + x == 100) {
						System.out.println("公鸡" + g + "只母鸡" + m + "只小鸡" + x + "只");
					}
				}
			}
		}
	}

	// #
	// # # #
	// # # # # #
	// # # # # # # #
	// # # # # # # # # #
	public void result11() {
		int aa = -1;
		for (int a = 0; a < 5; a++) {
			aa += 2;
			for (int b = 1; b <= aa; b++) {
				System.out.print("#");
			}
			System.out.println();
		}

	}

	public void result12() {
		/*
		 * 验证"鬼谷猜想"：对任意自然数，若是奇数， 就对它乘以3再加1； 
		 * 若是偶数，就对它除以2， 这样得到一个新数， 再按上述计算规则进行计算，
		 * 一直进行下去，最终必然得到1。
		 */
		int num = 10;
		Random rd = new Random();
		// Integer.MAX_VALUE为最大的整数
//		num = 1 + rd.nextInt(Integer.MAX_VALUE);// 产生数的范围-2[31]----2[31]-1
		// System.err.println(rd.nextInt(100));//产生数>=0且<100
		System.out.println("原来的数为：" + num);
		while (num != 1) {
			System.out.println("产生的新数是：" + num);
			if (num % 2 == 0) {
				// 偶数
				num = num / 2;
			} else {
				num = num * 3 + 1;
			}
		}
		System.out.println(num);
	}

	public boolean help01(int num) {
		int sum = 0;
		// 判断一个整数是不是一个完全数
		for (int d = num - 1; d >= 1; d--) {
			if (num % d == 0) {
				sum += d;
			}
		}
		return sum == num;
	}

	public void result13() {
		/*
		 * 编程求1~10000之间的所有“完全数”， 完全数是该数的所有因子之和等于该数的数。 
		 * 例如，6的因子有1、2、3， 且6=1+2+3，所以6是完全数
		 * 28=14+7+4+2+1
		 */
		for (int a = 1; a <= 1000; a++) {
			int num = a;
			if (help01(num)) {
				System.out.println(num);// 6 28 496
			}
		}

	}

	public void help02(int num) {
		int b = num / 100; // 百位数
		int s = num % 100 / 10; // 十位数
		int g = num % 10; // 个位数
		if (num % 9 == 0) {
			System.out.println(num + "能被9整除");
			if ((b + s + g) % 9 == 0) {
				System.out.println("同时" + num + "的各个位数之和也能被9整除");
			} else {
				System.out.println("但是" + num + "的各个位数之和不能被9整除");
			}
		} else
			System.out.println("next test!");

	}

	// 一个整数的各位数字之和能被 9 整除，则该数也能被 9 整除。
	// 编程验证给 定的整数能否被 9 整除。
	public void result14() {
		Random rd = new Random();
		int shu = 10 + rd.nextInt(90);
		shu = shu * 9;
		help02(shu);
	}

	public void result15() {
		System.out.println("请输入一个整数：");
		Scanner sc = new Scanner(System.in);
		int num = sc.nextInt();
		sc.close();
		System.out.println(num + "的质因数有：");
		for (int i = 2; i < num; i++) {
			while (num % i == 0) {
				num /= i;
				System.out.print(i + ",");
			}
		}
		System.out.print(num);
	}

}
