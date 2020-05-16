package com.demo03.linkedlist01;

public class SingleLoopLinkedListTest {
	public static void main(String[] args) {

		SingleLoopLinkedList loopList = new SingleLoopLinkedList();
		LoopNode n1 = new LoopNode(1);
		LoopNode n2 = new LoopNode(2);
		LoopNode n3 = new LoopNode(3);
		LoopNode n4 = new LoopNode(4);
		LoopNode n5 = new LoopNode(5);
		loopList.add(n1);
		loopList.add(n2);
		loopList.add(n3);
		loopList.add(n4);
		loopList.add(n5);
		loopList.show();
		loopList.josepfuGame(5, 1, 3);
	}

}

class SingleLoopLinkedList {
	private LoopNode head = null;

	// Լɪ��
	public void josepfuGame(int total, int start, int count) {
		if (head == null || start < 1 || start > total) {
			System.out.println("����Ĳ�������ȷ��");
			return;
		}
		LoopNode curr = head;

		// ����ָ���Ƶ����
		while (true) {
			if (curr.next == head) {
				break;
			}
			curr = curr.next;
		}

		// ��ָ���Ƶ���ʼ�ڵ�
		for (int i = 0; i < start - 1; i++) {
			head = head.next;
			curr = curr.next;
		}

		// ��Ȧ
		while (true) {
			if (curr == head) {
				break;
			}
			for (int j = 0; j < count - 1; j++) {
				head = head.next;
				curr = curr.next;
			}
			System.out.println("��Ȧ��" + head);
			head = head.next;
			curr.next = head;
		}
		System.out.println("�������Ȧ�еĽڵ��ǣ�" + head);
	}

	public void add(LoopNode loopNode) {
		if (head == null) {
			head = loopNode;
			head.next = head;
			return;
		}
		LoopNode curr = head;
		while (true) {
			if (curr.next == head) {
				break;
			}
			curr = curr.next;
		}
		curr.next = loopNode;
		loopNode.next = head;
	}

	public void show() {
		if (head == null) {
			System.out.println("����Ϊ�գ�");
			return;
		}
		LoopNode curr = head;
		while (true) {
			System.out.println(curr);
			if (curr.next == head) {
				break;
			}
			curr = curr.next;
		}
	}

}

class LoopNode {
	public int data;
	public LoopNode next;

	public LoopNode(int data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "LoopNode [data = " + data + "]";
	}
}