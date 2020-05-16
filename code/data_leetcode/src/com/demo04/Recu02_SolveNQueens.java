package com.demo04;

import java.util.ArrayList;
import java.util.List;

public class Recu02_SolveNQueens {

}


/*
	n皇后问题研究的是如何将 n 个皇后放置在 n×n 的棋盘上，并且使皇后彼此之间不能相互攻击。
	上图为 8 皇后问题的一种解法。	
	给定一个整数 n，返回所有不同的 n 皇后问题的解决方案。	
	每一种解法包含一个明确的 n 皇后问题的棋子放置方案，该方案中 'Q' 和 '.' 分别代表了皇后和空位。	
	示例:	
	输入: 4
	输出: [
	 [".Q..",  // 解法 1
	  "...Q",
	  "Q...",
	  "..Q."],
	
	 ["..Q.",  // 解法 2
	  "Q...",
	  "...Q",
	  ".Q.."]
	]
	解释: 4 皇后问题存在两个不同的解法

 
	回溯函数 backtrack(row = 0).	
	从第一个 row = 0 开始.	
	循环列并且试图在每个 column 中放置皇后.	
		如果方格 (row, column) 不在攻击范围内	
			在 (row, column) 方格上放置皇后。
			排除对应行，列和两个对角线的位置。
			If 所有的行被考虑过，row == N
				意味着我们找到了一个解
			Else
				继续考虑接下来的皇后放置 backtrack(row + 1).
			回溯：将在 (row, column) 方格的皇后移除.

 */


class Solution_2_1 {
	int rows[];
	// "hill" diagonals
	int hills[];
	// "dale" diagonals
	int dales[];
	int n;
	// output
	List<List<String>> output = new ArrayList();
	// queens positions
	int queens[];

	public boolean isNotUnderAttack(int row, int col) {
		int res = rows[col] + hills[row - col + 2 * n] + dales[row + col];
		return (res == 0) ? true : false;
	}

	public void placeQueen(int row, int col) {
		queens[row] = col;
		rows[col] = 1;
		hills[row - col + 2 * n] = 1; // "hill" diagonals
		dales[row + col] = 1; // "dale" diagonals
	}

	public void removeQueen(int row, int col) {
		queens[row] = 0;
		rows[col] = 0;
		hills[row - col + 2 * n] = 0;
		dales[row + col] = 0;
	}

	public void addSolution() {
		List<String> solution = new ArrayList<String>();
		for (int i = 0; i < n; ++i) {
			int col = queens[i];
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < col; ++j)
				sb.append(".");
			sb.append("Q");
			for (int j = 0; j < n - col - 1; ++j)
				sb.append(".");
			solution.add(sb.toString());
		}
		output.add(solution);
	}

	public void backtrack(int row) {
		for (int col = 0; col < n; col++) {
			if (isNotUnderAttack(row, col)) {
				placeQueen(row, col);
				// if n queens are already placed
				if (row + 1 == n)
					addSolution();
				// if not proceed to place the rest
				else
					backtrack(row + 1);
				// backtrack
				removeQueen(row, col);
			}
		}
	}

	public List<List<String>> solveNQueens(int n) {
		this.n = n;
		rows = new int[n];
		hills = new int[4 * n - 1];
		dales = new int[2 * n - 1];
		queens = new int[n];

		backtrack(0);
		return output;
	}
}

//	复杂度分析
//	
//	时间复杂度：O(N!). 放置第 1 个皇后有 N 种可能的方法，放置两个皇后的方法不超过 N (N - 2) ，
//	放置 3 个皇后的方法不超过 N(N - 2)(N - 4) ，以此类推。总体上，时间复杂度为O(N!) .
//	空间复杂度O(N) . 需要保存对角线和行的信息。


class Solution_2_2 {
	// 竖排被占登记，用于判断是否能够被竖排攻击
	int rows[]; //
	// "从左到右对角线" 登记，用于判断是否能够被竖排攻击
	int hills[];
	// "从右到左对角线" 登记，用于判断是否能够被竖排攻击
	int dales[];
	int n;
	// output
	List<List<String>> output = new ArrayList();
	// 皇后的位置
	int queens[];

	/**
	 * 判断该位置是否会被攻击
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean isNotUnderAttack(int row, int col) {
		int res = rows[col] + hills[row - col + n - 1] + dales[row + col];
		return (res == 0) ? true : false;
	}

	/**
	 * 将皇后放入该位置
	 * 
	 * @param row
	 * @param col
	 */
	public void placeQueen(int row, int col) {
		queens[row] = col; // 将皇后位置放入
		rows[col] = 1; // 竖排攻击位置
		hills[row - col + n - 1] = 1; // "从左到右对角线" 攻击位置
		dales[row + col] = 1; // "从右到左对角线" 攻击位置
	}

	/**
	 * 回溯皇后位置
	 * 
	 * @param row
	 * @param col
	 */
	public void removeQueen(int row, int col) {
		queens[row] = 0;
		rows[col] = 0;
		hills[row - col + n - 1] = 0;
		dales[row + col] = 0;
	}

	/**
	 * 将满足条件的皇后位置放入output中
	 */
	public void addSolution() {
		List<String> solution = new ArrayList<String>();
		for (int i = 0; i < n; ++i) {
			int col = queens[i];
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < col; ++j)
				sb.append(".");
			sb.append("Q");
			for (int j = 0; j < n - col - 1; ++j)
				sb.append(".");
			solution.add(sb.toString());
		}
		output.add(solution);
	}

	public void backtrack(int row) {
		for (int col = 0; col < n; col++) {
			if (isNotUnderAttack(row, col)) {
				placeQueen(row, col);
				// 皇后数量是否满足，满足则输出
				if (row + 1 == n)
					addSolution();
				// 不满足则继续
				else
					backtrack(row + 1);
				// 回溯。
				removeQueen(row, col);
			}
		}
	}

	public List<List<String>> solveNQueens(int n) {
		this.n = n;
		rows = new int[n];
		hills = new int[2 * n - 1];
		dales = new int[2 * n - 1];
		queens = new int[n];

		backtrack(0);
		return output;
	}
}
