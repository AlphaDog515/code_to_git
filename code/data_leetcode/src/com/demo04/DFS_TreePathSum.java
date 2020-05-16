package com.demo04;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class DFS_TreePathSum {

}

class Solution_1_2{
	/*
	 给定一个二叉树和一个目标和，找到所有从根节点到叶子节点路径总和等于给定目标和的路径。
	说明: 叶子节点是指没有子节点的节点。	
	示例:
	给定如下二叉树，以及目标和 sum = 22，
	
	              5
	             / \
	            4   8
	           /   / \
	          11  13  4
	         /  \    / \
	        7    2  5   1
	返回:	
	[
	   [5,4,11,2],
	   [5,8,4,5]
	]	 
	 
	 */
	
	// Definition for a binary tree node.
	public class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;

		TreeNode(int x) {
			val = x;
		}
	}

	public List<List<Integer>> pathSum01(TreeNode root, int sum) {
		List<List<Integer>> res = new ArrayList<>();
		helper(root, sum, res, new ArrayList<Integer>());
		return res;
	}

	private void helper(TreeNode root, int sum, List<List<Integer>> res, ArrayList<Integer> tmp) {
		if (root == null)
			return;
		tmp.add(root.val);
		if (root.left == null && root.right == null && sum - root.val == 0)
			res.add(new ArrayList<>(tmp));
		helper(root.left, sum - root.val, res, tmp);
		helper(root.right, sum - root.val, res, tmp);
		tmp.remove(tmp.size() - 1);
	}
	
	
	
	List<List<Integer>> result = new LinkedList<>();
	Stack<Integer> path = new Stack<>();

	public List<List<Integer>> pathSum02(TreeNode root, int sum) {
		dfs(root, sum);
		return result;
	}

	public void dfs(TreeNode root, int sum) {
		if (root == null)
			return;
		if (root.left == null && root.right == null) {
			path.push(root.val);
			if (root.val == sum) {
				// public ArrayList(Collection<? extends E> c)
				result.add(new ArrayList<>(path));
			}
			path.pop();
			return;
		}

		path.push(root.val);
		if (root.left != null)
			dfs(root.left, sum - root.val);
		if (root.right != null)
			dfs(root.right, sum - root.val);
		path.pop();
	}
	
	
	
	
}