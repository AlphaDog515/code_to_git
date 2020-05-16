package com.demo15.tree01;

public class AVLTreeTest {
	public static void main(String[] args) {
		// int[] arr = {4,3,6,5,7,8};
		// int[] arr = { 10, 12, 8, 9, 7, 6 };
		int[] arr = { 10, 11, 7, 6, 8, 9 };

		AVLTree avlTree = new AVLTree();

		for (int i = 0; i < arr.length; i++) {
			avlTree.add(new AVLNode(arr[i]));
		}

		avlTree.infixOrder();

	}
}

class AVLTree {
	private AVLNode root;

	public AVLNode getRoot() {
		return root;
	}

	public void infixOrder() {
		if (root != null) {
			root.infixOrder();
		} else {
			System.out.println("二叉树为空！");
		}
	}

	public void add(AVLNode node) {
		if (root == null) {
			root = node;
		} else {
			root.add(node);
		}
	}

}

class AVLNode {
	public int value;
	public AVLNode left;
	public AVLNode right;

	public AVLNode(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "AVLNode [value=" + value + "]";
	}

	// 中序遍历
	public void infixOrder() {
		if (this.left != null) {
			this.left.infixOrder();
		}
		System.out.println(this);
		if (this.right != null) {
			this.right.infixOrder();
		}
	}

	// 返回当前节点的高度
	public int height() {
		return Math.max(left == null ? 0 : left.height(), right == null ? 0 : right.height()) + 1;
	}

	public int leftHeight() {
		if (left == null) {
			return 0;
		}
		return left.height();
	}

	public int rightHeight() {
		if (right == null) {
			return 0;
		}
		return right.height();
	}

	// 左旋
	private void leftRotate() {
		AVLNode newNode = new AVLNode(value);
		newNode.left = left;
		newNode.right = right.left;
		value = right.value;
		left = newNode;
		right = right.right;
	}

	// 右旋
	private void rightRotate() {
		AVLNode newNode = new AVLNode(value);
		newNode.left = left.right;
		newNode.right = right;
		value = left.value;
		left = left.left;
		right = newNode;
	}

	// 添加节点
	public void add(AVLNode node) {
		if (node == null) {
			return;
		}
		if (node.value < this.value) {
			if (this.left == null) {
				this.left = node;
			} else {
				this.left.add(node);
			}
		} else {
			if (this.right == null) {
				this.right = node;
			} else {
				this.right.add(node);
			}
		}

		// 平衡操作
		if (rightHeight() - leftHeight() > 1) {
			if (right.leftHeight() > right.rightHeight()) {
				right.rightRotate();
				leftRotate();
			} else {
				leftRotate();
			}
			return; // 结束
		}

		if (leftHeight() - rightHeight() > 1) {
			if (left.rightHeight() > left.leftHeight()) {
				left.leftRotate();
				rightRotate();
			} else {
				rightRotate();
			}
		}
	}

}
