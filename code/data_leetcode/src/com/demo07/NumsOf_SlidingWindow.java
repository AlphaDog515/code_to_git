package com.demo07;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class NumsOf_SlidingWindow {

	public static void main(String[] args) {
//		SlidingWindow_01 s1 = new SlidingWindow_01();
//		int[] nums = { 1, 3, -1, -3, 5, 3, 6, 7 };
//		int[] res = s1.maxSlidingWindow(nums, 3);
//		System.out.println(Arrays.toString(res));
		
//		SlidingWindow_02 s2 = new SlidingWindow_02();
//		int res = s2.lengthOfLongestSubstring_01("abcabcbb");
//		System.out.println(res);

//		SlidingWindow_03 s3 = new SlidingWindow_03();
//		int[] nums = { 1, 2, 3, 1 };
//		boolean res = s3.containsNearbyDuplicate(nums, 3);
//		System.out.println(res);
		
		
//		SlidingWindow_05 s5 = new SlidingWindow_05();
//		String s1 = "hooheooeh";
//		String s2 = "he";
//		List<Integer> res = s5.findAnagrams(s1, s2);
//		System.out.println(res);
		
		
		SlidingWindow_06 s6 = new SlidingWindow_06();
		String s1 = "hooheooeh";
		String s2 = "he";
		String res = s6.minWindow(s1, s2);
		System.out.println(res);

	}

}

// 滑动窗口的最值
class SlidingWindow_01{
	/*
	 给定一个数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
	你只可以看到在滑动窗口 k 内的数字。滑动窗口每次只向右移动一位。
	返回滑动窗口最大值。
	
	示例:	
	输入: nums = [1,3,-1,-3,5,3,6,7], 和 k = 3
	输出: [3,3,5,5,6,7] 
	解释: 	
	滑动窗口的位置                		    最大值
	---------------               -----
	[1  3  -1] -3  5  3  6  7       3
	 1 [3  -1  -3] 5  3  6  7       3
	 1  3 [-1  -3  5] 3  6  7       5
	 1  3  -1 [-3  5  3] 6  7       5
	 1  3  -1  -3 [5  3  6] 7       6
	 1  3  -1  -3  5 [3  6  7]      7
	 
	 */
	
	public List<Integer> maxSlidingWindow_01(int[] nums, int k) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = k - 1; i < nums.length; i++) {
			int max = getArrMax(nums, i - k + 1, i);
			list.add(max);
		}
		return list;
	}

	private int getArrMax(int[] nums, int l, int r) {
		int max = nums[l];
		for (int i = l; i <= r; i++) {
			if (nums[i] > max) {
				max = nums[i];
			}
		}
		return max;
	}

	

	/*	 
	 	利用一个 双端队列，在队列中存储元素在数组中的位置，并且维持队列的严格递减，
	 	维持队首元素最大 ，当遍历到一个新元素时, 如果队列里有比当前元素小的，
	 	就将其移除队列，以保证队列的递减。
	 	当队列元素位置之差大于 k，就将队首元素移除。
	 */
	public int[] maxSlidingWindow(int[] nums, int k) {
		// 有点坑，题目里都说了数组不为空，且 k > 0。
		// 但是看了一下，测试用例里面还是有nums = [], k = 0，所以只好加上这个判断
		if (nums == null || nums.length < k || k == 0)
			return new int[0];
		
		int[] res = new int[nums.length - k + 1];
		
		// 双端队列  // nums = { 1, 3, -1, -3, 5, 3, 6, 7 }
		Deque<Integer> deque = new LinkedList<>();
		for (int i = 0; i < nums.length; i++) {
			// 在尾部添加元素，并保证左边元素都比尾部大
			while (!deque.isEmpty() && nums[deque.getLast()] < nums[i]) {
				deque.removeLast();
			}
			deque.addLast(i);
			// 在头部移除元素
			if (deque.getFirst() == i - k) {
				deque.removeFirst();
			}
			// 输出结果
			if (i - k + 1 >= 0) {
				res[i - k + 1] = nums[deque.getFirst()];
			}
		}
		return res;
	}

}


