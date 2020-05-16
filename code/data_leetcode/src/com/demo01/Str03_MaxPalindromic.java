package com.demo01;

import java.util.ArrayList;
import java.util.List;

public class Str03_MaxPalindromic {
/*
	 给定一个字符串 s，找到 s 中最长的回文子串。
	 你可以假设 s 的最大长度为 1000。
	
	示例 1：
	输入: "babad"
	输出: "bab"
	注意: "aba" 也是一个有效答案。
	
	示例 2：
	输入: "cbbd"
	输出: "bb"
 */
	public static void main(String[] args) {
		String s = "baab";
		Solution_3_1 solution = new Solution_3_1();
		String res = solution.longestPalindrome(s);		

		System.out.println(res);

	}

/*	// 从第一个字符开始遍历，里面的while向两边扩散，需要考虑是奇数还是偶数，很麻烦
	private static List<String> maxPal(String s) {
		List<String> list = new ArrayList<>();
		for (int i = 0; i < s.length(); i++) {
			int j = i;
			while (j >= 0 && j <= Math.min(i, s.length() - 1 - i)) {
				StringBuilder sb = new StringBuilder();
				// sb.append(s.substring(i - j, i + j + 1));
				sb.append(s.substring(i - j, i + j + 2));
				// sb.append(s.substring(i - j - 1, i + j));
				j--;
				String temp = sb.toString();

				if (isPalindromic(temp)) {
					if (list.size() > 0 && list.get(0).length() == temp.length()) {
						list.add(temp);
					}

					if (list.size() > 0 && list.get(0).length() < temp.length()) {
						list.clear();
						list.add(temp);
					}

					if (list.size() == 0) {
						list.add(temp);
					}

				}
			}
		}
		return list;
	}

	private static boolean isPalindromic(String s) {
		boolean flag = true;
		int length = s.length();
		for (int i = 0; i < length / 2;) {
			if (s.charAt(i) == s.charAt(length - 1 - i)) {
				i++;
			} else {
				flag = false;
				break;
			}
		}
		return flag;
	}
}
*/
}

// 暴力解法
class Solution_3_1 {
	public boolean isPalindromic(String s) {
		int len = s.length();
		for (int i = 0; i < len / 2; i++) {
			if (s.charAt(i) != s.charAt(len - i - 1)) {
				return false;
			}
		}
		return true;
	}
	
	public String longestPalindrome(String s) {
		String ans = "";
		int max = 0;
		int len = s.length();
		for (int i = 0; i < len; i++)
			for (int j = i + 1; j <= len; j++) {
				String test = s.substring(i, j);
				if (isPalindromic(test) && test.length() > max) {
					ans = s.substring(i, j);
					max = Math.max(max, ans.length());
				}
			}
		return ans;
	}
}
// 时间复杂度：两层 for 循环 O(n²），for 循环里边判断是否为回文 O(n），所以时间复杂度为O(n³）。
// 空间复杂度：O(1），常数个变量。

/*
	解法 2: 最长公共子串(需要修正)
	根据回文串的定义，正着和反着读一样，那我们是不是把原来的字符串倒置了，
	然后找最长的公共子串就可以了。
	例如 S = "caba" ，S'= "abac"，最长公共子串是 "aba"，所以原字符串的最长回文串就是 "aba"。
	
	关于求最长公共子串（不是公共子序列），有很多方法，这里用动态规划的方法，
	整体思想就是，申请一个二维的数组初始化为 0，然后判断对应的字符是否相等，相等的话	
	arr[i][j]=arr[i-1][j-1]+1 。	// 与arr[i-1][j],arr[i][j-1]没有关系，很明显,一方加一个字符与结果没有关系
	当 i=0 或者 j = 0 的时候单独分析，字符相等的话 arr[i][j] 就赋为 1 。	
	arr[i][j] 保存的就是公共子串的长度。

 */

