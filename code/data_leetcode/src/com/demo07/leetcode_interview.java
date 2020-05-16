package com.demo07;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

public class leetcode_interview {

	/*	1. 两数之和
			给定一个整数数组 nums和一个目标值 target，
			请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
			你可以假设每种输入只会对应一个答案。但是，你不能重复利用这个数组中同样的元素。	
			给定 nums = [2, 7, 11, 15], target = 9
			因为 nums[0] + nums[1] = 2 + 7 = 9
			所以返回 [0, 1]	
	*/		
	@Test
	public void test_twoSum() {
		int[] nums = { 2, 7, 11, 15 };
		int[] res = twoSum(nums, 9);
		System.out.println(Arrays.toString(res));
	}

	public int[] twoSum(int[] nums, int target) {
		Map<Integer, Integer> map = new HashMap<>();
		for (int i = 0; i < nums.length; i++) {
			int complement = target - nums[i];
			if (map.containsKey(complement)) {
				return new int[] { map.get(complement), i };
			}
			map.put(nums[i], i);
		}
		throw new IllegalArgumentException("No two sum solution");
	}
	
	

	

/*	2. 爬楼梯
		假设你正在爬楼梯。需要 n 阶你才能到达楼顶。
		每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？
		注意：给定 n 是一个正整数。
		示例 1：
		
		输入： 2
		输出： 2
		解释： 有两种方法可以爬到楼顶。
		1.  1  阶 + 1 阶
		2.  2 阶	
	
		示例 2：
		输入： 3
		输出： 3
		解释： 有三种方法可以爬到楼顶。
		1.  1 阶 + 1 阶 + 1 阶
		2.  1 阶 + 2 阶
		3.  2 阶 + 1 阶
*/	
	@Test
	public void test_climbStairs() {
		int res = climbStairs(4);
		System.out.println(res);
	}

	public int climbStairs(int n) {
		if (n == 1) {
			return 1;
		}
		int[] dp = new int[n + 1];
		dp[1] = 1;
		dp[2] = 2;
		for (int i = 3; i <= n; i++) {
			dp[i] = dp[i - 1] + dp[i - 2];
		}
		return dp[n];
	}
	
	


/*	3. 翻转二叉树		
		示例：
		输入：
		     4
		   /   \
		  2     7
		 / \   / \
		1   3 6   9
	
		输出：
		     4
		   /   \
		  7     2
		 / \   / \
		9   6 3   1
*/
// 参见BinaryTreeQuestion
	
	
	
	
	
/*	4. 反转链表
	反转一个单链表。
	示例:
	输入: 1->2->3->4->5->NULL
	输出: 5->4->3->2->1->NULL
*/
	@Test
	public void test_reverseList() {

		ListNode n1 = new ListNode(1);
		ListNode n2 = new ListNode(2);
		ListNode n3 = new ListNode(3);
		ListNode n4 = new ListNode(4);
		ListNode n5 = new ListNode(5);

		n1.next = n2;
		n2.next = n3;
		n3.next = n4;
		n4.next = n5;

		n1.show();
		ListNode res = n1.reverseList();
		n1.show();	// 1
		res.show();
	}

	class ListNode {
		int val;
		ListNode next;

		ListNode(int x) {
			val = x;
		}

		// 显示所有节点信息
		public void show() {
			ListNode curr = this;
			while (true) {
				System.out.print(curr.val + " ");
				curr = curr.next;
				if (curr == null) {
					break;
				}
			}
			System.out.println();
		}

		// head:从链表的哪个位置开始反转
		// 反转链表的思路，遍历原来的链表，依次摘下每一个节点，放在新链表的前面
		public ListNode reverseList() {
			ListNode prev = null; // 新链表的头节点
			ListNode curr = this; // 定义一个变量指向调用节点
			while (curr != null) {
				ListNode nextTemp = curr.next;
				curr.next = prev; // curr.next和prev两个指针指向同一个对象
				prev = curr; // 并不是把curr的值赋值给prev
				curr = nextTemp;
			}
			return prev;
		}
	}
	
	
/*	5. LRU缓存机制【leetcode 146】
		运用你所掌握的数据结构，设计和实现一个LRU (最近最少使用) 缓存机制。
		它应该支持以下操作： 获取数据 get 和 写入数据 put 。
		获取数据 get(key) - 如果密钥 (key) 存在于缓存中，则获取密钥的值（总是正数），否则返回 -1。
		写入数据 put(key, value) - 如果密钥不存在，则写入其数据值。
		当缓存容量达到上限时，它应该在写入新数据之前删除最近最少使用的数据值，从而为新的数据值留出空间。
	
		进阶:  //LRU-Least Recently Used: 最近最少使用
		你是否可以在 O(1) 时间复杂度内完成这两种操作？
		LRUCache cache = new LRUCache(2); // 缓存容量
		cache.put(1, 1);
		cache.put(2, 2);
		cache.get(1);       // 返回  1
		cache.put(3, 3);    // 该操作会使得密钥 2 作废
		cache.get(2);       // 返回 -1 (未找到)
		cache.put(4, 4);    // 该操作会使得密钥 1 作废
		cache.get(1);       // 返回 -1 (未找到)
		cache.get(3);       // 返回  3
		cache.get(4);       // 返回  4
*/

