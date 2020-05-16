package com.demo03.linkedlist01;

public class DoubleLoopLinkedListTest {
	public static void main(String[] args) {
		DoubleLoopLinkedList loopList = new DoubleLoopLinkedList();
//		loopList.del(10);
		DoubleLoopNode n1 = new DoubleLoopNode(1);
		DoubleLoopNode n2 = new DoubleLoopNode(2);
		DoubleLoopNode n3 = new DoubleLoopNode(3);
		DoubleLoopNode n4 = new DoubleLoopNode(4);
		DoubleLoopNode n5 = new DoubleLoopNode(5);
		loopList.add(n1);
		loopList.add(n2);
		loopList.add(n3);
		loopList.add(n4);
		loopList.add(n5);
//		loopList.show();
		loopList.del(1);
		loopList.show();
		System.out.println(n1.pre);
		System.out.println(n5.next);
	}
}

class DoubleLoopLinkedList {
	private DoubleLoopNode head = null;

	public void add(DoubleLoopNode loopNode) {
		if (head == null) {
			head = loopNode;
			loopNode.pre = head;
			loopNode.next = head;
			return;
		}
		DoubleLoopNode curr = head;
		while (true) {
			if (curr.next == head) {
				break;
			}
			curr = curr.next;
		}
		curr.next = loopNode;
		loopNode.pre = curr;
		loopNode.next = head;
		head.pre = loopNode;
	}

	public void show() {
		if (head == null) {
			System.out.println("链表为空！");
			return;
		}
		DoubleLoopNode curr = head;
		while (true) {
			System.out.println(curr);
			if (curr.next == head) {
				break;
			}
			curr = curr.next;
		}
	}

	// 删除
	public void del(int data) {
		if (head == null) {
			System.out.println("链表为空！");
			return;
		}
		if (head.data == data) {
			head.pre.next = head.next;
			head.next.pre = head.pre;
			head = head.next;
			return;
		}
		DoubleLoopNode curr = head.next;
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
			curr.next.pre = curr.pre;
		} else {
			System.out.println("删除的节点不存在！");
		}
	}

}

class DoubleLoopNode {
	public int data;
	public DoubleLoopNode pre;
	public DoubleLoopNode next;

	public DoubleLoopNode(int data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "DoubleLoopNode [data = " + data + "]";
	}
}