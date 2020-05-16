package com.demo03;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.junit.Test;

/*
 给定一个没有重复数字的序列，返回其所有可能的全排列。
示例:
	输入: [1,2,3]
输出:
	[
	  [1,2,3],
	  [1,3,2],
	  [2,1,3],
	  [2,3,1],
	  [3,1,2],
	  [3,2,1]
	]
 */
public class Num01_FullPermutation {

	@Test
	public void test01() {
		int[] arr = { 1, 2, 3 };
		Solution_01 so = new Solution_01();
		List<List<Integer>> res = so.permute(arr);
		System.out.println(res);
	}

	@Test
	public void test02() {
		int[] nums = new int[] { 1, 2, 3, 4 };
		Solution_01_1 solution = new Solution_01_1();
		List<List<Integer>> permute = solution.permute(nums);
		for (int i = 0; i < permute.size(); i++) {
			System.out.println(permute.get(i));
		}
	}
}

class Solution_01 {
	private List<List<Integer>> lists = new ArrayList<List<Integer>>();

	public List<List<Integer>> permute(int[] nums) {
		if (nums.length == 0)
			return lists;
		else {
			allSort(nums, new Stack<Integer>());
			return lists;
		}
	}

	// 递归函数
	public void allSort(int[] nums, Stack<Integer> stack) {
		// 终止条件
		if (stack.size() == nums.length) {
			lists.add(new ArrayList<Integer>(stack));
			return;
		}
		for (int num : nums) {
			if (stack.contains(num))
				continue;
			stack.push(num);
			allSort(nums, stack);
			stack.pop();// 出栈
		}
	}
}

class Solution_01_1 {

	// curSize 表示当前的路径 path 里面有多少个元素

	private void generatePermution(int[] nums, boolean[] visited, int curSize, int len, Stack<Integer> path,
			List<List<Integer>> res) {
		if (curSize == len) {
			// 此时 path 已经保存了 nums 中的所有数字，已经成为了一个排列
			res.add(new ArrayList<>(path));
			return;
		}
		for (int i = 0; i < len; i++) {
			if (!visited[i]) {
				path.push(nums[i]);
				visited[i] = true;
				generatePermution(nums, visited, curSize + 1, len, path, res);
				// 刚开始接触回溯算法的时候常常会忽略状态重置
				// 回溯的时候，一定要记得状态重置
				path.pop();
				visited[i] = false;
			}
		}
	}

	public List<List<Integer>> permute(int[] nums) {
		int len = nums.length;
		List<List<Integer>> res = new ArrayList<>();
		boolean[] used = new boolean[len];
		if (len == 0) {
			return res;
		}
		generatePermution(nums, used, 0, len, new Stack<>(), res);
		return res;
	}
}

/*
 
	 1、数组 used 记录了索引 i 在递归过程中是否被使用过，还可以用哈希表、位图来代替，
	 	在下面的参考代码 2 和参考代码 3 分别提供了 Java 的代码；
	
	 2、当程序第 1 次走到一个结点的时候，表示考虑一个数，要把它加入列表，
	 	经过更深层的递归又回到这个结点的时候，需要“状态重置”、“恢复现场”，
	 	需要把之前考虑的那个数从末尾弹出，这都是在一个列表的末尾操作，最合适的数据结构是栈（Stack）。

 
 */
class Solution_01_2 {

	public List<List<Integer>> permute(int[] nums) {
		int len = nums.length;

		List<List<Integer>> res = new ArrayList<>();
		if (len == 0) {
			return res;
		}

		// 使用哈希表检测一个数字是否使用过
		Set<Integer> used = new HashSet<>();
		Stack<Integer> stack = new Stack<>();

		backtrack(nums, 0, len, used, stack, res);
		return res;
	}

	private void backtrack(int[] nums, int depth, int len, Set<Integer> used, Stack<Integer> stack,
			List<List<Integer>> res) {
		if (depth == len) {
			res.add(new ArrayList<>(stack));
			return;
		}

		for (int i = 0; i < len; i++) {
			if (!used.contains(i)) {
				used.add(i);
				stack.push(nums[i]);

				backtrack(nums, depth + 1, len, used, stack, res);

				stack.pop();
				used.remove(i);
			}
		}
	}

}



/*
	使用递归的思想如下:	
	首先，排在第 1 位，可能的情况有 n 种，剩下的 n - 1 位数的排列可以递归求解；
	不过要解决的 1 个问题是，剩下的 n - 1 位数的排列不能够包括排在第 1 位的那个数，
	根据“选择排序”的思想，把剩下的 n - 1 位数依次交换到第 1 位即可。
		例如：[1, 2, 3, 4] 的全排列可以由下面 4 种情况得到：		
		1 + permute([2, 3, 4])		
		2 + permute([1, 3, 4])		
		3 + permute([1, 2, 4])		
		4 + permute([1, 2, 3])

*/


class Solution_01_3 {

	public List<List<Integer>> permute(int[] nums) {
		List<List<Integer>> res = new ArrayList<>();
		int len = nums.length;
		if (len == 0) {
			return res;
		}
		helper(nums, 0, len, res);
		return res;
	}

	private void helper(int[] nums, int begin, int len, List<List<Integer>> res) {
		if (begin == len - 1) {
			List<Integer> currRes = new ArrayList<>();
			for (int i = 0; i < len; i++) {
				currRes.add(nums[i]);
			}
			res.add(currRes);
			return;
		}

		helper(nums, begin + 1, len, res);
		// 从 begin 的下一位开始一直要交换到最后一位
		for (int i = begin + 1; i < len; i++) {
			swap(nums, begin, i);
			helper(nums, begin + 1, len, res);
			// 注意：递归完成以后要交换回来
			swap(nums, begin, i);
		}
	}

	private void swap(int[] nums, int index1, int index2) {
		nums[index1] = nums[index1] ^ nums[index2];
		nums[index2] = nums[index1] ^ nums[index2];
		nums[index1] = nums[index1] ^ nums[index2];
	}
}



class Solution_01_4 {
	// 递归 + dfs
	public  void permTest() {
		perm(new int[] { 1, 2, 3 }, new Stack<>());
	}

	public void perm(int[] array, Stack<Integer> stack) {
		if (array.length <= 0) {
			// 进入了叶子节点，输出栈中内容
			System.out.println(stack);
		} else {
			for (int i = 0; i < array.length; i++) {
				// tmepArray是一个临时数组，用于就是Ri
				// eg：1，2，3的全排列，先取出1，那么这时tempArray中就是2，3
				int[] tempArray = new int[array.length - 1];
				System.arraycopy(array, 0, tempArray, 0, i);
				System.arraycopy(array, i + 1, tempArray, i, array.length - i - 1);
				stack.push(array[i]);
				perm(tempArray, stack);
				stack.pop();
			}
		}
	}
}


class Solution_01_5 {
	public void permTest() {
		perm(new int[] { 1, 2, 3 }, 0, 2);
	}

	public void perm(int[] array, int start, int end) {
		if (start == end) {
			System.out.println(Arrays.toString(array));
		} else {
			for (int i = start; i <= end; i++) {
				// 1，2，3的全排列这块相当于将其中一个提了出来，下次递归从start+1开始
				swap(array, start, i);
				perm(array, start + 1, end);
				// 这块是复原数组，为了保证下次另外的同级递归使用数组不会出错
				// 这块可以通过树来理解，每次回退一步操作，交换回去
				swap(array, start, i);
			}
		}
	}

	public static void swap(int[] array, int i, int j) {
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}
}
