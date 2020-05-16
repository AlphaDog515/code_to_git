package com.demo01;

public class Str03_MaxPalindromic_Manacher {
	public static void main(String[] args) {
		PalindromeString solution = new PalindromeString();
		String[] testStr = new String[] { "cdedbabdedf", "cbdedbabdedf", "aaba", "aaaa" };
		for (String s : testStr) {
			System.out.println(String.format("原字符串：%s", s));
			System.out.println(String.format("最长回文串：%s", solution.findLongestPalindromeString(s)));
			System.out.println();
		}

	}
}

class Solution_1 {

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
				start = (i - maxLen) / 2; // 需要细品
			}
		}
		return s.substring(start, start + maxLen);
	}

	// 返回可以扩散的步数 0 1 2 3 4...
	private int centerSpread(String s, int center) {
		// i >= 0 && j < sLen 保证不越界
		// str.charAt(i) == str.charAt(j) 表示可以扩散 1 次
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
	 *
	 * @param s      原始字符串
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

class Solution_2_Manacher {

	public String longestPalindrome(String s) {
		// 特判
		int len = s.length();
		if (len < 2) {
			return s;
		}

		// 得到预处理字符串
		String str = addBoundaries(s, '#');
		// 新字符串的长度
		int sLen = 2 * len + 1;

		// 数组 p 记录了扫描过的回文子串的信息
		int[] p = new int[sLen];

		// 双指针，它们是一一对应的，须同时更新
		int maxRight = 0;
		int center = 0;

		// 原始字符串的最长回文子串的长度
		int maxLen = 1;

		// 原始字符串的最长回文子串的起始位置，与 maxLen 必须同时更新
		int start = 0;

		for (int i = 0; i < sLen; i++) {
			if (i < maxRight) {
				int mirror = 2 * center - i;
				// 这一行代码是 Manacher 算法的关键所在，要结合图形来理解
				p[i] = Math.min(maxRight - i, p[mirror]);
			}

			// 下一次尝试扩散的左右起点，能扩散的步数直接加到 p[i] 中
			int left = i - (1 + p[i]);
			int right = i + (1 + p[i]);

			// left >= 0 && right < sLen 保证不越界
			// str.charAt(left) == str.charAt(right) 表示可以扩散 1 次
			while (left >= 0 && right < sLen && str.charAt(left) == str.charAt(right)) {
				p[i]++;
				left--;
				right++;
			}
			// 根据 maxRight 的定义，它是遍历过的 i 的 i + p[i] 的最大者
			// 如果 maxRight 的值越大，进入上面 i < maxRight 的判断的可能性就越大，
			// 这样就可以重复利用之前判断过的回文信息了
			if (i + p[i] > maxRight) {
				// maxRight 和 center 需要同时更新
				maxRight = i + p[i];
				center = i;
			}
			if (p[i] > maxLen) {
				// 记录最长回文子串的长度和相应它在原始字符串中的起点
				maxLen = p[i];
				start = (i - maxLen) / 2;
			}
		}
		return s.substring(start, start + maxLen);
	}

	/**
	 * 创建预处理字符串
	 *
	 * @param s      原始字符串
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
		StringBuilder stringBuilder = new StringBuilder(); // 线程不安全
		for (int i = 0; i < len; i++) {
			stringBuilder.append(divide);
			stringBuilder.append(s.charAt(i));
		}
		stringBuilder.append(divide);
		return stringBuilder.toString();
	}
}

// B站视频算法
class PalindromeString {
	// 预处理字符串，在两个字符之间添加#
	private String preHandlerString(String s) {
		StringBuffer sb = new StringBuffer();
		int len = s.length();
		sb.append("#");
		for (int i = 0; i < len; i++) {
			sb.append(s.charAt(i));
			sb.append("#");
		}
		return sb.toString();
	}

	// 寻找最长回文子串
	public String findLongestPalindromeString(String s) {
		String str = preHandlerString(s); // 预处理字符串
		int len = str.length();
		int rightSide = 0; // 右边界
		int rightSideCenter = 0;// 右边界对应的回文字符串中心
		int[] halfLenArr = new int[len]; // 保存以每个字符为中心的回文串的一半（向下取整）
		int center = 0;// 记录回文中心
		int longestHalf = 0; // 记录最长回文长度

		for (int i = 0; i < len; i++) {
			boolean needCalc = true;// 是否需要中心扩展
			if (rightSide > i) { // 如果在右边界的覆盖范围之内
				int leftCenter = 2 * rightSideCenter - i;// 计算相对rightSideCenter的对称位置
				halfLenArr[i] = halfLenArr[leftCenter]; // 根据回文性质得到的结论
				if (i + halfLenArr[i] > rightSide) { // 如果超多了右边界，进行调整
					halfLenArr[i] = rightSide - i;
				}
				// 如果根据计算得出的最长回文子串小于右边边界，那么不需要扩展了
				if (i + halfLenArr[leftCenter] < rightSide) {
					needCalc = false;// 直接推出结论
				}
			}
			// 中心扩展
			if (needCalc) {
				while (i - 1 - halfLenArr[i] >= 0 && i + 1 + halfLenArr[i] < len) {
					if (str.charAt(i + 1 + halfLenArr[i]) == str.charAt(i - 1 - halfLenArr[i])) {
						halfLenArr[i]++;
					} else {
						break;
					}
				}
				rightSide = i + halfLenArr[i]; // 更新右边界及中心
				rightSideCenter = i;
				if (halfLenArr[i] > longestHalf) { // 记录最长回文串
					center = i;
					longestHalf = halfLenArr[i];
				}
			}
		}
		// 去掉之前添加的#
		StringBuffer sb = new StringBuffer();
		for (int i = center - longestHalf + 1; i <= center + longestHalf; i += 2) {
			sb.append(str.charAt(i));
		}
		return sb.toString();
	}
}