// 不重复最长子串
class SlidingWindow_02{
	// 给定一个字符串，请你找出其中不含有重复字符的最长子串的长度。
	/*	 
	 	输入: "abcabcbb"
		输出: 3 
		解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
	 */
	
	public int lengthOfLongestSubstring(String s) {
		int max = 0;
		Deque<Character> deque = new LinkedList<>();
		for (int i = 0; i < s.length(); i++) {
			while (!deque.isEmpty() && deque.contains(s.charAt(i))) {
				deque.removeFirst();
			}
			deque.addLast(s.charAt(i));
			max = deque.size() > max ? deque.size() : max;
		}
		return max;
	}
	
	/*
	 	建立一个256位大小的整型数组 freg ，用来建立字符和其出现位置之间的映射。
		维护一个滑动窗口，窗口内的都是没有重复的字符，去尽可能的扩大窗口的大小，窗口不停的向右滑动。		
		（1）如果当前遍历到的字符从未出现过，那么直接扩大右边界；		
		（2）如果当前遍历到的字符出现过，则缩小窗口（左边索引向右移动），然后继续观察当前遍历到的字符；		
		（3）重复（1）（2），直到左边索引无法再移动；		
		（4）维护一个结果res，每次用出现过的窗口大小来更新结果 res，最后返回 res 获取结果。	 
	 */
	public int lengthOfLongestSubstring_01(String s) {
		int[] freq = new int[128];
		char[] arr = s.toCharArray();
		int l = 0, r = -1; // 滑动窗口为arr[l...r]
		int res = 0;
		// 整个循环从 l == 0; r == -1 这个空窗口开始
		// 到l == arr.size(); r == arr.size()-1 这个空窗口截止
		// 在每次循环里逐渐改变窗口, 维护freq, 并记录当前窗口中是否找到了一个新的最优值
		while (l < s.length()) {
			if (r + 1 < s.length() && freq[arr[r + 1]] == 0) {
				r++;
				freq[arr[r]]++;
			} else { // r已经到头 || freq[arr[r+1]] == 1
				freq[arr[l]]--;
				l++;
			}
			res = Math.max(res, r - l + 1);
		}
		return res;
	}

	public int lengthOfLongestSubstring_02(String s) {
		if (s == null || s.length() == 0) {
			return 0;
		}

		char[] sArr = s.toCharArray();
		int[] hash = new int[256];

		int l = 0, result = 1;
		for (int r = 0; r < sArr.length; r++) {
			hash[sArr[r]]++;

			while (hash[sArr[r]] != 1) {
				hash[sArr[l]]--;
				l++;
			}

			result = Math.max(result, r - l + 1);
		}

		return result;
	}

}



class SlidingWindow_03{
	/*
	 
	 给定一个整数数组和一个整数 k，判断数组中是否存在两个不同的索引 i 和 j，
	 使得 nums [i] = nums [j]，并且 i 和 j 的差的绝对值最大为 k.
	示例 1:	
	输入: nums = [1,2,3,1], k = 3
	输出: true
	
	示例 2:	
	输入: nums = [1,0,1,1], k = 1
	输出: true
	
	示例 3:	
	输入: nums = [1,2,3,1,2,3], k = 2
	输出: false

	使用用滑动窗口与查找表来解决。	
		设置查找表record，用来保存每次遍历时插入的元素，record的最大长度为k	
		遍历数组nums，每次遍历的时候在record查找是否存在相同的元素，如果存在则返回true，遍历结束	
		如果此次遍历在record未查找到，则将该元素插入到record中，而后查看record的长度是否为k + 1	
		如果此时record的长度是否为k + 1，则删减record的元素，该元素的值为nums[i - k]	
		如果遍历完整个数组nums未查找到则返回false
		 
	 */
	
