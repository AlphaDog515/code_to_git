package com.demo05;

// 合并k个排序链表，返回合并后的排序链表
// 输入:[1->4->5,1->3->4,2->6]
// 输出: 1->1->2->3->4->4->5->6
// 定义单链表
class ListNode02 {
	int val;
	ListNode02 next;

	ListNode02(int x) {
		val = x;
	}

	// 为节点追回节点
	public ListNode02 append(ListNode02 node) {
		// 当前节点
		ListNode02 currentNode = this;
		// 循环向后找
		while (true) {
			// 取出下一个节点
			ListNode02 nextNode = currentNode.next;
			// 如果下一个节点为null,当前节点已经是最后一个节点
			if (nextNode == null) {
				break;
			}
			// 赋给当前节点
			currentNode = nextNode;
		}
		// 把需要追回的节点追加为找到的当前节点的下一个节点
		currentNode.next = node;
		return this;
	}

	// 显示所有节点信息
	public void show() {
		ListNode02 currentNode = this;
		while (true) {
			System.out.print(currentNode.val + " ");
			// 取出下一个节点
			currentNode = currentNode.next;
			// 如果是最后一个节点
			if (currentNode == null) {
				break;
			}
		}
		System.out.println();
	}

}

public class List02_MergeKLists {
	public ListNode02 mergeKLists(ListNode02[] lists) {
		if (lists.length == 0)
			return null; // 空，返回null
		return solve(lists, 0, lists.length - 1);
	}

	private ListNode02 solve(ListNode02[] arr, int left, int right) {
		if (left == right)
			return arr[left];
		// << : 左移运算符，num << 1,相当于num乘以2
		// >> : 右移运算符，num >> 1,相当于num除以2
		int mid = (left + right) >> 1; // mid=(left+right)/2
		ListNode02 lNode = solve(arr, left, mid); // 处理左边
		ListNode02 rNode = solve(arr, mid + 1, right); // 处理右边

		return merge(lNode, rNode);
	}

	// 处理两个，如果有一个为空直接返回另外一个即可
	private ListNode02 merge(ListNode02 node1, ListNode02 node2) {
		if (node1 == null)
			return node2;
		if (node2 == null)
			return node1;

		if (node1.val < node2.val) {
			node1.next = merge(node1.next, node2);
			return node1;
		} else {
			node2.next = merge(node1, node2.next);
			return node2;
		}
	}

	// 测试程序，创建节点，将单链表的首元素赋值给ListNode类型的数组
	// 调用方法测试
	public static void main(String[] args) {
		ListNode02 n1 = new ListNode02(1);
		ListNode02 n2 = new ListNode02(3);
		ListNode02 n3 = new ListNode02(4);
		ListNode02 n4 = new ListNode02(2);
		ListNode02 n5 = new ListNode02(3);
		ListNode02 n6 = new ListNode02(5);
		ListNode02 n7 = new ListNode02(0);
		ListNode02 n8 = new ListNode02(9);
		n1.append(n2); // n1 1 3 4
		// n1.append(n3); // n4 2 3 5
//		n4.append(n5);
//		n4.append(n6);
//		n7.append(n8);
		
		List02_MergeKLists mk = new List02_MergeKLists();
		ListNode02 re = mk.merge(n1, n4);
		re.show();
		
//		ListNode[] lists = { n1, n4, n7 };
//		ListNode result = mk.mergeKLists(lists);
//		result.show();
	}
}