	@Test
	public void test_LRUCache() {
		LRUCache cache = new LRUCache(3);
		for (int i = 1; i <= 10; i++) {
			cache.put(i, i);
		}
		cache.get(8);
		cache.put(12, 12);
		Set<Entry<Integer, Integer>> entrySet = cache.entrySet();
		for (Entry<Integer, Integer> entry : entrySet) {
			System.out.println(entry.getKey() + "===" + entry.getValue());
		}
	}	// 10===10	8===8	12===12

	class LRUCache extends LinkedHashMap<Integer, Integer> {
		private static final long serialVersionUID = 1L;
		private int capacity;

		public LRUCache(int capacity) {
			super(capacity, 0.75F, true);	// 默认false，插入顺序，true表示读的顺序
			this.capacity = capacity;
		}

		public int get(int key) {
			return super.getOrDefault(key, -1);
		}

		public void put(int key, int value) {
			super.put(key, value);
		}

		@Override
		protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
			return size() > capacity;	// 什么情况下删除第一个key，返回true删除
		}
	}

	

/*	6. 最长回文子串
		链接：https://leetcode-cn.com/problems/longest-palindromic-substring/	
		给定一个字符串 s，找到 s 中最长的回文子串。你可以假设 s 的最大长度为 1000。	
		示例 1：		
		输入: "babad"
		输出: "bab"
		注意: "aba" 也是一个有效答案。	
	
		示例 2：			
		输入: "cbbd"
		输出: "bb"
*/	

	@Test
	public void test_longestPalindrome() {
		String s = "abcd";
		String res = longestPalindrome(s);
		System.out.println(res);
	}

	public String longestPalindrome(String s) {
		if (s == null || s.length() < 1)
			return "";
		int start = 0, end = 0;
		for (int i = 0; i < s.length(); i++) {
			int len1 = expandAroundCenter(s, i, i);
			int len2 = expandAroundCenter(s, i, i + 1);
			int len = Math.max(len1, len2);
			if (len > end - start) {
				start = i - (len - 1) / 2;
				end = i + len / 2;
			}
		}
		return s.substring(start, end + 1);
	}

	private int expandAroundCenter(String s, int left, int right) {
		int L = left, R = right;
		while (L >= 0 && R < s.length() && s.charAt(L) == s.charAt(R)) {
			L--;
			R++;
		}
		return R - L - 1;
	}

	
	
/*	7. 有效的括号
	链接：https://leetcode-cn.com/problems/valid-parentheses/
	给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串，判断字符串是否有效。

	有效字符串需满足：
	    1. 左括号必须用相同类型的右括号闭合。
	    2. 左括号必须以正确的顺序闭合。

	注意空字符串可被认为是有效字符串。

	示例 1:
	输入: "()"
	输出: true
	
	示例 2:
	输入: "()[]{}"
	输出: true
	
	示例 3:
	输入: "(]"
	输出: false
	
	示例 4:
	输入: "([)]"
	输出: false
	
	示例 5:
	输入: "{[]}"
	输出: true
*/
	@Test
	public void test_IsValid() {
		String s = "{[]}{";
		boolean res = isValid(s);
		System.out.println(res);
	}

	public boolean isValid(String s) {
		if (s == null || s.length() == 0) {
			return false;
		}
		Map<Character, Character> map = new HashMap<>();
		map.put(')', '(');
		map.put(']', '[');
		map.put('}', '{');
		Deque<Character> dq = new ArrayDeque<>();
		for (char c : s.toCharArray()) {
			if (c == '(' || c == '[' || c == '{') {
				dq.push(c);
			} else {
				if (dq.size() == 0 || map.get(c) != dq.pop()) {
					return false;
				}
			}
		}
		return dq.size() == 0;// (){}[
	}
	
	
	
/*	8. 数组中的第K个最大元素
	链接：https://leetcode-cn.com/problems/kth-largest-element-in-an-array/
	在未排序的数组中找到第 k 个最大的元素。
	请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。

	示例 1:
	输入: [3,2,1,5,6,4] 和 k = 2
	输出: 5
	
	示例 2:
	输入: [3,2,3,1,2,4,5,5,6] 和 k = 4
	输出: 4
	
	说明:
	你可以假设 k 总是有效的，且 1 ≤ k ≤ 数组的长度。
*/

	

