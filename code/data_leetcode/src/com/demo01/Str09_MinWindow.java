package com.demo01;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.util.Pair;
public class Str09_MinWindow {

}

/*

	给你一个字符串 S、一个字符串 T，请在字符串 S 里面找出：包含 T 所有字母的最小子串。	
	示例：	
	输入: S = "ADOBECODEBANC", T = "ABC"
	输出: "BANC"
	说明：	
	如果 S 中不存这样的子串，则返回空字符串 ""。
	如果 S 中存在这样的子串，我们保证它是唯一的答案。
	
	直觉
	本问题要求我们返回字符串S中包含字符串T的全部字符的最小窗口。我们称包含T的全部字母的窗口为可行窗口。	
	可以用简单的滑动窗口法来解决本问题。	
	在滑动窗口类型的问题中都会有两个指针。一个用于延伸现有窗口的right指针，
	和一个用于收缩窗口的left指针。在任意时刻，只有一个指针运动，而另一个保持静止。	
	本题的解法很符合直觉。我们通过移动right指针不断扩张窗口。
	当窗口包含全部所需的字符后，如果能收缩，我们就收缩窗口直到得到最小窗口。	
	答案是最小的可行窗口。
	
	算法	
	1.初始，left指针和right指针都指向S的第一个元素.	
	2.将 right指针右移，扩张窗口，直到得到一个可行窗口，亦即包含T的全部字母的窗口。	
	3.得到可行的窗口后，将left指针逐个右移，若得到的窗口依然可行，则更新最小窗口大小。	
	4.若窗口不再可行，则跳转至 2。

*/

class Solution_9_1 {
	public String minWindow(String s, String t) {
		if (s.length() == 0 || t.length() == 0) {
			return "";
		}

		// Dictionary which keeps a count of all the unique characters in t.
		Map<Character, Integer> dictT = new HashMap<Character, Integer>();
		for (int i = 0; i < t.length(); i++) {
			int count = dictT.getOrDefault(t.charAt(i), 0);
			dictT.put(t.charAt(i), count + 1);
		}

		// Number of unique characters in t, which need to be present in the desired
		// window.
		int required = dictT.size();

		// Left and Right pointer
		int l = 0, r = 0;

		// formed is used to keep track of how many unique characters in t
		// are present in the current window in its desired frequency.
		// e.g. if t is "AABC" then the window must have two A's, one B and one C.
		// Thus formed would be = 3 when all these conditions are met.
		int formed = 0;

		// Dictionary which keeps a count of all the unique characters in the current
		// window.
		Map<Character, Integer> windowCounts = new HashMap<Character, Integer>();

		// ans list of the form (window length, left, right)
		int[] ans = { -1, 0, 0 };

		while (r < s.length()) {
			// Add one character from the right to the window
			char c = s.charAt(r);
			int count = windowCounts.getOrDefault(c, 0);
			windowCounts.put(c, count + 1);

			// If the frequency of the current character added equals to the
			// desired count in t then increment the formed count by 1.
			if (dictT.containsKey(c) && windowCounts.get(c).intValue() == dictT.get(c).intValue()) {
				formed++;
			}

			// Try and co***act the window till the point where it ceases to be 'desirable'.
			while (l <= r && formed == required) {
				c = s.charAt(l);
				// Save the smallest window until now.
				if (ans[0] == -1 || r - l + 1 < ans[0]) {
					ans[0] = r - l + 1;
					ans[1] = l;
					ans[2] = r;
				}

				// The character at the position pointed by the
				// `Left` pointer is no longer a part of the window.
				windowCounts.put(c, windowCounts.get(c) - 1);
				if (dictT.containsKey(c) && windowCounts.get(c).intValue() < dictT.get(c).intValue()) {
					formed--;
				}

				// Move the left pointer ahead, this would help to look for a new window.
				l++;
			}

			// Keep expanding the window once we are done co***acting.
			r++;
		}

		return ans[0] == -1 ? "" : s.substring(ans[1], ans[2] + 1);
	}
}


//	算法
//	我们建立一个filtered_S列表，其中包括 S中的全部字符以及它们在S的下标，但这些字符必须在 T中出现。
//	S = "ABCDDDDDDEEAFFBC" T = "ABC"
//	filtered_S = [(0, 'A'), (1, 'B'), (2, 'C'), (11, 'A'), (14, 'B'), (15, 'C')]
//	此处的(0, 'A')表示字符'A' 在字符串S的下表为0。
//	现在我们可以在更短的字符串filtered_S中使用滑动窗口法。


class Solution_9_2 {
	public String minWindow(String s, String t) {

		if (s.length() == 0 || t.length() == 0) {
			return "";
		}

		Map<Character, Integer> dictT = new HashMap<Character, Integer>();

		for (int i = 0; i < t.length(); i++) {
			int count = dictT.getOrDefault(t.charAt(i), 0);
			dictT.put(t.charAt(i), count + 1);
		}

		int required = dictT.size();

		// Filter all the characters from s into a new list along with their index.
		// The filtering criteria is that the character should be present in t.
		List<Pair<Integer, Character>> filteredS = new ArrayList<Pair<Integer, Character>>();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (dictT.containsKey(c)) {
				filteredS.add(new Pair<Integer, Character>(i, c));
			}
		}

		int l = 0, r = 0, formed = 0;
		Map<Character, Integer> windowCounts = new HashMap<Character, Integer>();
		int[] ans = { -1, 0, 0 };

		// Look for the characters only in the filtered list instead of entire s.
		// This helps to reduce our search.
		// Hence, we follow the sliding window approach on as small list.
		while (r < filteredS.size()) {
			char c = filteredS.get(r).getValue();
			int count = windowCounts.getOrDefault(c, 0);
			windowCounts.put(c, count + 1);

			if (dictT.containsKey(c) && windowCounts.get(c).intValue() == dictT.get(c).intValue()) {
				formed++;
			}

			// Try and co***act the window till the point where it ceases to be 'desirable'.
			while (l <= r && formed == required) {
				c = filteredS.get(l).getValue();

				// Save the smallest window until now.
				int end = filteredS.get(r).getKey();
				int start = filteredS.get(l).getKey();
				if (ans[0] == -1 || end - start + 1 < ans[0]) {
					ans[0] = end - start + 1;
					ans[1] = start;
					ans[2] = end;
				}

				windowCounts.put(c, windowCounts.get(c) - 1);
				if (dictT.containsKey(c) && windowCounts.get(c).intValue() < dictT.get(c).intValue()) {
					formed--;
				}
				l++;
			}
			r++;
		}
		return ans[0] == -1 ? "" : s.substring(ans[1], ans[2] + 1);
	}
}
