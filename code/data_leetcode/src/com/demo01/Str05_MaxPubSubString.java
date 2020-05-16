package com.demo01;

public class Str05_MaxPubSubString {
	public static void main(String[] args) {
//		Solution_5_1 solution = new Solution_5_1();
		Solution_5_2 solution = new Solution_5_2();
		solution.findMaxSubString("abcd", "bcd");
//		System.out.println(res);
		
	}
}

// 最长公共子序列
class Solution_5_1 {
	int count = 0;
	public int findMaxSubString(String s1, String s2) {
		count ++;
		if (s1.length() == 0 || s2.length() == 0) { // s1或者s2有一个为0
			return 0;
		}
		if (s1.charAt(0) == s2.charAt(0)) {
			return findMaxSubString(s1.substring(1), s2.substring(1)) + 1; // 取接下来的
		} else {
			return Math.max(findMaxSubString(s1.substring(1), s2), findMaxSubString(s1, s2.substring(1)));
			// 比较两个中更大的
		}
	}
}

class Solution_5_2 {
	public void findMaxSubString(String a, String b) {

		if (a == null || b == null) {
			return;
		}
		char[] charA = a.toCharArray();
		char[] charB = b.toCharArray();
		int lengthA = charA.length;
		int lengthB = charB.length;

		int[][] arr = new int[lengthA + 1][lengthB + 1];
		for (int i = 1; i <= lengthA; i++) {
			for (int j = 1; j <= lengthB; j++) {
				if (charA[i - 1] == charB[j - 1]) {
					arr[i][j] = arr[i - 1][j - 1] + 1;
				}
			}
		}

		int maxLength = 0;
		int end = 0;
		for (int i = 0; i <= lengthA; i++) {
			for (int j = 0; j <= lengthB; j++) {
				if (maxLength < arr[i][j]) {
					end = i;
					maxLength = arr[i][j];
				}
			}
		}

		System.out.println("最大长度为:" + maxLength);
		System.out.println("字符串为:" + a.subSequence(end - maxLength, end));
	}

}