class Solution_3_2 {
	public String longestPalindrome(String s) {
		if (s.equals(""))
			return "";
		String origin = s;
		String reverse = new StringBuffer(s).reverse().toString(); // 字符串倒置
		int length = s.length();
		int[][] arr = new int[length][length];
		int maxLen = 0;
		int maxEnd = 0;
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				if (origin.charAt(i) == reverse.charAt(j)) {
					if (i == 0 || j == 0) {
						arr[i][j] = 1;
					} else {
						arr[i][j] = arr[i - 1][j - 1] + 1;
					}
				}
				if (arr[i][j] > maxLen) {
					maxLen = arr[i][j];
					maxEnd = i; // 以 i 位置结尾的字符
				}

			}
		}
		return s.substring(maxEnd - maxLen + 1, maxEnd + 1);
	}
}


class Solution_3_3 {
	public String longestPalindrome(String s) {
		if (s.equals(""))
			return "";
		String origin = s;
		String reverse = new StringBuffer(s).reverse().toString();
		int length = s.length();
		int[][] arr = new int[length][length];
		int maxLen = 0;
		int maxEnd = 0;
		for (int i = 0; i < length; i++)
			for (int j = 0; j < length; j++) {
				if (origin.charAt(i) == reverse.charAt(j)) {
					if (i == 0 || j == 0) {
						arr[i][j] = 1;
					} else {
						arr[i][j] = arr[i - 1][j - 1] + 1;
					}
				}
				/********** 修改的地方 *******************/
				if (arr[i][j] > maxLen) {
					int beforeRev = length - 1 - j;
					if (beforeRev + arr[i][j] - 1 == i) { // 判断下标是否对应
						maxLen = arr[i][j];
						maxEnd = i;
					}
					/*************************************/
				}
			}
		return s.substring(maxEnd - maxLen + 1, maxEnd + 1);
	}
}
// 时间复杂度：两层循环 O(n²)。
// 空间复杂度：一个二维数组 O(n²)。
// 空间复杂度其实可以再优化一下。

class Solution_3_4 {
	public String longestPalindrome(String s) {
		if (s.equals(""))
			return "";
		String origin = s;
		String reverse = new StringBuffer(s).reverse().toString();
		int length = s.length();
		int[] arr = new int[length];
		int maxLen = 0;
		int maxEnd = 0;
		for (int i = 0; i < length; i++)
			/************** 修改的地方 ***************************/
			for (int j = length - 1; j >= 0; j--) {
				/**************************************************/
				if (origin.charAt(i) == reverse.charAt(j)) {
					if (i == 0 || j == 0) {
						arr[j] = 1;
					} else {
						arr[j] = arr[j - 1] + 1;
					}
					/************** 修改的地方 ***************************/
					// 之前二维数组，每次用的是不同的列，所以不用置 0 。
				} else {
					arr[j] = 0;
				}
				/**************************************************/
				if (arr[j] > maxLen) {
					int beforeRev = length - 1 - j;
					if (beforeRev + arr[j] - 1 == i) {
						maxLen = arr[j];
						maxEnd = i;
					}

				}
			}
		return s.substring(maxEnd - maxLen + 1, maxEnd + 1);
	}
}
// 时间复杂度 O(n²)。
// 空间复杂度降为 O(n)。

class Solution_3_5 {
	public String longestPalindrome(String s) {
		int length = s.length();
		boolean[][] P = new boolean[length][length];
		int maxLen = 0;
		String maxPal = "";
		for (int len = 1; len <= length; len++) // 遍历所有的长度
			for (int start = 0; start < length; start++) {
				int end = start + len - 1;
				if (end >= length) // 下标已经越界，结束本次循环
					break;
				P[start][end] = (len == 1 || len == 2 || P[start + 1][end - 1]) && s.charAt(start) == s.charAt(end); 
				// 长度为1和2的单独判断下
				if (P[start][end] && len > maxLen) {
					maxPal = s.substring(start, end + 1);
				}
			}
		return maxPal;
	}
}
// 时间复杂度：两层循环 O(n²）。
// 空间复杂度：用二维数组 P保存每个子串的情况 O(n²)。

class Solution_3_6 {
	public String longestPalindrome(String s) {
		int n = s.length();
		String res = "";
		boolean[][] dp = new boolean[n][n];
		for (int i = n - 1; i >= 0; i--) {
			for (int j = i; j < n; j++) {
				dp[i][j] = s.charAt(i) == s.charAt(j) && (j - i < 2 || dp[i + 1][j - 1]); // j - i 代表长度减去 1
				if (dp[i][j] && j - i + 1 > res.length()) {
					res = s.substring(i, j + 1);
				}
			}
		}
		return res;
	}
}