	public boolean containsNearbyDuplicate(int[] nums, int k) {
		if (nums.length <= 1)
			return false;
		Deque<Integer> record = new LinkedList<>();
		for (int i = 0; i < nums.length; i++) { // 1 2 3 1 3
			record.addLast(nums[i]);
			System.out.println(record);
			if (record.size() == k + 1 && record.getFirst() == record.getLast()) {
				return true;
			}

			if (record.size() == k + 1) {
				record.removeFirst();
			}
		}
		return false;
	}
	
	
}

// 最小连续子数组
class SlidingWindow_04{
	
	/*
		给定一个含有 n 个正整数的数组和一个正整数 s，
		找出该数组中满足其和 ≥ s 的长度最小的连续子数组。
		如果不存在符合条件的连续子数组，返回 0。
	
		示例:	
		输入: s = 7, nums = [2,3,1,2,4,3]
		输出: 2
		解释: 子数组 [4,3] 是该条件下的长度最小的连续子数组。
		
		题目解析
		定义两个指针 left 和 right，分别记录子数组的左右的边界位置。	
		（1）让 right 向右移，直到子数组和大于等于给定值或者 right 达到数组末尾；	
		（2）更新最短距离，将 left 像右移一位, sum 减去移去的值；	
		（3）重复（1）（2）步骤，直到 right 到达末尾，且 left 到达临界位置
	
	*/

	public int minSubArrayLen(int s, int[] nums) {
		int l = 0, r = -1; // nums[l...r]为我们的滑动窗口
		int sum = 0;
		int result = nums.length + 1;
		while (l < nums.length) { // 窗口的左边界在数组范围内,则循环继续

			if (r + 1 < nums.length && sum < s) {
				r++;
				sum += nums[r];
			} else { // r已经到头 或者 sum >= s
				sum -= nums[l];
				l++;
			}

			if (sum >= s) {
				result = (r - l + 1) < result ? (r - l + 1) : result;
			}
		}
		if (result == nums.length + 1) {
			return 0;
		}
		return result;
	}
}



// 所有字母异位词
class SlidingWindow_05{
	/*
	 
	 给定一个字符串 s 和一个非空字符串 p，找到 s 中所有是 p 的字母异位词的子串，返回这些子串的起始索引。
	 字符串只包含小写英文字母，并且字符串 s 和 p 的长度都不超过 20100 。

	 首先窗口是固定的，窗口长度就是输入参数中第二个字符串的长度，
	 也就是说，右指针移动到某个位置后，左指针必须跟着一同移动，且每次移动都是一格，
	 模版中 count 用来记录窗口内满足条件的元素，直到 count 和窗口长度相等即可更新答案。
	 
	 */
	/*
		 public int slidingWindowTemplate(String[] a, ...) {
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

			// 如果当前条件不满足，移动左指针直至条件满足为止，此处需要具体问题具体分析
			if (r > pLength - 1) {
				hash[sArr[l] - 'a']++;

				if (hash[sArr[l] - 'a'] > 0) {
					count--;
				}

				l++;
			}

			// 更新结果  满足条件，左指针添加进去
			if (count == pLength) {
				results.add(l);
			}
		}

		return results;
	}
}


// 最小覆盖子串
class SlidingWindow_06 {
	
	/*
 	给你一个字符串 S、一个字符串 T，请在字符串 S 里面找出：包含 T 所有字母的最小子串。
	示例：		
	输入: S = "ADOBECODEBANC", T = "ABC"
	输出: "BANC"
	
	同样是两个字符串之间的关系问题，因为题目求的最小子串，也就是窗口的最小长度，
	说明这里的窗口大小是可变的，这里移动左指针的条件变成，只要左指针指向不需要的字符，就进行移动。 
	 
	 */

