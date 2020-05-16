package com.demo05;

import java.util.ArrayList;
import java.util.List;

public class List01_AddTwoNum {
	
/* 
	给出两个非空的链表用来表示两个非负的整数。
	其中，它们各自的位数是按照 逆序 的方式存储的，并且它们的每个节点只能存储 一位 数字。
	如果，我们将这两个数相加起来，则会返回一个新的链表来表示它们的和。
	您可以假设除了数字 0 之外，这两个数都不会以 0 开头。
	
	示例：
	输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
	输出：7 -> 0 -> 8
	原因：342 + 465 = 807 
 */
	
	public static void main(String[] args) {
		ListNode01 n1 = new ListNode01(2);
		ListNode01 n2 = new ListNode01(4);
		ListNode01 n3 = new ListNode01(3);
		n1.appendNode(n2);
		n1.appendNode(n3);

		ListNode01 n4 = new ListNode01(5);
		ListNode01 n5 = new ListNode01(6);
		ListNode01 n6 = new ListNode01(4);
		n4.appendNode(n5);
		n4.appendNode(n6);

		Solution_01 solution = new Solution_01();
		ListNode01 resNode = solution.addTwoNumbers(n1, n4);
		List<ListNode01> list = resNode.show();
		System.out.println(list);
	}
}

class ListNode01 {
	int data;
	ListNode01 next;

	ListNode01(int data) {
		this.data = data;
	}

	public void appendNode(ListNode01 listNode) {
		ListNode01 currentNode = this;
		ListNode01 nextNode = currentNode.next;
		while (nextNode != null) {
			currentNode = nextNode;
			nextNode = currentNode.next;
		}
		currentNode.next = listNode;
	}

	public void appendNodeError(ListNode01 listNode) {
		ListNode01 nextNode = this.next;
		while (nextNode != null) {
			nextNode = nextNode.next;
		}
		nextNode = listNode;// 这样遍历前一个节点找不到后面一个节点
	}

	public List<ListNode01> show() {
		List<ListNode01> list = new ArrayList<>();
		list.add(this);
		ListNode01 node = this.next;
		while (node != null) {
			list.add(node);
			node = node.next;
		}
		return list;
	}

	@Override
	public String toString() {
		return "ListNode [data=" + data + "]";
	}

}

class Solution_01 {
	/*	 
	1.将两个链表看成是相同长度的进行遍历，如果一个链表较短则在前面补 00，比如 987 + 23 = 987 + 023 = 1010
	2.每一位计算的同时需要考虑上一位的进位问题，而当前位计算结束后同样需要更新进位值
	3.如果两个链表全部遍历完毕后，进位值为 1，则在新链表最前方添加节点 1
	
	小技巧：对于链表问题，返回结果为头结点时，通常需要先初始化一个预先指针 pre，
	该指针的下一个节点指向真正的头结点head。
	使用预先指针的目的在于链表初始化时无可用节点值，
	而且链表构造过程需要指针移动，进而会导致头指针丢失，无法返回结果。
		987 + 23 = 1010
pre->	7 -> 8 -> 9
pre->	3 -> 2 -> 0 [最后一位补0]
pre->	0 -> 1 -> 0 -> 1

	 */	
	public ListNode01 addTwoNumbers(ListNode01 n1, ListNode01 n2) {
		ListNode01 pre = new ListNode01(0); // 创建一个节点
		ListNode01 cur = pre;
		int carry = 0; // 进位值
		while (n1 != null || n2 != null) {
			int x = n1 == null ? 0 : n1.data;
			int y = n2 == null ? 0 : n2.data;
			int sum = x + y + carry;

			carry = sum / 10; // 求出和的十位数
			sum = sum % 10;
			cur.next = new ListNode01(sum);

			cur = cur.next;
			if (n1 != null)
				n1 = n1.next;
			if (n2 != null)
				n2 = n2.next;
		}
		if (carry == 1) {
			cur.next = new ListNode01(carry);
		}
		return pre.next;
	}
}
