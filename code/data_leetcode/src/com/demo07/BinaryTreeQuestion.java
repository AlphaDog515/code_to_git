package com.demo07;

import java.util.Deque;
import java.util.LinkedList;

import org.junit.Test;

public class BinaryTreeQuestion {

		/*	
		
		// 翻转二叉树		
		示例：
		输入：
		     4
		   /   \
		  2     7
		 / \   / \
		1   3 6   9
		
		输出：
		     4
		   /   \
		  7     2
		 / \   / \
		9   6 3   1
		*/
	@Test
	public void test_invertTree() {
		BinaryTree tree = new BinaryTree();

		TreeNode root = new TreeNode(4);
		TreeNode tn1 = new TreeNode(2);
		TreeNode tn2 = new TreeNode(7);
		root.left = tn1;
		root.right = tn2;

		TreeNode tn3 = new TreeNode(1);
		TreeNode tn4 = new TreeNode(3);
		tn1.left = tn3;
		tn1.right = tn4;

		TreeNode tn5 = new TreeNode(6);
		TreeNode tn6 = new TreeNode(9);
		tn2.left = tn5;
		tn2.right = tn6;

		tree.root = root;

//		tree.preOrder();
//		tree.invertTree(tree.root);
//		tree.preOrder();

//		int res = tree.getNumTreeNode(0, root, 0);
//		int res = tree.floor(root, 9);
		
		int res = tree.levelKNodes(3);
		
		System.out.println(res);
	}
}

class BinaryTree {

	TreeNode root;

	// 前序遍历
	public void preOrder() {
		if (root != null) {
			root.preOrder();
		}
	}

	// 翻转二叉树
	public TreeNode invertTree(TreeNode node) {
		if (node == null) {
			return null;
		}
		TreeNode left = invertTree(node.left);
		TreeNode right = invertTree(node.right);
		node.left = right;
		node.right = left;
		return node;
	}

	// 二叉树各层节点的个数
	public int getNumTreeNode(int n, TreeNode node, int num) {
		if (node == null) {
			return 0;
		}
		if (node.height() == n) {
			num++;
		}
		if (node.left != null) {
			getNumTreeNode(n, node.left, num);
		}
		if (node.right != null) {
			getNumTreeNode(n, node.right, num);
		}
		return num;
	}

	public int floor(TreeNode root, int x) {
		int level, m, n;
		if (root == null) {
			level = 0;
		} else if (root.value == x) {
			level = 1;
		} else {
			m = floor(root.left, x);
			n = floor(root.right, x);
			if (m == 0 && n == 0) {
				level = 0;
			} else {
				level = ((m > n) ? m : n) + 1;
			}
		}
		return level;
	}

	// 树的叶子节点数
	public int countleaf(TreeNode root) {
		int i;
		if (root == null)
			i = 0;
		else if ((root.left == null) && (root.right == null))
			i = 1;
		else
			i = countleaf(root.left) + countleaf(root.right);
		return i;
	}

	// 树的层级遍历
	public void levelPrint(TreeNode root) {
		if (root == null) {
			return;
		}
		Deque<TreeNode> queue = new LinkedList<>();
		TreeNode current = null;
		queue.offer(root);// 将根节点入队
		while (!queue.isEmpty()) {
			current = queue.poll();// 出队队头元素并访问
			System.out.println(current.value);
			if (current.left != null) { // 如果当前节点的左节点不为空入队
				queue.offer(current.left);
			} 
			if (current.right != null) { // 如果当前节点的右节点不为空，把右节点入队
				queue.offer(current.right);
			}
		}
	}

	public int levelKNodes(int k) {
		if (k < 0)
			return 0;
		return levelKNodes(root, k);
	}

	private int levelKNodes(TreeNode root, int k) {
		if (root == null)
			return 0;
		if (k == 1)
			return 1;// 根结点
		else
			return levelKNodes(root.left, k - 1) + levelKNodes(root.right, k - 1);
	}
}

class TreeNode {
	int value;
	TreeNode left;
	TreeNode right;

	TreeNode(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "TreeNode [value=" + value + "]";
	}

	// 前序遍历
	public void preOrder() {
		System.out.println(this);
		if (this.left != null) {
			this.left.preOrder();
		}
		if (this.right != null) {
			this.right.preOrder();
		}
	}

	// 返回当前节点的高度
	public int height() {
		return Math.max(left == null ? -1 : left.height(), right == null ? -1 : right.height()) + 1;
	}

}
