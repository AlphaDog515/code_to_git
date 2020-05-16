package com.demo03;

/*
	给定两个二进制字符串，返回他们的和（用二进制表示）。	
	输入为非空字符串且只包含数字 1 和 0。	
	示例 1:	
		输入: a = "11", b = "1"
		输出: "100"
	示例 2:	
		输入: a = "1010", b = "1011"
		输出: "10101"

 */
// 包含二进制与10进制的互转
public class Num07_TwoBinarySum {
	public static void main(String[] args) {
		String a = "1010", b = "1011";
		Solution_07 solution = new Solution_07();
		String res = solution.addBinary(a, b);
		System.out.println(res);

//		decimalToBinary_2(13);
		
		int btd = binaryToDecimal(1010);
		System.out.println(btd);
	}

	public static void decimalToBinary_1(int n) {
		String res = Integer.toBinaryString(n);
		System.out.println(res);

		// 将字符串转换为数字进行打印，但不建议这么做，当为负数时，
		// int型的表示不了32的一个整数，另外，int的位数有大小，打印的值超出范围就会报错
		int in = Integer.parseInt(res);
		System.out.print(in + "\n");

	}

	public static void decimalToBinary_2(int n) {
		for (int i = 8; i >= 0; i--) {
			System.out.print(n >>> i & 1); // 000001010
		}
	}

	public static void decimalToBinary_3(int n) {
		int t = 0; // 用来记录位数
		int bin = 0; // 用来记录最后的二进制数
		int r = 0; // 用来存储余数

		while (n != 0) {
			r = n % 2;
			n = n / 2;
			// pow()方法，求取x的y次方，数值类型是double;
			bin += r * Math.pow(10, t);
			t++;
		}
		System.out.println(bin + "\n");
	}
	
	
	public static int binaryToDecimal(int binaryNumber) {
		int decimal = 0;
		int p = 0;
		while (true) {
			if (binaryNumber == 0) {
				break;
			} else {
				int temp = binaryNumber % 10;
				decimal += temp * Math.pow(2, p);
				binaryNumber = binaryNumber / 10;
				p++;
			}
		}
		return decimal;
	}

}

class Solution_07 {
	public String addBinary(String a, String b) {
		StringBuilder ans = new StringBuilder();
		int ca = 0; // 是否进一位
		for (int i = a.length() - 1, j = b.length() - 1; i >= 0 || j >= 0; i--, j--) {
			int sum = ca;
			sum += (i >= 0 ? a.charAt(i) - '0' : 0);
			// 获取字符串a对应的某一位的值: 当i<0是 sum+=0（向前补0） 否则取原值
			// '1'的char类型和'0'的char类型刚好相差为1
			sum += (j >= 0 ? b.charAt(j) - '0' : 0);
			ans.append(sum % 2); // 如果二者都为1 那么sum%2应该刚好为0 否则为1
			ca = sum / 2; // 如果二者都为1 那么ca 应该刚好为1 否则为0
		}
		ans.append(ca == 1 ? ca : "");// 判断最后一次计算是否有进位 有则在最前面加上1 否则原样输出
		return ans.reverse().toString();
	}
}