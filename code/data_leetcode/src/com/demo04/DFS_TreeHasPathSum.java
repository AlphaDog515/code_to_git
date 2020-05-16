package com.demo04;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class DFS_TreeHasPathSum {

}

class Solution_01_01{
	/*
	给定一个二叉树和一个目标和，判断该树中是否存在根节点到叶子节点的路径，
	这条路径上所有节点值相加等于目标和。
	说明: 叶子节点是指没有子节点的节点。
	
	示例: 
	给定如下二叉树，以及目标和 sum = 22，
		
	              5
	             / \
	            4   8
	           /   / \
	          11  13  4
	         /  \      \
	        7    2      1
	        
	返回 true, 因为存在目标和为 22 的根节点到叶子节点的路径 5->4->11->2。	 
	 
	 */
	
	
	/* Definition for a binary tree node. */
	public class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;

		TreeNode(int x) {
			val = x;
		}
	}

	// 递归
	public boolean hasPathSum01(TreeNode root, int sum) {
		if (root == null)
			return false;
		sum -= root.val;
		if ((root.left == null) && (root.right == null))
			return (sum == 0);
		return hasPathSum01(root.left, sum) || hasPathSum01(root.right, sum);
	}

	public boolean hasPathSum02(TreeNode root, int sum) {
		Stack<TreeNode> toVisit = new Stack<>();
		TreeNode cur = root;
		TreeNode pre = null;
		int curSum = 0; // 记录当前的累计的和
		while (cur != null || !toVisit.isEmpty()) {
			while (cur != null) {
				toVisit.push(cur); // 添加根节点
				curSum += cur.val;
				cur = cur.left; // 递归添加左节点
			}
			cur = toVisit.peek(); // 已经访问到最左的节点了
			// 判断是否满足条件
			if (curSum == sum && cur.left == null && cur.right == null) {
				return true;
			}
			// 在不存在右节点或者右节点已经访问过的情况下，访问根节点
			if (cur.right == null || cur.right == pre) {
				TreeNode pop = toVisit.pop();
				curSum -= pop.val; // 减去出栈的值
				pre = cur;
				cur = null;
			} else {
				cur = cur.right; // 右节点还没有访问过就先访问右节点
			}
		}
		return false;
	}

	public boolean hasPathSum03(TreeNode root, int sum) {
		Stack<TreeNode> stack = new Stack<>();
		Stack<Integer> stackSum = new Stack<>();
		TreeNode cur = root;
		int curSum = 0;
		while (cur != null || !stack.isEmpty()) {
			// 节点不为空一直压栈
			while (cur != null) {
				stack.push(cur);
				curSum += cur.val;
				stackSum.push(curSum);
				cur = cur.left; // 考虑左子树
			}
			// 节点为空，就出栈
			cur = stack.pop();
			curSum = stackSum.pop();
			// 判断是否满足条件
			if (curSum == sum && cur.left == null && cur.right == null) {
				return true;
			}
			// 考虑右子树
			cur = cur.right;
		}
		return false;
	}

	// bfs
	public boolean hasPathSum04(TreeNode root, int sum) {
		if (root == null)
			return false;
		boolean res = false;
		Queue<TreeNode> q1 = new LinkedList<>();
		Queue<Integer> q2 = new LinkedList<>();
		q1.offer(root);
		q2.offer(root.val);
		while (!q1.isEmpty()) {
			TreeNode cur = q1.poll();
			int temp = q2.poll();
			if (cur.left == null && cur.right == null && temp == sum)
				res = true;
			if (cur.left != null) {
				q1.offer(cur.left);
				q2.offer(cur.left.val + temp);
			}
			if (cur.right != null) {
				q1.offer(cur.right);
				q2.offer(cur.right.val + temp);
			}
		}
		return res;
	}

}