/*	9. 实现 Trie (前缀树)
		实现一个 Trie (前缀树)，包含 insert, search, 和 startsWith 这三个操作。
		
		示例:
		Trie trie = new Trie();
		trie.insert("apple");
		trie.search("apple");   // 返回 true
		trie.search("app");     // 返回 false
		trie.startsWith("app"); // 返回 true
		trie.insert("app");   
		trie.search("app");     // 返回 true
		
		说明:
		- 你可以假设所有的输入都是由小写字母 a-z 构成的。
		- 保证所有输入均为非空字符串。
*/

	@Test
	public void test_Trie() {
		Trie trie = new Trie();
		trie.insert("apple");
		boolean res1 = trie.search("apple"); // 返回 true
		boolean res2 = trie.search("app"); // 返回 false
		boolean res3 = trie.startsWith("app"); // 返回 true
		trie.insert("app");
		boolean res4 = trie.search("app"); // 返回 true
		System.out.printf("res1=%b,\nres2=%b,\nres3=%b,\nres4=%b", res1, res2, res3, res4);
	}

	class TrieNode {
		TrieNode[] child;	// 记录孩子节点
		boolean is_end;		// 记录当前节点是不是一个单词的结束字母

		public TrieNode() {
			child = new TrieNode[26];	// 子节点数组长度26，0:a,1:b.....
			is_end = false;
		}
	}

	class Trie {

		TrieNode root;	// 记录前缀树的根
		
		public Trie() {
			root = new TrieNode();
		}
	
		public void insert(String word) {
			TrieNode ptr = root;// 从根出发
			for (int i = 0; i < word.length(); i++) {
				char c = word.charAt(i);// 对于每个单词
				if (ptr.child[c - 'a'] == null) {// 如果c - 'a'为空，说明还没有存入
					ptr.child[c - 'a'] = new TrieNode();// 存入节点
				}
				ptr = ptr.child[c - 'a'];// 指向当前节点
			}
			ptr.is_end = true;// 最后的节点为单词的最后一个单词，is_end设置为true
		}

		/** Returns if the word is in the trie. */
		public boolean search(String word) {
			TrieNode ptr = root;// 从根出发
			for (int i = 0; i < word.length(); i++) {
				char c = word.charAt(i);// 对于每个字母
				if (ptr.child[c - 'a'] == null) {// 如果不存在于前缀树中，返回false
					return false;
				}
				ptr = ptr.child[c - 'a'];
			}
			return ptr.is_end;// 如果所有字符都在前缀树中，那么判断最后一个字母结束标志是否为true，
			// 为true，返回true，说明单词找到，否则，false，没找到
		}

		/*
		  Returns if there is any word in the trie that starts with the given prefix.
		 */
		public boolean startsWith(String prefix) {
			TrieNode ptr = root;// 从根出发
			for (int i = 0; i < prefix.length(); i++) {
				char c = prefix.charAt(i);// 对于每个字母
				if (ptr.child[c - 'a'] == null) {// 如果不存在于前缀树中，返回false
					return false;
				}
				ptr = ptr.child[c - 'a'];
			}
			return true;
		}
	}
	
	
	
	
/*	10. 编辑距离
		链接：https://leetcode-cn.com/problems/edit-distance/
		给定两个单词 word1 和 word2，计算出将 word1 转换成 word2 所使用的最少操作数 。
		你可以对一个单词进行如下三种操作：
		    1. 插入一个字符
		    2. 删除一个字符
		    3. 替换一个字符
	
		示例 1:
			输入: word1 = "horse", word2 = "ros"
			输出: 3
			解释: 
			horse -> rorse (将 'h' 替换为 'r')
			rorse -> rose (删除 'r')
			rose -> ros (删除 'e')
	
		示例 2:	
			输入: word1 = "intention", word2 = "execution"
			输出: 5
			解释: 
			intention -> inention (删除 't')
			inention -> enention (将 'i' 替换为 'e')
			enention -> exention (将 'n' 替换为 'x')
			exention -> exection (将 'n' 替换为 'c')
			exection -> execution (插入 'u')
*/	

	public int minDistance(String word1, String word2) {
		int size1 = word1.length();
		int size2 = word2.length();
		int[][] dp = new int[size1 + 1][size2 + 1];
		for (int i = 0; i <= size1; i++) {
			dp[i][0] = i;
		}
		for (int j = 0; j <= size2; j++) {
			dp[0][j] = j;
		}
		for (int i = 1; i <= size1; i++) {
			for (int j = 1; j <= size2; j++) {
				if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
					dp[i][j] = dp[i - 1][j - 1];
				} else {
					dp[i][j] = Math.min(Math.min(dp[i - 1][j - 1], dp[i][j - 1]), dp[i - 1][j]) + 1;
				}
			}
		}
		return dp[size1][size2];
	}

}