package com.demo03;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Num13_MergeBlock {
	public static void main(String[] args) {
		int[][] arr = { { 1, 3 }, { -1, 0 }, { 8, 10 }, { 15, 18 } };
		Arrays.sort(arr,(a, b) -> a[0] - b[0]);
		
		
//		Solution_13 solution = new Solution_13();
//		int[][] res = solution.merge(arr);
		for (int[] r : arr) {
			System.out.println(Arrays.toString(r));
		}
	}
}
/*
	 给出一个区间的集合，请合并所有重叠的区间。
	
	示例 1:	
	输入: [[1,3],[2,6],[8,10],[15,18]]
	输出: [[1,6],[8,10],[15,18]]
	解释: 区间 [1,3] 和 [2,6] 重叠, 将它们合并为 [1,6].
	
	示例 2:	
	输入: [[1,4],[4,5]]
	输出: [[1,5]]
	解释: 区间 [1,4] 和 [4,5] 可被视为重叠区间。

 */
class Solution_13 {
	public int[][] merge(int[][] intervals) {
		List<int[]> res = new ArrayList<>();
		if (intervals.length == 0 || intervals == null)
			return res.toArray(new int[0][]);
		Arrays.sort(intervals, (a, b) -> a[0] - b[0]); // 按照一维数组的第一个数字排序
		int i = 0;
		while (i < intervals.length) {
			int left = intervals[i][0];
			int right = intervals[i][1];
			while (i < intervals.length - 1 && intervals[i + 1][0] <= right) {
				i++;
				right = Math.max(right, intervals[i][1]);
			}
			res.add(new int[] { left, right });
			i++;
		}
		return res.toArray(new int[0][]); // 返回int[][]类型
	}
}