	public String minWindow(String s, String t) {
		if (s.length() < t.length()) {
			return "";
		}

		char[] sArr = s.toCharArray();
		char[] tArr = t.toCharArray();

		int[] hash = new int[256];

		for (int i = 0; i < tArr.length; ++i) {
			hash[tArr[i]]++;
		}

		int l = 0, count = tArr.length, max = s.length() + 1;
		String result = "";
		
		for (int r = 0; r < sArr.length; ++r) {
			hash[sArr[r]]--;

			if (hash[sArr[r]] >= 0) {
				count--;
			}

			while (l < r && hash[sArr[l]] < 0) {
				hash[sArr[l]]++;
				l++;
			}

			if (count == 0 && max > r - l + 1) {
				max = r - l + 1;
				result = s.substring(l, r + 1);
			}
		}

		return result;
	}
}


// 字符串的排列
class SlidingWindow_07 {
	
	/*
	 
	 给定两个字符串 s1 和 s2，写一个函数来判断 s2 是否包含 s1 的排列。
	换句话说，第一个字符串的排列之一是第二个字符串的子串。
	
	示例1:	
	输入: s1 = "ab" s2 = "eidbaooo"
	输出: True
	解释: s2 包含 s1 的排列之一 ("ba").
	
	示例2:	
	输入: s1= "ab" s2 = "eidboaoo"
	输出: False
	 
	 */

	public boolean checkInclusion(String s1, String s2) {
		if (s1.length() > s2.length()) {
			return false;
		}

		char[] s1Arr = s1.toCharArray();
		char[] s2Arr = s2.toCharArray();

		int[] hash = new int[26];

		for (int i = 0; i < s1Arr.length; ++i) {
			hash[s1Arr[i] - 'a']++;
		}

		int l = 0, count = 0;
		for (int r = 0; r < s2Arr.length; ++r) {
			hash[s2Arr[r] - 'a']--;

			if (hash[s2Arr[r] - 'a'] >= 0) {
				count++;
			}

			if (r >= s1Arr.length) {
				hash[s2Arr[l] - 'a']++;

				if (hash[s2Arr[l] - 'a'] >= 1) {
					count--;
				}

				l++;
			}

			if (count == s1Arr.length) {
				return true;
			}
		}

		return false;
	}

}


// K 个不同整数的子数组
class SlidingWindow_08 {

	/*
	 
	 给定一个正整数数组 A，如果 A 的某个子数组中不同整数的个数恰好为 K，
	 则称 A 的这个连续、不一定独立的子数组为好子数组。	
	
	示例 1：	
	输入：A = [1,2,1,2,3], K = 2
	输出：7	
	解释：恰好由 2 个不同整数组成的子数组：[1,2], [2,1], [1,2], [2,3], [1,2,1], [2,1,2], [1,2,1,2].
	
	示例 2：	
	输入：A = [1,2,1,3,4], K = 3
	输出：3
	解释：恰好由 3 个不同整数组成的子数组：[1,2,1,3], [2,1,3], [1,3,4].
	
	
	题目中的 subarray 已经明确了这个题可以考虑用滑动窗口，
	这题比较 trick 的一个地方在于，这里不是求最小值最大值，而是要你计数。
	
	但是如果每次仅仅加 1 的话又不太对，例如 A = [1,2,1,2,3], K = 2 这个例子，
	假如右指针移到 index 为 3 的位置，如果按之前的思路左指针根据 count 来移动，
	当前窗口是 [1,2,1,2]，但是怎么把 [2,1] 给考虑进去呢？
	
	可以从数组和子数组的关系来思考！
	
	假如 [1,2,1,2] 是符合条件的数组，如果要计数的话，[1,2,1,2] 要求的结果是否和 [1,2,1] 的结果存在联系？
	这两个数组的区别在于多了一个新进来的元素，之前子数组计数没考虑到这个元素，
	假如把这个元素放到之前符合条件的子数组中组成的新数组也是符合条件的，
	我们看看这个例子中所有满足条件的窗口以及对应的满足条件的子数组情况：
	
	[1,2,1,2,3]  // 窗口满足条件
	 l r         // 满足条件的子数组 [1,2]
	
	[1,2,1,2,3]  // 窗口满足条件
	 l   r       // 满足条件的子数组 [1,2],[2,1],[1,2,1]
	
	[1,2,1,2,3]  // 窗口满足条件
	 l     r     // 满足条件的子数组 [1,2],[2,1],[1,2,1],[1,2],[2,1,2],[1,2,1,2]
	
	[1,2,1,2,3]  // 窗口不满足条件，移动左指针至满足条件
	 l       r   
	
	[1,2,1,2,3]  // 窗口满足条件
	       l r   // 满足条件的子数组 [1,2],[2,1],[1,2,1],[1,2],[2,1,2],[1,2,1,2],[2,3]
	
	你可以看到对于一段连续的数组，新的元素进来，窗口增加 1，每次的增量都会在前一次增量的基础上加 1。
	
	当新的元素进来打破当前条件会使这个增量从新回到 1，
	这样我们左指针移动条件就是只要是移动不会改变条件，就移动，不然就停止。	 
	 
	 */

