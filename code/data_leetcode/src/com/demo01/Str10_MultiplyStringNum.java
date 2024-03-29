package com.demo01;

public class Str10_MultiplyStringNum {

}

/*
 
	给定两个以字符串形式表示的非负整数 num1 和 num2，返回 num1 和 num2 的乘积，它们的乘积也表示为字符串形式。	
	示例 1:	
	输入: num1 = "2", num2 = "3"
	输出: "6"
	示例 2:	
	输入: num1 = "123", num2 = "456"
	输出: "56088"
	说明：	
	num1 和 num2 的长度小于110。
	num1 和 num2 只包含数字 0-9。
	num1 和 num2 均不以零开头，除非是数字 0 本身。
	不能使用任何标准库的大数类型（比如 BigInteger）或直接将输入转换为整数来处理。
	
	基础版思路：
	遍历 num2 每一位与 num1 进行相乘，将每一步的结果进行累加。
	注意：num2 除了第一位的其他位与 num1 运算的结果需要补0
	复杂度分析
	时间复杂度：O(M*N)。M,N分别为 num1 和 num2 的长度。
	空间复杂度：O(M+N)。用于存储计算结果。
	
	优化版思路：	
	该算法是通过两数相乘时，乘数某位与被乘数某位相乘，与产生结果的位置的规律来完成。具体规律如下：	
	乘数 num1 位数为 M，被乘数 num2 位数为 N， num1 x num2 结果 res 最大总位数为 M+N
	num1[i] x num2[j] 的结果为 tmp(位数为两位，"0x","xy"的形式)，
		其第一位位于 res[i+j]，第二位位于 res[i+j+1]。

 
 */

class Solution_10_1 {
	/**
	 * 	计算形式 
	 * 		num1 
	 * 	  x 
	 * 		num2 
	 * 	   ------ 
	 *     result
	 */
	public String multiply(String num1, String num2) {
		if (num1.equals("0") || num2.equals("0")) {
			return "0";
		}
		// 保存计算结果
		String res = "0";

		// num2 逐位与 num1 相乘
		for (int i = num2.length() - 1; i >= 0; i--) {
			int carry = 0;
			// 保存 num2 第i位数字与 num1 相乘的结果
			StringBuilder temp = new StringBuilder();
			// 补 0
			for (int j = 0; j < num2.length() - 1 - i; j++) {
				temp.append(0);
			}
			int n2 = num2.charAt(i) - '0';

			// num2 的第 i 位数字 n2 与 num1 相乘
			for (int j = num1.length() - 1; j >= 0 || carry != 0; j--) {
				int n1 = j < 0 ? 0 : num1.charAt(j) - '0';
				int product = (n1 * n2 + carry) % 10;
				temp.append(product);
				carry = (n1 * n2 + carry) / 10;
			}
			// 将当前结果与新计算的结果求和作为新的结果
			res = addStrings(res, temp.reverse().toString());
		}
		return res;
	}

	/**
	 * 对两个字符串数字进行相加，返回字符串形式的和
	 */
	public String addStrings(String num1, String num2) {
		StringBuilder builder = new StringBuilder();
		int carry = 0;
		for (int i = num1.length() - 1, j = num2.length() - 1; i >= 0 || j >= 0 || carry != 0; i--, j--) {
			int x = i < 0 ? 0 : num1.charAt(i) - '0';
			int y = j < 0 ? 0 : num2.charAt(j) - '0';
			int sum = (x + y + carry) % 10;
			builder.append(sum);
			carry = (x + y + carry) / 10;
		}
		return builder.reverse().toString();
	}
}


class Solution_10_2 {
	public String multiply(String num1, String num2) {
		if (num1.equals("0") || num2.equals("0")) {
			return "0";
		}
		int[] res = new int[num1.length() + num2.length()];
		for (int i = num1.length() - 1; i >= 0; i--) {
			int n1 = num1.charAt(i) - '0';
			for (int j = num2.length() - 1; j >= 0; j--) {
				int n2 = num2.charAt(j) - '0';
				int sum = (res[i + j + 1] + n1 * n2);
				res[i + j + 1] = sum % 10;
				res[i + j] += sum / 10;
			}
		}

		StringBuilder result = new StringBuilder();
		for (int i = 0; i < res.length; i++) {
			if (i == 0 && res[i] == 0)
				continue;
			result.append(res[i]);
		}
		return result.toString();
	}
}

//	复杂度分析
//	时间复杂度：O(M*N)。M,N分别为 num1 和 num2 的长度。
//	空间复杂度：O(M+N)。用于存储计算结果。

