package com.demo01;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class Str07_TrieNode01 {
	public static void main(String[] args) {
		Map<String, String> map = new TreeMap<>();	// treeMap有序
		map.put("aa", "a1");
		map.put("dd", "b1");
		map.put("cc", "c1");
		Set<Entry<String, String>> entrySet = map.entrySet();
		for (Entry<String, String> entry : entrySet) {
			System.out.println(entry.getKey() + "-->" + entry.getValue());
		}

	}

}

class Trie01 {
	private class Node {

		public boolean isWord;
		public TreeMap<Character, Node> next;

		public Node(boolean isWord) {
			this.isWord = isWord;
			next = new TreeMap<>();
		}

		public Node() {
			this(false);
		}
	}

	private Node root;
	private int size;

	public Trie01() {
		root = new Node();
		size = 0;
	}

	/**
	 * 获取存储的单词量
	 * 
	 * @return
	 */
	public int getSize() {
		return size;
	}

	/**
	 * 向Trie树中传入单词word
	 * 
	 * @param word
	 */
	public void add(String word) {
		Node cur = root;
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			if (cur.next.get(c) == null) {
				cur.next.put(c, new Node());
			}
			cur = cur.next.get(c);
		}

		if (!cur.isWord) {
			cur.isWord = true;
			size++;
		}
	}

	/**
	 * 添加元素的递归写法接口
	 */
	public void recursionAdd(String word) {
		Node node = root;
		recursionAdd(root, word, 0);
	}

	/**
	 * 添加元素的递归写法
	 *
	 * @param node  要添加的节点
	 * @param word  被添加的字符串
	 * @param index 字符串第几个元素该被添加
	 */
	private void recursionAdd(Node node, String word, int index) {
		// 终止条件：该单词已经被遍历完且终节点上没有isWord标记，则标记单词并size++
		if (!node.isWord && index == word.length()) {
			node.isWord = true;
			size++;
		}
		// 隐藏的终止条件：如果该单词遍历完但终结点上有isWord标记，则终止遍历，当无事发生
		if (index < word.length()) {
			char addChar = word.charAt(index);
			// 判断下个节点组是否存在该字符，没有则添加
			if (node.next.get(addChar) == null) {
				node.next.put(addChar, new Node());
			}
			// 进行下一个字符的递归添加操作
			recursionAdd(node.next.get(addChar), word, index + 1);
		}
	}

	/**
	 * 查询Trie中是否有单词word
	 * 
	 * @param word
	 * @return
	 */
	public boolean contains(String word) {
		Node cur = root;
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			if (cur.next.get(c) == null) {
				return false;
			}
			cur = cur.next.get(c);
		}
		return cur.isWord;
	}

	/**
	 * 查询Trie中是否有单词word的递归写法接口
	 * 
	 * @param word
	 * @return
	 */
	public boolean recursionContains(String word) {
		Node node = root;
		return recursionContains(root, word, 0);
	}

	/**
	 * 检索Trie中是否包含word的递归写法
	 * 
	 * @param node  当前节点
	 * @param word  检查的单词
	 * @param index 单词的第几个字符
	 * @return
	 */
	private boolean recursionContains(Node node, String word, int index) {
		if (index == word.length()) {
			return node.isWord;
		}
		char c = word.charAt(index);
		if (node.next.get(c) == null) {
			return false;
		}
		return recursionContains(node.next.get(c), word, index + 1);
	}

	/**
	 * 查询Trie中是否有前缀为prefix的单词
	 * 
	 * @param prefix
	 * @return
	 */
	public boolean isPrefix(String prefix) {
		Node cur = root;
		for (int i = 0; i < prefix.length(); i++) {
			char c = prefix.charAt(i);
			if (cur.next.get(c) == null) {
				return false;
			}
			cur = cur.next.get(c);
		}
		return true;
	}

	/**
	 * 查询Trie中是否有前缀为prefix的单词的递归接口
	 * 
	 * @param prefix
	 * @return
	 */
	public boolean recursionIsPrefix(String prefix) {
		Node cur = root;
		return recursionIsPrefix(cur, prefix, 0);
	}

	/**
	 * 查询Trie中是否有前缀为prefix的单词的递归实现
	 * 
	 * @param node
	 * @param prefix
	 * @param index
	 * @return
	 */
	private boolean recursionIsPrefix(Node node, String prefix, int index) {
		if (index == prefix.length()) {
			return true;
		}
		char c = prefix.charAt(index);
		if (node.next.get(c) == null) {
			return false;
		}
		return recursionIsPrefix(node.next.get(c), prefix, index + 1);
	}
}
