package com.demo01;

public class Str04_MaxPalSeq {
	/*
	 
	 给定一个字符串s，找到其中最长的回文子序列。可以假设s的最大长度为1000。
	示例 1:
		输入: "bbbab"
		输出: 4
		一个可能的最长回文子序列为 "bbbb"。

	示例 2:
		输入: "cbbd"
	 	输出: 2
		一个可能的最长回文子序列为 "bb"。	 
	 
	 	状态
		f[i][j] 表示 s 的第 i 个字符到第 j 个字符组成的子串中，最长的回文序列长度是多少。
		
		转移方程
		如果 s 的第 i 个字符和第 j 个字符相同的话		
		f[i][j] = f[i + 1][j - 1] + 2
		
		如果 s 的第 i 个字符和第 j 个字符不同的话		
		f[i][j] = max(f[i + 1][j], f[i][j - 1])
		
		然后注意遍历顺序，i 从最后一个字符开始往前遍历，j 从 i + 1 开始往后遍历，这样可以保证每个子问题都已经算好了。
		
		初始化
		f[i][i] = 1 单个字符的最长回文序列是 1
		
		结果
		f[0][n - 1]

	 */
	public static void main(String[] args) {
		String s = "bbbab";
		Solution_04 solution = new Solution_04();
		int res = solution.longestPalindromeSubseq(s);
		System.out.println(res);
	}
}

class Solution_04 {
	public int longestPalindromeSubseq(String s) {
		int n = s.length();
		int[][] f = new int[n][n];
		for (int i = n - 1; i >= 0; i--) {
			f[i][i] = 1;
			for (int j = i + 1; j < n; j++) {
				if (s.charAt(i) == s.charAt(j)) {
					f[i][j] = f[i + 1][j - 1] + 2;
				} else {
					f[i][j] = Math.max(f[i + 1][j], f[i][j - 1]);
				}
			}
		}
		return f[0][n - 1];
	}
}
