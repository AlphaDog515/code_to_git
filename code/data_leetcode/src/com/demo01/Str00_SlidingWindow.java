package com.demo01;

import java.util.ArrayList;
import java.util.List;

public class Str00_SlidingWindow {
	public static void main(String[] args) {
		Solution01_1 solution = new Solution01_1();
		String s1 = "hooheooeh";
		String s2 = "he";
		List<Integer> res = solution.findAnagrams(s1, s2);
		System.out.println(res);
	}
}

// 问题1：给定一个字符串 s 和一个非空字符串 p，找到 s 中所有是 p 的字母异位词的子串，返回这些子串的起始索引。
// 字符串只包含小写英文字母，并且字符串 s 和 p 的长度都不超过 20100。
/*public int slidingWindowTemplate(String[] a, ...) {
    // 输入参数有效性判断
    if (...) {
        ...
    }

    // 申请一个散列，用于记录窗口中具体元素的个数情况
    // 这里用数组的形式呈现，也可以考虑其他数据结构
    int[] hash = new int[...];

    // 预处理(可省), 一般情况是改变 hash
    ...

    // l 表示左指针
    // count 记录当前的条件，具体根据题目要求来定义
    // result 用来存放结果
    int l = 0, count = ..., result = ...;
    for (int r = 0; r < A.length; ++r) {
        // 更新新元素在散列中的数量
        hash[A[r]]--;

        // 根据窗口的变更结果来改变条件值
        if (hash[A[r]] == ...) {
            count++;
        }

        // 如果当前条件不满足，移动左指针直至条件满足为止
        while (count > K || ...) {
            ...
            if (...) {
                count--;
            }
            hash[A[l]]++;
            l++;
        }

        // 更新结果
        results = ...
    }
    return results;
}
*/

class Solution01_1 {
	// 使用上面的模板进行解题，so easy ！
	public List<Integer> findAnagrams(String s, String p) {
		// 输入参数有效性判断
		if (s.length() < p.length()) {
			return new ArrayList<Integer>();
		}

		// 申请一个散列，用于记录窗口中具体元素的个数情况
		// 这里用数组的形式呈现，也可以考虑其他数据结构
		char[] sArr = s.toCharArray();
		char[] pArr = p.toCharArray();

		int[] hash = new int[26];

		for (int i = 0; i < pArr.length; ++i) {
			hash[pArr[i] - 'a']++; // 指定位置的元素加1
		}

		// l 表示左指针
		// count 记录当前的条件，具体根据题目要求来定义
		// result 用来存放结果
		List<Integer> results = new ArrayList<>();
		int l = 0, count = 0, pLength = p.length();

		for (int r = 0; r < sArr.length; ++r) {
			// 更新新元素在散列中的数量
			hash[sArr[r] - 'a']--;

			// 根据窗口的变更结果来改变条件值
			if (hash[sArr[r] - 'a'] >= 0) {
				count++;
			}

			// 如果当前条件不满足，移动左指针直至条件满足为止,此处需要具体问题具体分析
			if (r > pLength - 1) {
				hash[sArr[l] - 'a']++;

				if (hash[sArr[l] - 'a'] > 0) {
					count--;
				}

				l++;
			}

			// 更新结果
			if (count == pLength) {
				results.add(l);
			}
		}

		return results;
	}
}