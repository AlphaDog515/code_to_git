package com.demo04;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;



public class Structure01_LRUCache {
	public static void main(String[] args) {
		LRUCache cache = new LRUCache(2); // 缓存容量
		cache.put(1, 1);
		cache.put(2, 2);
		cache.get(1); // 返回 1
		cache.put(3, 3); // 该操作会使得密钥 2 作废
		cache.get(2); // 返回 -1 (未找到)
		cache.put(4, 4); // 该操作会使得密钥 1 作废
		cache.get(1); // 返回 -1 (未找到)
		cache.get(3); // 返回 3
		cache.get(4); // 返回 4
	}
}
/*
	运用你所掌握的数据结构，设计和实现一个  LRU (最近最少使用) 缓存机制。
	它应该支持以下操作： 获取数据 get 和 写入数据 put 。	
	获取数据 get(key) - 如果密钥 (key) 存在于缓存中，则获取密钥的值（总是正数），否则返回 -1。
	写入数据 put(key, value) - 如果密钥不存在，则写入其数据值。
	当缓存容量达到上限时，它应该在写入新数据之前删除最近最少使用的数据值，从而为新的数据值留出空间。
	
	进阶:	
	你是否可以在 O(1) 时间复杂度内完成这两种操作？
	
	示例:	
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

	// LRU（The Least Recently Used，最近最久未使用算法）
	// LFU（Least Frequently Used，最近最少使用算法）
	// 将出现频率加入，频率高的在队头，低的在队尾，如果队列满了，新加一个队尾的元素会被删除
 */

// 有一种叫做有序字典的数据结构，综合了哈希表和链表，在 Python中为 OrderedDict，在 Java 中为 LinkedHashMap。

class LRUCache_1 extends LinkedHashMap<Integer, Integer> {
	private int capacity;

	public LRUCache_1(int capacity) {
		super(capacity, 0.75F, true);
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
		return size() > capacity;
	}
}

/**
 * LRUCache 对象会以如下语句构造和调用:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */


class LRUCache {

	class DLinkedNode {
		int key;
		int value;
		DLinkedNode prev;
		DLinkedNode next;
	}

	private void addNode(DLinkedNode node) {
		/**
		 * Always add the new node right after head.
		 */
		node.prev = head;
		node.next = head.next;

		head.next.prev = node;
		head.next = node;
	}

	private void removeNode(DLinkedNode node) {
		/**
		 * Remove an existing node from the linked list.
		 */
		DLinkedNode prev = node.prev;
		DLinkedNode next = node.next;

		prev.next = next;
		next.prev = prev;
	}

	private void moveToHead(DLinkedNode node) {
		/**
		 * Move certain node in between to the head.
		 */
		removeNode(node);
		addNode(node);
	}

	private DLinkedNode popTail() {
		/**
		 * Pop the current tail.
		 */
		DLinkedNode res = tail.prev;
		removeNode(res);
		return res;
	}

	private Hashtable<Integer, DLinkedNode> cache = new Hashtable<Integer, DLinkedNode>();
	private int size;
	private int capacity;
	private DLinkedNode head, tail;

	public LRUCache(int capacity) {
		this.size = 0;
		this.capacity = capacity;

		head = new DLinkedNode();
		// head.prev = null;

		tail = new DLinkedNode();
		// tail.next = null;

		head.next = tail;
		tail.prev = head;
	}

	public int get(int key) {
		DLinkedNode node = cache.get(key);
		if (node == null)
			return -1;

		// move the accessed node to the head;
		moveToHead(node);

		return node.value;
	}

	public void put(int key, int value) {
		DLinkedNode node = cache.get(key);

		if (node == null) {
			DLinkedNode newNode = new DLinkedNode();
			newNode.key = key;
			newNode.value = value;

			cache.put(key, newNode);
			addNode(newNode);

			++size;

			if (size > capacity) {
				// pop the tail
				DLinkedNode tail = popTail();
				cache.remove(tail.key);
				--size;
			}
		} else {
			// update the value.
			node.value = value;
			moveToHead(node);
		}
	}
}

// 哈希表 + 双向链表
// 节点类
class Node {
	public int key, val;
	public Node next, prev;

	public Node(int k, int v) {
		this.key = k;
		this.val = v;
	}
}

// 构建双链表
class DoubleList {
	private Node head, tail; // 头尾虚节点
	private int size; // 链表元素数

	public DoubleList() {
		head = new Node(0, 0);
		tail = new Node(0, 0);
		head.next = tail;
		tail.prev = head;
		size = 0;
	}

	// 在链表头部添加节点 x
	public void addFirst(Node x) {
		x.next = head.next;
		x.prev = head;
		head.next.prev = x;
		head.next = x;
		size++;
	}

	// 删除链表中的 x 节点（x 一定存在）
	public void remove(Node x) {
		x.prev.next = x.next;
		x.next.prev = x.prev;
		size--;
	}

	// 删除链表中最后一个节点，并返回该节点
	public Node removeLast() {
		if (tail.prev == head)
			return null;
		Node last = tail.prev;
		remove(last);
		return last;
	}

	// 返回链表长度
	public int size() {
		return size;
	}
}

class LRUCache_2 {
	// key -> Node(key, val)
	private HashMap<Integer, Node> map;
	// Node(k1, v1) <-> Node(k2, v2)...
	private DoubleList cache;
	// 最大容量
	private int cap;

	public LRUCache_2(int capacity) {
		this.cap = capacity;
		map = new HashMap<>();
		cache = new DoubleList();
	}

	public int get(int key) {
		if (!map.containsKey(key))
			return -1;
		int val = map.get(key).val;
		// 利用 put 方法把该数据提前
		put(key, val);
		return val;
	}

	public void put(int key, int val) {
		// 先把新节点 x 做出来
		Node x = new Node(key, val);

		if (map.containsKey(key)) {
			// 删除旧的节点，新的插到头部
			cache.remove(map.get(key));
			cache.addFirst(x);
			// 更新 map 中对应的数据
			map.put(key, x);
		} else {
			if (cap == cache.size()) {
				// 删除链表最后一个数据
				Node last = cache.removeLast();
				map.remove(last.key);
			}
			// 直接添加到头部
			cache.addFirst(x);
			map.put(key, x);
		}
	}
}
// 当缓存容量已满，我们不仅仅要删除最后一个 Node 节点，还要把 map 中映射到该节点的 key 同时删除，
// 而这个 key 只能由 Node 得到。如果 Node 结构中只存储 val，那么我们就无法得知 key 是什么，
// 就无法删除 map 中的键，造成错误。

