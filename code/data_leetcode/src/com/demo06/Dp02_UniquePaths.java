package com.demo06;

import java.util.Arrays;

public class Dp02_UniquePaths {

}

/*  
	一个机器人位于一个 m x n 网格的左上角 。[m列 n行]
	机器人每次只能向下或者向右移动一步。
	机器人试图达到网格的右下角
	问总共有多少条不同的路径？
	输入: m = 3, n = 2 
	输出: 3
	解释:
	从左上角开始，总共有 3 条路径可以到达右下角。
	1. 向右 -> 向右 -> 向下
	2. 向右 -> 向下 -> 向右
	3. 向下 -> 向右 -> 向右

	思路一：
	因为机器到底右下角，向下几步，向右几步都是固定的，m列n行,向下n-1步，向右m-1步
	组合数m-1+n-1选出m-1
	
	
	思路二：动态规划
	我们令 dp[i][j] 是到达 i, j 最多路径	
	动态方程：dp[i][j] = dp[i-1][j] + dp[i][j-1]	
	注意，对于第一行 dp[0][j]，或者第一列 dp[i][0]，由于都是在边界，所以只能为 1	
	时间复杂度：O(m*n)
	空间复杂度：O(m*n)
	优化：因为我们每次只需要 dp[i-1][j],dp[i][j-1]	
	所以我们只要记录这两个数

​	
*/

class Solution_2_1 {
	public int uniquePaths(int m, int n) {
		int[][] dp = new int[m][n];
		for (int i = 0; i < n; i++)
			dp[0][i] = 1;
		for (int i = 0; i < m; i++)
			dp[i][0] = 1;
		for (int i = 1; i < m; i++) {
			for (int j = 1; j < n; j++) {
				dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
			}
		}
		return dp[m - 1][n - 1];
	}
}

class Solution_2_2 { // 空间复杂度 O(2n)
	public int uniquePaths(int m, int n) {
		int[] pre = new int[n];
		int[] cur = new int[n];
		Arrays.fill(pre, 1);
		Arrays.fill(cur, 1);
		for (int i = 1; i < m; i++) {
			for (int j = 1; j < n; j++) {
				cur[j] = cur[j - 1] + pre[j];
			}
			pre = cur.clone();
		}
		return pre[n - 1];
	}
}

class Solution_2_3 { // 空间复杂度 O(n)
	public int uniquePaths(int m, int n) {
		int[] cur = new int[n];
		Arrays.fill(cur, 1);
		for (int i = 1; i < m; i++) {
			for (int j = 1; j < n; j++) {
				cur[j] += cur[j - 1];
			}
		}
		return cur[n - 1];
	}
}

/*
	1.如果第一个格点 obstacleGrid[0,0] 是 1，说明有障碍物，那么机器人不能做任何移动，我们返回结果 0。
	2.否则，如果 obstacleGrid[0,0] 是 0，我们初始化这个值为 1 然后继续算法。
	3.遍历第一行，如果有一个格点初始值为 1 ，说明当前节点有障碍物，没有路径可以通过，设值为 0 ；
		否则设这个值是前一个节点的值 obstacleGrid[i,j] = obstacleGrid[i,j-1]。
	4.遍历第一列，如果有一个格点初始值为 1 ，说明当前节点有障碍物，没有路径可以通过，设值为 0 ；
		否则设这个值是前一个节点的值 obstacleGrid[i,j] = obstacleGrid[i-1,j]。
	5.现在，从 obstacleGrid[1,1] 开始遍历整个数组，如果某个格点初始不包含任何障碍物，
		就把值赋为上方和左侧两个格点方案数之和 obstacleGrid[i,j] = obstacleGrid[i-1,j] + obstacleGrid[i,j-1]。
		如果这个点有障碍物，设值为 0 ，这可以保证不会对后面的路径产生贡献。
*/
// 时间复杂度 ：O(M×N) 。长方形网格的大小是 M×N，而访问每个格点恰好一次。
// 空间复杂度 ：O(1)。我们利用 obstacleGrid 作为 DP 数组，因此不需要额外的空间。
// 以下是有障碍物的情况，思路如上
class Solution_2_4 {
	public int uniquePathsWithObstacles(int[][] obstacleGrid) {

		int R = obstacleGrid.length;
		int C = obstacleGrid[0].length;

		// If the starting cell has an obstacle, then simply return as there would be
		// no paths to the destination.
		if (obstacleGrid[0][0] == 1) {
			return 0;
		}

		// Number of ways of reaching the starting cell = 1.
		obstacleGrid[0][0] = 1;

		// Filling the values for the first column
		for (int i = 1; i < R; i++) {
			obstacleGrid[i][0] = (obstacleGrid[i][0] == 0 && obstacleGrid[i - 1][0] == 1) ? 1 : 0;
		}

		// Filling the values for the first row
		for (int i = 1; i < C; i++) {
			obstacleGrid[0][i] = (obstacleGrid[0][i] == 0 && obstacleGrid[0][i - 1] == 1) ? 1 : 0;
		}

		// Starting from cell(1,1) fill up the values
		// No. of ways of reaching cell[i][j] = cell[i - 1][j] + cell[i][j - 1]
		// i.e. From above and left.
		for (int i = 1; i < R; i++) {
			for (int j = 1; j < C; j++) {
				if (obstacleGrid[i][j] == 0) {
					obstacleGrid[i][j] = obstacleGrid[i - 1][j] + obstacleGrid[i][j - 1];
				} else {
					obstacleGrid[i][j] = 0;
				}
			}
		}

		// Return value stored in rightmost bottommost cell. That is the destination.
		return obstacleGrid[R - 1][C - 1];
	}
}
