package com.demo03.linkedlist01;

import java.util.Stack;

public class SingleLinkedListTest {
	public static void main(String[] args) {
		SingleLinkedList list = new SingleLinkedList();
		Node n1 = new Node(12);
		Node n2 = new Node(3);
		Node n3 = new Node(5);
		Node n4 = new Node(10);
		list.addByOrder(n1);
		list.addByOrder(n2);
		list.addByOrder(n3);
		list.addByOrder(n4);
		// list.del(1);
//		list.show();
//		System.out.println(list.findNode(n2));
//		System.out.println(list.getLength());
//		list.update(n4);
//		System.out.println(list.findLastKNode(3));
//		list.reverseList();
		list.reversePrint();
		list.show();

	}
}

class SingleLinkedList {
	private Node head = new Node(); // 不要使用null

	// 单链表反转
	public void reverseList() {
		if (head.next == null || head.next.next == null) {
			return;
		}
		Node curr = head.next;
		Node next = null;
		Node reverseHead = new Node();
		while (curr != null) {
			next = curr.next;
			curr.next = reverseHead.next;
			reverseHead.next = curr;
			curr = next;
		}
		head.next = reverseHead.next;
	}

	// 逆序打印
	public void reversePrint() {
		if (head.next == null) {
			return;
		}

		Stack<Node> stack = new Stack<>();
		Node curr = head.next;
		while (curr != null) {
			stack.push(curr);
			curr = curr.next;
		}
		while (stack.size() > 0) {
			System.out.println(stack.pop());
		}
	}

	// 查找单链表倒数第k个节点
	public Node findLastKNode(int index) {
		if (head.next == null) {
			return null;
		}
		int size = getLength();
		if (index <= 0 || index > size) {
			return null;
		}
		Node curr = head.next;
		for (int i = 0; i < size - index; i++) {
			curr = curr.next;
		}
		return curr;
	}

	// 获取单链表有效节点个数
	public int getLength() {
		if (head.next == null) {
			return 0;
		}
		Node curr = head.next;
		int length = 0;
		while (curr != null) {
			length++;
			curr = curr.next;
		}
		return length;

	}

	public void add(Node node) {
		Node curr = head;
		while (true) {
			if (curr.next == null) {
				break;
			}
			curr = curr.next;
		}
		curr.next = node;
	}

	public void addByOrder(Node node) {
		Node curr = head;
		boolean flag = false;
		while (true) {
			if (curr.next == null) {
				break;
			}
			if (curr.next.data > node.data) {
				break;
			} else if (curr.next.data == node.data) {
				flag = true;
				break;
			}
			curr = curr.next;

		}
		if (flag) {
			System.out.println("节点已经存在！");
		} else {
			node.next = curr.next;
			curr.next = node; // 注意顺序
		}
	}

	public void del(int data) {
		Node curr = head;
		boolean flag = false;
		while (true) {
			if (curr.next == null) {
				break;
			}
			if (curr.next.data == data) {
				flag = true;
				break;
			}
			curr = curr.next;
		}

		if (flag) {
			curr.next = curr.next.next;
		} else {
			System.out.printf("要删除的节点%d不存在！", data);
		}
	}

	public boolean findNode(Node node) {
		Node curr = head;
		boolean flag = false;
		while (true) {
			if (curr.next == null) {
				break;
			}
			if (curr.next.data == node.data) {
				flag = true;
				break;
			}
			curr = curr.next;
		}
		return flag;
	}

	public void update(Node node) {
		if (head.next == null) {
			System.out.println("链表为空！");
			return;
		}
		Node curr = head.next;

		boolean flag = false;
		while (true) {
			if (curr == null) {
				break;
			}
			if (curr.data == node.data) {
				flag = true;
				break;
			}
			curr = curr.next;
		}
		if (flag) {
			curr.data = node.data; // 节点中有别的信息才有意义
		} else {
			System.out.println("要更新的节点不存在！");
		}
	}

	public void show() {
		if (head.next == null) {
			System.out.println("链表为空！");
			return;
		}
		Node curr = head.next;
		while (true) {
			if (curr == null) {
				break;
			}
			System.out.println(curr);
			curr = curr.next;
		}
	}

}

class Node {
	public int data;
	public Node next;

	public Node() {
	}

	public Node(int data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Node [data = " + data + "]";
	}
}