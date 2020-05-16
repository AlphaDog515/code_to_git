package com.demo06;

public class Dp05_WaysToChange {
	public static void main(String[] args) {
		Solution_5 solution = new Solution_5();
		int res = solution.waysToChange(4);
		System.out.println(res);
	}
	
	
/*	硬币。给定数量不限的硬币，币值为25分、10分、5分和1分，
        编写代码计算n分有几种表示法。(结果可能会很大，你需要将结果模上1000000007)

	示例1:
	输入: n = 5
	输出：2
	解释: 有两种方式可以凑成总金额: 5=5 5=1+1+1+1+1
	
	示例2:
	输入: n = 10
	输出：4
	解释: 有四种方式可以凑成总金额:
	10=10
	10=5+5
	10=5+1+1+1+1+1
	10=1+1+1+1+1+1+1+1+1+1
*/
}

class Solution_5 {
	
/*	动态规划【二维数组】
	dp[i][j]=dp[i-1][j]+dp[i][j-coins[i]]
	令 dp[i][j] 为遍历到当下这个硬币时，组成金额 j 的方法数目,有两种可能性:
	(1)当前这个硬币没有取，dp[i][j]=dp[i-1][j]；
	(2)当前这个硬币取了，dp[i][j]=dp[i][j-coins[i]]。
	最后的结果是两者的和,将状态转移方程翻译成代码，并处理边界条件
*/
	public int waysToChange(int n) {
		int[][] dp = new int[4][n + 1];
		int[] coins = { 1, 2, 5, 10 };

		for (int i = 0; i <= n; i++)
			dp[0][i] = 1;
		for (int i = 0; i < 4; i++)
			dp[i][0] = 1;

		for (int i = 1; i < 4; i++) {
			for (int j = 1; j <= n; j++) {
				if (j >= coins[i])
					dp[i][j] = (dp[i - 1][j] + dp[i][j - coins[i]]) % 1000000007;
				else
					dp[i][j] = dp[i - 1][j];
			}
		}
		return dp[3][n];
	}

/*	动态规划【一维数组】
	从上面的状态转移方程可以看出，
	dp[i][j]只与dp[i-1][j]和dp[i][j-coins[i]]有关，
	所以完全可以把第一个维度除掉，只用一个一维数组存储
*/
	public int waysToChange2(int n) {
		int[] dp = new int[n + 1];
		int[] coins = { 1, 5, 10, 25 };

		for (int i = 0; i <= n; i++)
			dp[i] = 1;

		for (int i = 1; i < 4; i++) {
			for (int j = 1; j <= n; j++) {
				if (j >= coins[i])
					dp[j] = (dp[j] + dp[j - coins[i]]) % 1000000007;
			}
		}
		return dp[n];
	}
	
	
/*	 def change(money: Int, coins: Array[Int]):Int = {
        if(money < 0 || coins.isEmpty) 0
        else if(money == 0) 1       
        else change(money, coins.tail) + change(money - coins.head, coins)
	 }
*/
}