class Solution_3_7 {
	public String longestPalindrome7(String s) {
		int n = s.length();
		String res = "";
		boolean[] P = new boolean[n];
		for (int i = n - 1; i >= 0; i--) {
			for (int j = n - 1; j >= i; j--) {
				P[j] = s.charAt(i) == s.charAt(j) && (j - i < 3 || P[j - 1]);
				if (P[j] && j - i + 1 > res.length()) {
					res = s.substring(i, j + 1);
				}
			}
		}
		return res;
	}
}
// 时间复杂度：不变 O(n²）,空间复杂度：降为O(n) 。


// 这个是扩展中心方法
// 枚举可能出现的回文子串的“中心位置”，从“中心位置”尝试尽可能扩散出去，得到一个回文串。
// 如果传入重合的索引编码，进行中心扩散，此时得到的回文子串的长度是奇数；
// 如果传入相邻的索引编码，进行中心扩散，此时得到的回文子串的长度是偶数。
class Solution_3_8 {

	public String longestPalindrome(String s) {
		int len = s.length();
		if (len < 2) {
			return s;
		}
		int maxLen = 1;
		String res = s.substring(0, 1);
		// 中心位置枚举到 len - 2 即可
		for (int i = 0; i < len - 1; i++) {
			String oddStr = centerSpread(s, i, i);
			String evenStr = centerSpread(s, i, i + 1);
			String maxLenStr = oddStr.length() > evenStr.length() ? oddStr : evenStr;
			if (maxLenStr.length() > maxLen) {
				maxLen = maxLenStr.length();
				res = maxLenStr;
			}
		}
		return res;
	}

	private String centerSpread(String s, int left, int right) {
		// left = right 的时候，此时回文中心是一个空隙，回文串的长度是奇数
		// right = left + 1 的时候，此时回文中心是任意一个字符，回文串的长度是偶数
		int len = s.length();
		int i = left;
		int j = right;
		while (i >= 0 && j < len) {
			if (s.charAt(i) == s.charAt(j)) {
				i--;
				j++;
			} else {
				break;
			}
		}
		// 这里要小心，跳出 while 循环时，恰好满足 s.charAt(i) != s.charAt(j)，因此不能取 i，不能取 j
		return s.substring(i + 1, j);
	}
}

// 时间复杂度：O(n^2),空间复杂度：O(1)，只使用到常数个临时变量，与字符串长度无关。


class Solution_3_9 {

	public String longestPalindrome(String s) {
		int len = s.length();
		if (len < 2) {
			return s;
		}
		String str = addBoundaries(s, '#');
		int sLen = 2 * len + 1;
		int maxLen = 1;

		int start = 0;
		for (int i = 0; i < sLen; i++) {
			int curLen = centerSpread(str, i);
			if (curLen > maxLen) {
				maxLen = curLen;
				start = (i - maxLen) / 2;
			}
		}
		return s.substring(start, start + maxLen);
	}

	private int centerSpread(String s, int center) {
		// left = right 的时候，此时回文中心是一个空隙，回文串的长度是奇数
		// right = left + 1 的时候，此时回文中心是任意一个字符，回文串的长度是偶数
		int len = s.length();
		int i = center - 1;
		int j = center + 1;
		int step = 0;
		while (i >= 0 && j < len && s.charAt(i) == s.charAt(j)) {
			i--;
			j++;
			step++;
		}
		return step;
	}

	/**
	 * 创建预处理字符串
	 * @param s 原始字符串
	 * @param divide 分隔字符
	 * @return 使用分隔字符处理以后得到的字符串
	 */
	private String addBoundaries(String s, char divide) {
		int len = s.length();
		if (len == 0) {
			return "";
		}
		if (s.indexOf(divide) != -1) {
			throw new IllegalArgumentException("参数错误，您传递的分割字符，在输入字符串中存在！");
		}
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < len; i++) {
			stringBuilder.append(divide);
			stringBuilder.append(s.charAt(i));
		}
		stringBuilder.append(divide);
		return stringBuilder.toString();
	}

}

