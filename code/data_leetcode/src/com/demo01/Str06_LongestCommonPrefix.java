package com.demo01;

public class Str06_LongestCommonPrefix {
	public static void main(String[] args) {
		String[] s = { "flower", "flow", "flight" };

		Solution_6_5 solution = new Solution_6_5();
		String res = solution.longestCommonPrefix(s[0], s);
		System.out.println(res);
	}
}

/*
	编写一个函数来查找字符串数组中的最长公共前缀。	
	如果不存在公共前缀，返回空字符串 ""。	
	示例 1:	
	输入: ["flower","flow","flight"]
	输出: "fl"
	示例 2:	
	输入: ["dog","racecar","car"]
	输出: ""
	解释: 输入不存在公共前缀。
	说明: 所有输入只包含小写字母 a-z 。

*/

class Solution_6_0 {
	public String findMaxPrefix(String s1, String s2) {
		int len = Math.min(s1.length(), s2.length());
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len;) {
			if (s1.charAt(i) == s2.charAt(i)) {
				sb.append(s1.charAt(i));
				i++;
			} else {
				break;
			}
		}
		return sb.toString();
	}

	public String findKMaxPrefix(String[] s) {
		String temp = s[0];
		for (int i = 1; i < s.length; i++) {
			temp = findMaxPrefix(temp, s[i]);
		}
		return temp;
	}
}


class Solution_6_1 {
	
	public String longestCommonPrefix(String[] strs) {
		if (strs.length == 0)
			return "";
		String prefix = strs[0];
		for (int i = 1; i < strs.length; i++)
			while (strs[i].indexOf(prefix) != 0) {
				// s1.indexOf(s2) 返回s2在s1中第一次出现的位置,从0开始,没有返回-1
				// s2.substring(0, s2.length()) == s2 true
				prefix = prefix.substring(0, prefix.length() - 1);
				if (prefix.isEmpty())
					return "";
			}
		return prefix;
	}
}
// 时间复杂度：O(S)，S 是所有字符串中字符数量的总和。
// 空间复杂度：O(1)，我们只需要使用常数级别的额外空间。

class Solution_6_2 {
	public String longestCommonPrefix(String[] strs) {
		if (strs == null || strs.length == 0)
			return "";
		for (int i = 0; i < strs[0].length(); i++) {
			char c = strs[0].charAt(i);
			for (int j = 1; j < strs.length; j++) {
				if (i == strs[j].length() || strs[j].charAt(i) != c)
					return strs[0].substring(0, i);
			}
		}
		return strs[0];
	}
}

// 分治算法  lcs(s) = lcs[ lcs(左) , lcs(s右)]
class Solution_6_3 {
	public String longestCommonPrefix(String[] strs) {
		if (strs == null || strs.length == 0)
			return "";
		return longestCommonPrefix(strs, 0, strs.length - 1);
	}

	private String longestCommonPrefix(String[] strs, int l, int r) {
		if (l == r) {
			return strs[l];
		} else {
			int mid = (l + r) / 2;
			String lcpLeft = longestCommonPrefix(strs, l, mid);
			String lcpRight = longestCommonPrefix(strs, mid + 1, r);
			return commonPrefix(lcpLeft, lcpRight);
		}
	}

	String commonPrefix(String left, String right) {
		int min = Math.min(left.length(), right.length());
		for (int i = 0; i < min; i++) {
			if (left.charAt(i) != right.charAt(i))
				return left.substring(0, i);
		}
		return left.substring(0, min);
	}
}

// 二分查找法求解
class Solution_6_4 {
	public String longestCommonPrefix(String[] strs) {
		if (strs == null || strs.length == 0)
			return "";
		int minLen = Integer.MAX_VALUE;
		for (String str : strs)
			minLen = Math.min(minLen, str.length());
		int low = 1;
		int high = minLen;
		while (low <= high) {
			int middle = (low + high) / 2;
			if (isCommonPrefix(strs, middle))
				low = middle + 1;
			else
				high = middle - 1;
		}
		return strs[0].substring(0, (low + high) / 2);
	}

	private boolean isCommonPrefix(String[] strs, int len) {
		String str1 = strs[0].substring(0, len);
		for (int i = 1; i < strs.length; i++)
			if (!strs[i].startsWith(str1))
				return false;
		return true;
	}
}

class Solution_6_5 {
	public String longestCommonPrefix(String q, String[] strs) {
		if (strs == null || strs.length == 0)
			return "";
		if (strs.length == 1)
			return strs[0];
		Trie trie = new Trie();
		for (int i = 1; i < strs.length; i++) {
			trie.insert(strs[i]);
		}
		return trie.searchLongestPrefix(q);
	}

}

//	复杂度分析
//	最坏情况下查询字符串 q 的长度为 m 并且它与数组中 n 个字符串均相同。
//	时间复杂度：预处理过程 O(S)，其中 S 数组里所有字符串中字符数量的总和，
//	长公共前缀查询操作的复杂度为 O(m)。
//	建立字典树的时间复杂度为 O(S)。在字典树中查找字符串 q 的最长公共前缀在最坏情况下需要O(m) 的时间。
//	空间复杂度：O(S)，我们只需要使用额外的 S 空间建立字典树。

