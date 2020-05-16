package com.demo00;

public class Test02_RBTree {
	public static void main(String[] args) {
		RBTree tree = new RBTree();

		// 12 1 9 2 0 11 7 19 4 15 18 5 14 13 10 16 6 3 8 17
		int[] arr = { 12, 1, 9, 2, 0, 11, 7, 19, 4, 15, 18, 5, 14, 13, 10, 16, 6, 3, 8, 17 };
		for (int i = 0; i < 20; i++) {

			tree.insert(arr[i]);

		}
//		tree.inOrder();
		tree.delete(12);
		tree.inOrder();
	}
}

class RBTree {
	RBTreeNode root;
	private final boolean RED = false;
	private final boolean BLACK = true;

	public RBTreeNode query(int key) {
		RBTreeNode tmp = root;
		while (tmp != null) {
			if (tmp.getKey() == key)
				return tmp;
			else if (tmp.getKey() > key)
				tmp = tmp.getLeft();
			else
				tmp = tmp.getRight();
		}
		return null;
	}

	public void insert(int key) {
		RBTreeNode node = new RBTreeNode(key);
		if (root == null) {
			root = node;
			node.setColor(BLACK);
			return;
		}
		RBTreeNode parent = root;
		RBTreeNode son = null;
		if (key <= parent.getKey()) {
			son = parent.getLeft();
		} else {
			son = parent.getRight();
		}
		// find the position
		while (son != null) {
			parent = son;
			if (key <= parent.getKey()) {
				son = parent.getLeft();
			} else {
				son = parent.getRight();
			}
		}
		if (key <= parent.getKey()) {
			parent.setLeft(node);
		} else {
			parent.setRight(node);
		}
		node.setParent(parent);

		// fix up
		insertFix(node);
	}

	private void insertFix(RBTreeNode node) {
		RBTreeNode father, grandFather;
		while ((father = node.getParent()) != null && father.getColor() == RED) {
			grandFather = father.getParent();
			if (grandFather.getLeft() == father) { // FΪG����ӵ��������֮ǰ�ķ���
				RBTreeNode uncle = grandFather.getRight();
				if (uncle != null && uncle.getColor() == RED) {
					setBlack(father);
					setBlack(uncle);
					setRed(grandFather);
					node = grandFather;
					continue;
				}
				if (node == father.getRight()) {
					leftRotate(father);
					RBTreeNode tmp = node;
					node = father;
					father = tmp;
				}
				setBlack(father);
				setRed(grandFather);
				rightRotate(grandFather);
			} else { // FΪG���Ҷ��ӵ�������ԳƲ���
				RBTreeNode uncle = grandFather.getLeft();
				if (uncle != null && uncle.getColor() == RED) {
					setBlack(father);
					setBlack(uncle);
					setRed(grandFather);
					node = grandFather;
					continue;
				}
				if (node == father.getLeft()) {
					rightRotate(father);
					RBTreeNode tmp = node;
					node = father;
					father = tmp;
				}
				setBlack(father);
				setRed(grandFather);
				leftRotate(grandFather);
			}
		}
		setBlack(root);
	}

	public void delete(int key) {
		delete(query(key));
	}

	private void delete(RBTreeNode node) {
		if (node == null)
			return;
		if (node.getLeft() != null && node.getRight() != null) {
			RBTreeNode replaceNode = node;
			RBTreeNode tmp = node.getRight();
			while (tmp != null) {
				replaceNode = tmp;
				tmp = tmp.getLeft();
			}
			int t = replaceNode.getKey();
			replaceNode.setKey(node.getKey());
			node.setKey(t);
			delete(replaceNode);
			return;
		}
		RBTreeNode replaceNode = null;
		if (node.getLeft() != null)
			replaceNode = node.getLeft();
		else
			replaceNode = node.getRight();

		RBTreeNode parent = node.getParent();
		if (parent == null) {
			root = replaceNode;
			if (replaceNode != null)
				replaceNode.setParent(null);
		} else {
			if (replaceNode != null)
				replaceNode.setParent(parent);
			if (parent.getLeft() == node)
				parent.setLeft(replaceNode);
			else {
				parent.setRight(replaceNode);
			}
		}
		if (node.getColor() == BLACK)
			removeFix(parent, replaceNode);

	}

