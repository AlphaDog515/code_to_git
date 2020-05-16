package com.demo03.linkedlist01;

public class DoubleLinkedListTest {

	public static void main(String[] args) {
		DoubleLinkedList list = new DoubleLinkedList();
		DoubleNode d1 = new DoubleNode(10);
		DoubleNode d2 = new DoubleNode(11);
		DoubleNode d3 = new DoubleNode(12);
		DoubleNode d4 = new DoubleNode(13);
		list.add(d1);
		list.add(d2);
		list.add(d3);
		list.add(d4);
		list.show();
		list.del(10);
		list.show();
	}

}

class DoubleLinkedList {
	private DoubleNode head = new DoubleNode();

	public void show() {
		if (head.next == null) {
			System.out.println("链表为空！");
			return;
		}
		DoubleNode curr = head.next;
		while (true) {
			if (curr == null) {
				break;
			}
			System.out.println(curr);
			curr = curr.next;
		}
	}

	// 添加
	public void add(DoubleNode doubleNode) {
		DoubleNode curr = head;
		while (true) {
			if (curr.next == null) {
				break;
			}
			curr = curr.next;
		}
		curr.next = doubleNode;
		doubleNode.pre = curr;
	}

	// 删除
	public void del(int data) {
		if (head.next == null) {
			System.out.println("链表为空！");
			return;
		}
		DoubleNode curr = head.next;
		boolean flag = false;
		while (true) {
			if (curr == null) {
				break;
			}
			if (curr.data == data) {
				flag = true;
				break;
			}
			curr = curr.next;
		}

		if (flag) {
			curr.pre.next = curr.next;
			if (curr.next != null) {
				curr.next.pre = curr.pre;
			}
		} else {
			System.out.println("删除的节点不存在！");
		}

	}

	// 更新
	public void update(DoubleNode doubleNode) {
		if (head.next == null) {
			System.out.println("链表为空！");
			return;
		}
		DoubleNode curr = head.next;
		boolean flag = false;
		while (true) {
			if (curr == null) {
				break;
			}
			if (curr.data == doubleNode.data) {
				flag = true;
				break;
			}
			curr = curr.next;
		}
		if (flag) {
			curr.data = doubleNode.data; // 需要其他信息才有意义
		} else {
			System.out.println("要修改的节点不存在！");
		}
	}

}

class DoubleNode {
	public int data;
	public DoubleNode pre;
	public DoubleNode next;

	public DoubleNode() {
	}

	public DoubleNode(int data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "DoubleNode [data = " + data + "]";
	}
}