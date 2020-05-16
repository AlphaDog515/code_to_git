package com.demo01;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

/**
 * 标签：滑动窗口 
 * 暴力解法时间复杂度较高，会达到 O(n^2)，故而采取滑动窗口的方法降低时间复杂度 
 * 定义一个 map 数据结构存储 (k,v)，其中 key 值为字符，value 值为字符位置 +1，
 * 加 1表示从字符位置后一个才开始不重复 
 * 我们定义不重复子串的开始位置为start，结束位置为 end 
 * 随着 end 不断遍历向后，会遇到与 [start, end] 区间内字符相同的情况，
 * 此时将字符作为 key 值，获取其 value 值，并更新 start，此时 [start, end] 区间内不存在重复字符 
 * 无论是否更新 start，都会更新其 map 数据结构和结果ans。 
 * 时间复杂度：O(n)
 * 
 */

// 给定一个字符串，请你找出其中不含有重复字符的最长子串的长度。
// 示例 1:
// 输入: "abcabcbb"
// 输出: 3
// 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。

public class Str01_SlidingWindow {
	public static void main(String[] args) {
		String s = "abcdbad";
		Solution_02 so = new Solution_02();
		int len = so.lengthOfLongestSubstring03(s);
		System.out.println(len);
		
//		Map<Character, Integer> map = new HashMap<>();
//		map.put('w', 1);
//		map.put('w', 2);
//		System.out.println(map);	// 加入相同的数据，会更新数据，w=2
		
		
	}
}

class Solution_02 {

	// end-start+1 始终等于当前遍历的最大长度
	public int lengthOfLongestSubstring01(String s) {
		int n = s.length();
		int result = 0;
		Map<Character, Integer> map = new HashMap<>();
		for (int end = 0, start = 0; end < n; end++) {
			char temp = s.charAt(end);
			if (map.containsKey(temp)) {
				start = Math.max(map.get(temp), start);
			}
			result = Math.max(result, end - start + 1);
			map.put(s.charAt(end), end + 1);
		}
		return result;
	}

	// o(n3)暴力匹配
	// boolean allUnique(String substring) ，如果子字符串中的字符都是唯一的，
	// 它会返回 true，否则会返回 false。 
	// 我们可以遍历给定字符串 s的所有可能的子字符串并调用函数 allUnique。 
	// 如果事实证明返回值为 true，那么我们将会更新无重复字符子串的最大长度的答案。
	public int lengthOfLongestSubstring02(String s) {
		int n = s.length();
		int ans = 0;
		for (int i = 0; i < n; i++) {
			for (int j = i + 1; j <= n; j++) {
				if (allUnique(s, i, j)) {
					ans = Math.max(ans, j - i);
				}				
			}
		}
		return ans;
	}

	public boolean allUnique(String s, int start, int end) {
		Set<Character> set = new HashSet<>();
		for (int i = start; i < end; i++) {
			Character ch = s.charAt(i);
			if (set.contains(ch)) {
				return false;
			}
			set.add(ch);
		}
		return true;
	}

	// 滑动窗口  pawwkew
	public int lengthOfLongestSubstring03(String s) {
		int n = s.length();
		Set<Character> set = new HashSet<Character>();
		int ans = 0, i = 0, j = 0;
		while (i < n && j < n) {
			// try to extend the range [i, j]
			if (!set.contains(s.charAt(j))) {
				set.add(s.charAt(j++));
				ans = Math.max(ans, j - i);
			} else {
				set.remove(s.charAt(i++));
			}
		}
		return ans;
	}
	
	
	
	@Test
	public void test02() {
		String s = "abcdabccfdab";
		int res = maxDifferent(s);
		System.out.println(res);

		// List<Character> list = new ArrayList<>();
		// list.add('a');
		// list.add('b');
		// list.add('c');
		// list.add('d');
		// deleteLeft(list, 'c');
		// System.out.println(list);
	}

	private int maxDifferent(String s) {
		int length = 0;
		List<Character> list = new ArrayList<>();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (!list.contains(c)) {
				list.add(c);
				length = length > list.size() ? length : list.size();
			} else {
				deleteLeft(list, c);
			}
		}
		System.out.println(list);
		return length;
	}

	private void deleteLeft(List<Character> list, char s) {
		while (list.size() > 0) {
			if (list.get(0) != s) {
				list.remove(0);
			} else {
				list.remove(0);
				list.add(s);
				break;
			}
		}
	}

}
