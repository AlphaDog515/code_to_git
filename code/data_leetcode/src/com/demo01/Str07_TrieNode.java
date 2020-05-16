package com.demo01;

public class Str07_TrieNode {

}
//	前缀树应用场景：自动补全，拼写检查，打字预测，IP路由
//	Trie 树的结点结构
//	Trie 树是一个有根的树，其结点具有以下字段：
//	最多 R 个指向子结点的链接，其中每个链接对应字母表数据集中的一个字母。
//	本文中假定 R 为 26，小写拉丁字母的数量。
//	布尔字段，以指定节点是对应键的结尾还是只是键前缀。

class TrieNode {

	// R links to node children
	private TrieNode[] links;

	private final int R = 26;

	private boolean isEnd;  // 是否是一个单词的结尾，默认false

	public TrieNode() {
		links = new TrieNode[R]; // 0 - 25
	}

	public boolean containsKey(char ch) {
		return links[ch - 'a'] != null;
	}

	public TrieNode get(char ch) {
		return links[ch - 'a'];
	}

	public void put(char ch, TrieNode node) {
		links[ch - 'a'] = node; // 将node添加到ch的下面
	}

	public void setEnd() {
		isEnd = true;
	}

	public boolean isEnd() {
		return isEnd;
	}

	//  以下代码是在str06中调用
	//  非空子节点的数量
	private int size;
	public void put1(char ch, TrieNode node) {
		links[ch - 'a'] = node;
		size++;
	}
	public int getLinks() {
		return size;
	}

}

//	Trie 树中最常见的两个操作是键的插入和查找。
//	向 Trie 树中插入键
//	我们通过搜索 Trie 树来插入一个键。我们从根开始搜索它对应于第一个键字符的链接。有两种情况：
//	链接存在。沿着链接移动到树的下一个子层。算法继续搜索下一个键字符。
//	链接不存在。创建一个新的节点，并将它与父节点的链接相连，该链接与当前的键字符相匹配。
//	重复以上步骤，直到到达键的最后一个字符，然后将当前节点标记为结束节点，算法完成。

class Trie {
	private TrieNode root;

	public Trie() {
		root = new TrieNode();
	}

	// Inserts a word into the trie.
	public void insert(String word) {
		TrieNode node = root;
		for (int i = 0; i < word.length(); i++) {
			char currentChar = word.charAt(i);
			if (!node.containsKey(currentChar)) {
				node.put1(currentChar, new TrieNode()); // 原生树调用put Str06调用put
			}
			node = node.get(currentChar);
		}
		node.setEnd();
	}

	// search a prefix or whole key in trie and
	// returns the node where search ends
	private TrieNode searchPrefix(String word) {
		TrieNode node = root;
		for (int i = 0; i < word.length(); i++) {
			char curLetter = word.charAt(i);
			if (node.containsKey(curLetter)) {
				node = node.get(curLetter);
			} else {
				return null;
			}
		}
		return node;
	}

	// Returns if the word is in the trie.
	public boolean search(String word) {
		TrieNode node = searchPrefix(word);
		return node != null && node.isEnd();
	}

	// Returns if there is any word in the trie
	// that starts with the given prefix.
	public boolean startsWith(String prefix) {
		TrieNode node = searchPrefix(prefix);
		return node != null;
	}

	// 下面是Str06的方法
	// 假设方法 insert、search、searchPrefix 都已经实现了
	// 可以参考文章：https://leetcode.com/articles/implement-trie-prefix-tree/
	public String searchLongestPrefix(String word) {
		TrieNode node = root;
		StringBuilder prefix = new StringBuilder();
		for (int i = 0; i < word.length(); i++) {
			char curLetter = word.charAt(i);
			if (node.containsKey(curLetter) && (node.getLinks() == 1) && (!node.isEnd())) {
				prefix.append(curLetter);
				node = node.get(curLetter);
			} else
				return prefix.toString();

		}
		return prefix.toString();
	}

}

//	每个键在 trie 中表示为从根到内部节点或叶的路径。我们用第一个键字符从根开始，
//  检查当前节点中与键字符对应的链接。有两种情况：	
//	存在链接。我们移动到该链接后面路径中的下一个节点，并继续搜索下一个键字符。
//	不存在链接。若已无键字符，且当前结点标记为 isEnd，则返回 true。否则有两种可能，均返回 false :
//		还有键字符剩余，但无法跟随 Trie 树的键路径，找不到键。
//		没有键字符剩余，但当前结点没有标记为 isEnd。也就是说，待查找键只是Trie树中另一个键的前缀。

//  查找 Trie 树中的键前缀
//  该方法与在 Trie 树中搜索键时使用的方法非常相似。我们从根遍历 Trie 树，直到键前缀中没有字符，
//  或者无法用当前的键字符继续 Trie 中的路径。与上面提到的“搜索键”算法唯一的区别是，到达键前缀的末尾时，
//  总是返回 true。我们不需要考虑当前 Trie 节点是否用 “isend” 标记，因为我们搜索的是键的前缀，而不是整个键。