	// �������ɫ��node��
	private void removeFix(RBTreeNode father, RBTreeNode node) {
		while ((node == null || node.getColor() == BLACK) && node != root) {
			if (father.getLeft() == node) { // SΪP������ӵ��������֮ǰ�ķ���
				RBTreeNode brother = father.getRight();
				if (brother != null && brother.getColor() == RED) {
					setRed(father);
					setBlack(brother);
					leftRotate(father);
					brother = father.getRight();
				}
				if (brother == null || (isBlack(brother.getLeft()) && isBlack(brother.getRight()))) {
					setRed(brother);
					node = father;
					father = node.getParent();
					continue;
				}
				if (isRed(brother.getLeft())) {
					setBlack(brother.getLeft());
					setRed(brother);
					rightRotate(brother);
					brother = brother.getParent();
				}

				brother.setColor(father.getColor());
				setBlack(father);
				setBlack(brother.getRight());
				leftRotate(father);
				node = root;// ����ѭ��
			} else { // SΪP���Ҷ��ӵ�������ԳƲ���
				RBTreeNode brother = father.getLeft();
				if (brother != null && brother.getColor() == RED) {
					setRed(father);
					setBlack(brother);
					rightRotate(father);
					brother = father.getLeft();
				}
				if (brother == null || (isBlack(brother.getLeft()) && isBlack(brother.getRight()))) {
					setRed(brother);
					node = father;
					father = node.getParent();
					continue;
				}
				if (isRed(brother.getRight())) {
					setBlack(brother.getRight());
					setRed(brother);
					leftRotate(brother);
					brother = brother.getParent();
				}

				brother.setColor(father.getColor());
				setBlack(father);
				setBlack(brother.getLeft());
				rightRotate(father);
				node = root;// ����ѭ��
			}
		}

		if (node != null)
			node.setColor(BLACK);
	}

	private boolean isBlack(RBTreeNode node) {
		if (node == null)
			return true;
		return node.getColor() == BLACK;
	}

	private boolean isRed(RBTreeNode node) {
		if (node == null)
			return false;
		return node.getColor() == RED;
	}

	private void leftRotate(RBTreeNode node) {
		RBTreeNode right = node.getRight();
		RBTreeNode parent = node.getParent();
		if (parent == null) {
			root = right;
			right.setParent(null);
		} else {
			if (parent.getLeft() != null && parent.getLeft() == node) {
				parent.setLeft(right);
			} else {
				parent.setRight(right);
			}
			right.setParent(parent);
		}
		node.setParent(right);
		node.setRight(right.getLeft());
		if (right.getLeft() != null) {
			right.getLeft().setParent(node);
		}
		right.setLeft(node);
	}

	private void rightRotate(RBTreeNode node) {
		RBTreeNode left = node.getLeft();
		RBTreeNode parent = node.getParent();
		if (parent == null) {
			root = left;
			left.setParent(null);
		} else {
			if (parent.getLeft() != null && parent.getLeft() == node) {
				parent.setLeft(left);
			} else {
				parent.setRight(left);
			}
			left.setParent(parent);
		}
		node.setParent(left);
		node.setLeft(left.getRight());
		if (left.getRight() != null) {
			left.getRight().setParent(node);
		}
		left.setRight(node);
	}

	private void setBlack(RBTreeNode node) {
		node.setColor(BLACK);
	}

	private void setRed(RBTreeNode node) {
		node.setColor(RED);
	}

	public void inOrder() {
		inOrder(root);
	}

	private void inOrder(RBTreeNode node) {
		if (node == null)
			return;
		inOrder(node.getLeft());
		System.out.println(node);
		inOrder(node.getRight());
	}
}

class RBTreeNode {
	private final boolean RED = false;
	private final boolean BLACK = true;

	private int key;
	private boolean color;

	private RBTreeNode left;
	private RBTreeNode right;
	private RBTreeNode parent;

	public RBTreeNode(int key) {
		this.key = key;
		this.color = RED;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public boolean getColor() {
		return color;
	}

	public void setColor(boolean color) {
		this.color = color;
	}

	public RBTreeNode getLeft() {
		return left;
	}

	public void setLeft(RBTreeNode left) {
		this.left = left;
	}

	public RBTreeNode getRight() {
		return right;
	}

	public void setRight(RBTreeNode right) {
		this.right = right;
	}

	public RBTreeNode getParent() {
		return parent;
	}

	public void setParent(RBTreeNode parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return "RBTreeNode{" + "key=" + key + ", color=" + color + '}';
	}
}
