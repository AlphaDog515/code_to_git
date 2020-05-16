package com.demo01;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack; // 线程安全

public class Str02_RemoveDuplicates {
	public static void main(String[] args) {
		String s = "deeedbbcccbdaa";
		Solution_01 s_01 = new Solution_01();
		String str = s_01.removeDuplicates_01(s, 3);
		System.out.println(str);

	}
}

// 给定字符串 s，k重复项删除操作会从 s中选择 k个相邻且相等的字母删除，
// 被删去的字符串的左侧和右侧连在一起，重复删除操作，直到无法继续为止，
// 在执行完所有删除操作后，返回最终得到的字符串。
// 例如：输入：s = "deeedbbcccbdaa", k = 3，
// 输出：输出："aa"，先删除 "eee" 和 "ccc"，得到 "ddbbbdaa"，
// 再删除 "bbb"，得到 "dddaa"，最后删除 "ddd"，得到 "aa"。
class Solution_01 {

	public String removeDuplicates_01(String s, int k) {
		Stack<Integer[]> stack = new Stack<>();

		for (char c : s.toCharArray()) {
			int index = 1, ch = c - 'a'; // 将字符转化为数字存在Integer[]里面

			// 如果栈不为空，并且栈顶元素（Integer数组第0个元素等于ch）
			if (!stack.isEmpty() && stack.peek()[0] == ch) {
				// 索引index，相同字符个数加1，Integer数组的第一个元素加1
				index = stack.peek()[1] + 1;
			}
			
			// 将新遍历得到的元素压入栈中
			stack.push(new Integer[] { ch, index });
			
			// 如果栈顶元素，Integer数组第1一个元素等于k，将栈顶的k个元素出栈
			if (stack.peek()[1] == k) {
				for (int i = 0; i < k; i++) {
					stack.pop();
				}
			}
		}

		// 拼接,将每个数组中的第0个元素转化为字符
		StringBuffer sb = new StringBuffer();
		// 遍历栈中的整数数组
		for (Integer[] integers : stack) {
			sb.append((char) (integers[0] + 'a'));
		}
		return sb.toString();
	}

	public String removeDuplicates_02(String s, int k) {
		// 无限删除的问题统计连续字母达到K的坐标
		while (true) {
			List<List<Integer>> allList = checkStr(s, k);
			if (allList.size() == 0)
				return s;
			else {
				StringBuffer sb = new StringBuffer();
				int ss = 0;
				for (List<Integer> list : allList) {
					String newS = s.substring(ss, list.get(0));
					ss = list.get(1) + 1;
					sb.append(newS);
				}

				String lastStr = s.substring(ss, s.length());
				sb.append(lastStr);
				s = sb.toString();
			}
		}
	}

	private List<List<Integer>> checkStr(String s, int k) {
		int start = 0, len = 1;
		List<List<Integer>> allList = new ArrayList<>();
		while (start < s.length() - 1) {
			if (s.charAt(start) == s.charAt(start + 1)) {
				len++;
			} else {
				len = 1;
			}

			if (len == k) {
				List<Integer> list = new ArrayList<>();
				list.add(start + 2 - k);
				list.add(start + 1);
				allList.add(list);
				start = start + 2;
				len = 1;
			} else
				start++;
		}

		return allList;
	}

	public String removeDuplicates_03(String s, int k) {
		int n = 0;
		while (n < 2) {// 循环
			for (int i = 0; i <= s.length() - k; i++) {
				if (/* k == s.substring(i, i + k).length() && */ isSame(s.substring(i, i + k))) {
					s = s.replace(s.substring(i, i + k), "");
					n = 0;// 重置n
				}
			}
			n++;// 遍历了一次，n+1
		}
		return s;
	}

	private boolean isSame(String s) {// 判断子串是否都相同
		char f = s.charAt(0);
		for (int i = 0; i < s.length(); i++)
			if (s.charAt(i) != f)
				return false;
		return true;
	}

	public String removeDuplicates_04(String s, int k) {
		while (isRepeat(s, k)) {
			String regex = "([a-zA-Z])\\1{2}"; // \\d代表的是任意数字
			s = s.replaceAll(regex, "");
		}
		return s;
	}

	private boolean isRepeat(String s, int k) {
		char[] c = s.toCharArray();
		List<Character> list = new ArrayList<>();
		for (int i = 0; i <= c.length - k; i++) {
			for (int j = i; j < i + k; j++) {
				if (c[i] == c[j]) {
					list.add(c[j]);
					if (list.size() >= k) {
						list.clear();
						return true;
					}
				}

			}
		}
		return false;
	}
}
//([0-9a-zA-Z])\1{5} 连续相同的6位数字或字母 如:222222 cccccc ZZZZZZ
//([\d])\1{2}([a-z])\2{2} 连续相同3位数字后根连续相同的三位小写字母 如:222www
//([\d])\1{2}([a-z])\2{2}|([a-z])\3{2}([\d])\4{2} 同上,但是能匹配数字+字母或字母+数字 
//如:222www 或 www222,注意的就是 \1 \2代表位置,从左到右递增