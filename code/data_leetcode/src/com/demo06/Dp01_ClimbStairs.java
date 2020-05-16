package com.demo06;

public class Dp01_ClimbStairs {
	public static void main(String[] args) {
//		Solution_1_1 solution = new Solution_1_1();
		Solution_1_2 solution = new Solution_1_2();
		
		int res = solution.climbStairs(4);
		System.out.println(res);
	}

}
/*
	假设你正在爬楼梯。需要 n 阶你才能到达楼顶。	
	每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？	
	注意：给定 n 是一个正整数。	
	示例 1：	
	输入： 2
	输出： 2
	解释： 有两种方法可以爬到楼顶。
	1. 1 阶 + 1 阶
	2. 2 阶
	
	示例 2：	
	输入： 3
	输出： 3
	解释： 有三种方法可以爬到楼顶。
	1. 1 阶 + 1 阶 + 1 阶
	2. 1 阶 + 2 阶
	3. 2 阶 + 1 阶

*/

class Solution_1_1 {
	public int climbStairs(int n) {
		int res = climb_Stairs(0, n);
		return res;
	}
	// 其中 i 定义了当前阶数，而 n 定义了目标阶数
	public int climb_Stairs(int i, int n) {
		if (i > n) {
			return 0;
		}
		if (i == n) {
			return 1;
		}
		return climb_Stairs(i + 1, n) + climb_Stairs(i + 2, n);
	}
}

// 在上一种方法中，我们计算每一步的结果时出现了冗余。
// 另一种思路是，我们可以把每一步的结果存储在 memomemo 数组之中，
// 每当函数再次被调用，我们就直接从 memomemo 数组返回结果。
class Solution_1_2 {
	public int climbStairs(int n) {
		int memo[] = new int[n + 1];
		return climb_Stairs(0, n, memo);
	}

	public int climb_Stairs(int i, int n, int memo[]) {
		if (i > n) {
			return 0;
		}
		if (i == n) {
			return 1;
		}
		if (memo[i] > 0) {
			return memo[i];
		}
		memo[i] = climb_Stairs(i + 1, n, memo) + climb_Stairs(i + 2, n, memo);
		return memo[i];
	}
}
// 动态规划
/*
        第i阶可以由以下两种方法得到：	
	在第 (i-1)阶后向上爬1阶。	
	在第 (i-2)阶后向上爬2阶。	
	所以到达第 i阶的方法总数就是到第 (i-1)阶和第 (i-2)阶的方法数之和。	
	令 dp[i]表示能到达第 i阶的方法总数：	
	dp[i]=dp[i-1]+dp[i-2]
	时间复杂度：O(n)，单循环到 n。
	空间复杂度：O(n)，dp数组用了n的空间。
	// 可以使用求等比数列的方法求出递推公式
	// an+2=an+1+an => an+2+ran+1 = p(an+1+ran)
 */
class Solution_1_3 {
	public int climbStairs(int n) {
		if (n == 1) {
			return 1;
		}
		int[] dp = new int[n + 1];
		dp[1] = 1;
		dp[2] = 2;
		for (int i = 3; i <= n; i++) {
			dp[i] = dp[i - 1] + dp[i - 2];
		}
		return dp[n];
	}
}

// 斐波那契数列
// 时间复杂度：O(n)，单循环到 n，需要计算第 n 个斐波那契数。
// 空间复杂度：O(1)，使用常量级空间。
class Solution_1_4 {
	public int climbStairs(int n) {
		if (n == 1) {
			return 1;
		}
		int first = 1;
		int second = 2;
		for (int i = 3; i <= n; i++) {
			int third = first + second;
			first = second;
			second = third;
		}
		return second;
	}
}