	public int subarraysWithKDistinct(int[] A, int K) {
		if (A == null || A.length < K) {
			return 0;
		}

		int[] hash = new int[A.length + 1];

		int l = 0, results = 0, count = 0, result = 1;
		for (int r = 0; r < A.length; ++r) {
			hash[A[r]]++;

			if (hash[A[r]] == 1) {
				count++;
			}

			while (hash[A[l]] > 1 || count > K) {
				if (count > K) {
					result = 1;
					count--;
				} else {
					result++;
				}
				hash[A[l]]--;
				l++;
			}

			if (count == K) {
				results += result;
			}
		}

		return results;
	}

}


// 替换后的最长重复字符
class SlidingWindow_09 {
	/*
		给你一个仅由大写英文字母组成的字符串，你可以将任意位置上的字符替换成另外的字符，
		总共可最多替换 k 次。在执行上述操作后，找到包含重复字母的最长子串的长度。
	
		示例 1:
		输入:
		s = "ABAB", k = 2		
		输出:
		4		
		解释:
		用两个'A'替换为两个'B',反之亦然。
		
		示例 2:		
		输入:
		s = "AABABBA", k = 1		
		输出:
		4		
		解释:
		将中间的一个'A'替换为'B',字符串变为 "AABBBBA"。
		子串 "BBBB" 有最长重复字母, 答案为 4。

		题目解析
		这道题想 accept 的话不难，但是问题在于怎么知道当前窗口中数量最多的字符的数量，
		因为需要替换的字符就是当前窗口的大小减去窗口中数量最多的字符的数量。
	
		最简单的方法就是把 哈希散列 遍历一边找到最大的字符数量，
		但是仔细想想如果我们每次新进元素都更新这个最大数量，
		且只更新一次，我们保存的是当前遍历过的全局的最大值，
		它肯定是比实际的最大值大的，
		我们左指针移动的条件是 r - l + 1 - maxCount > k，
		保存的结果是 result = Math.max(r - l + 1, result); 
		这里 maxCount 比实际偏大的话，虽然导致左指针不能移动，
		但是不会记录当前的结果，所以最后的答案并不会受影响。

	*/
	public int characterReplacement(String s, int k) {
		if (s == null || s.length() == 0) {
			return 0;
		}

		char[] sArr = s.toCharArray();

		int[] hash = new int[26];

		int l = 0, maxCount = 0, result = 0;
		for (int r = 0; r < sArr.length; ++r) {
			hash[sArr[r] - 'A']++;

			maxCount = Math.max(maxCount, hash[sArr[r] - 'A']);

			while (r - l + 1 - maxCount > k) {
				hash[sArr[l] - 'A']--;
				l++;
			}

			result = Math.max(r - l + 1, result);
		}

		return result;
	}

}
