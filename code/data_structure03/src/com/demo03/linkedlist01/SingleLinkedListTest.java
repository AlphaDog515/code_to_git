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
	private Node head = new Node(); // ��Ҫʹ��null

	// ������ת
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

	// �����ӡ
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

	// ���ҵ���������k���ڵ�
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

	// ��ȡ��������Ч�ڵ����
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
			System.out.println("�ڵ��Ѿ����ڣ�");
		} else {
			node.next = curr.next;
			curr.next = node; // ע��˳��
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
			System.out.printf("Ҫɾ���Ľڵ�%d�����ڣ�", data);
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
			System.out.println("����Ϊ�գ�");
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
			curr.data = node.data; // �ڵ����б����Ϣ��������
		} else {
			System.out.println("Ҫ���µĽڵ㲻���ڣ�");
		}
	}

	public void show() {
		if (head.next == null) {
			System.out.println("����Ϊ�գ�");
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