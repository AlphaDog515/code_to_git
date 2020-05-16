package com.demo03;

import java.util.Arrays;

public class Num03_FindMedian02 {

	public static void main(String[] args) {
		int[] nums1 = { 1, 3, 5, 8, 20, 30 };
		int[] nums2 = { 1, 3, 9 };
		Solution_03_1 so = new Solution_03_1();
		double res = so.findMedianSortedArrays(nums1, nums2);
		System.out.println(res);

	}

}


class Solution_03_1 {

	public double findMedianSortedArrays(int[] nums1, int[] nums2) {
		// 即在输入数组不是有序数组的时候，这个算法依然有效
		int m = nums1.length;
		int n = nums2.length;
		int[] merge = new int[m + n];
		System.arraycopy(nums1, 0, merge, 0, m);
		System.arraycopy(nums2, 0, merge, m, n);
		Arrays.sort(merge);

		if (((m + n) & 1) == 1) { // 判断时否时奇数
			return merge[(m + n - 1) >>> 1];
		} else {
			return (double) (merge[(m + n - 1) >>> 1] + merge[(m + n) >>> 1]) / 2;
		}
	}
}

class Solution_03_2 {
	// 找到m/2以及其前面一个数
	public double findMedianSortedArrays(int[] nums1, int[] nums2) {
		int m = nums1.length;
		int n = nums2.length;

		// 最后要找到合并以后索引是 median_index 的这个数
		int medianIndex = (m + n) >>> 1;

		// 计数器从 -1 开始，在循环开始之前加 1
		// 这样在退出循环的时候，counter 能指向它最后赋值的那个元素
		int counter = -1;

		// nums1 的索引
		int i = 0;
		// nums2 的索引
		int j = 0;

		int[] res = new int[] { 0, 0 };
		while (counter < medianIndex) {
			counter++;
			// 先写 i 和 j 遍历完成的情况，否则会出现数组下标越界
			if (i == m) {
				res[counter & 1] = nums2[j];
				j++;
			} else if (j == n) {
				res[counter & 1] = nums1[i];
				i++;
			} else if (nums1[i] < nums2[j]) {
				res[counter & 1] = nums1[i];
				i++;
			} else {
				res[counter & 1] = nums2[j];
				j++;
			}
		}

		// 如果 m + n 是奇数，median_index 就是我们要找的
		// 如果 m + n 是偶数，有一点麻烦，要考虑其中有一个用完的情况，其实也就是把上面循环的过程再进行一步
		if (((m + n) & 1) == 1) {
			return res[counter & 1];
		} else {
			return (double) (res[0] + res[1]) / 2;
		}
	}
}

class Solution_03_3 {
	public double findMedianSortedArrays(int[] nums1, int[] nums2) {
		// 为了让搜索范围更小，我们始终让 num1 是那个更短的数组，PPT 第 9 张
		if (nums1.length > nums2.length) {
			int[] temp = nums1;
			nums1 = nums2;
			nums2 = temp;
		}

		// 上述交换保证了 m <= n，在更短的区间 [0, m] 中搜索，会更快一些
		int m = nums1.length;
		int n = nums2.length;

		// 使用二分查找算法在数组 nums1 中搜索一个索引 i，PPT 第 9 张
		int left = 0;
		int right = m;
		// 这里使用的是最简单的、"传统"的二分查找法模板，使用"高级的"二分查找法模板在退出循环时候处理不方便
		while (left <= right) {
			// 尝试要找的索引，在区间里完成二分，为了保证语义，这里就不定义成 mid 了
			// 用加号和右移是安全的做法，即使在溢出的时候都能保证结果正确，但是 Python 中不存在溢出
			// 参考：https://leetcode-cn.com/problems/guess-number-higher-or-lower/solution/shi-fen-hao-yong-de-er-fen-cha-zhao-fa-mo-ban-pyth/
			int i = (left + right) >>> 1;
			// j 的取值在 PPT 第 7 张
			int j = ((m + n + 1) >>> 1) - i;

			// 边界值的特殊取法的原因在 PPT 第 10 张
			int nums1LeftMax = i == 0 ? Integer.MIN_VALUE : nums1[i - 1];
			int nums1RightMin = i == m ? Integer.MAX_VALUE : nums1[i];

			int nums2LeftMax = j == 0 ? Integer.MIN_VALUE : nums2[j - 1];
			int nums2RightMin = j == n ? Integer.MAX_VALUE : nums2[j];

			// 交叉小于等于关系成立，那么中位数就可以从"边界线"两边的数得到，原因在 PPT 第 2 张、第 3 张
			if (nums1LeftMax <= nums2RightMin && nums2LeftMax <= nums1RightMin) {
				// 已经找到解了，分数组之和是奇数还是偶数得到不同的结果，原因在 PPT 第 2 张
				if (((m + n) & 1) == 1) {
					return Math.max(nums1LeftMax, nums2LeftMax);
				} else {
					return (double) ((Math.max(nums1LeftMax, nums2LeftMax) + Math.min(nums1RightMin, nums2RightMin)))
							/ 2;
				}
			} else if (nums2LeftMax > nums1RightMin) {
				// 这个分支缩短边界的原因在 PPT 第 8 张
				left = i + 1;
			} else {
				// 这个分支缩短边界的原因在 PPT 第 8 张
				right = i - 1;
			}
		}
		throw new IllegalArgumentException("传入无效的参数，输入的数组不是有序数组，算法失效");
	}
}

class Solution_03_4 {
	public double findMedianSortedArrays(int[] nums1, int[] nums2) {
		// 为了让搜索范围更小，我们始终让 num1 是那个更短的数组，PPT 第 9 张
		if (nums1.length > nums2.length) {
			int[] temp = nums1;
			nums1 = nums2;
			nums2 = temp;
		}

		// 上述交换保证了 m <= n，在更短的区间 [0, m] 中搜索，会更快一些
		int m = nums1.length;
		int n = nums2.length;

		// 使用二分查找算法在数组 nums1 中搜索一个索引 i，PPT 第 9 张
		int left = 0;
		int right = m;

		// 因为 left_total 这个变量会一直用到，因此单独赋值，表示左边粉红色部分一共需要的元素个数
		int totalLeft = (m + n + 1) >>> 1;
		while (left < right) {
			// 尝试要找的索引，在区间里完成二分，为了保证语义，这里就不定义成 mid 了
			// 用加号和右移是安全的做法，即使在溢出的时候都能保证结果正确，但是 Python 中不存在溢出
			// 参考：https://leetcode-cn.com/problems/guess-number-higher-or-lower/solution/shi-fen-hao-yong-de-er-fen-cha-zhao-fa-mo-ban-pyth/
			int i = (left + right) >>> 1;
			// j 的取值在 PPT 第 7 张
			int j = totalLeft - i;

			if (nums2[j - 1] > nums1[i]) {
				// 这个分支缩短边界的原因在 PPT 第 8 张，情况 ①
				left = i + 1;
			} else {
				// 这个分支缩短边界的原因在 PPT 第 8 张，情况 ②
				// 【注意】：不让它收缩的原因是讨论 nums1[i - 1] > nums2[j]，i - 1 在数组的索引位置，在 i = 0 时越界
				right = i;
			}
		}

		/*
		 1  2 | 3  8
		 2  3   4  6 | 8  9  10
		 
		 1  2  3 | 8
		 2  3  4 | 6  8  9  10
		 解释退出循环以后交叉小于等于一定成立,6>3以后才进入第二次比较，第二次必然有3<6，4<8退出循环
		 
		 二分法退出循环的时候left=right，并且是不满足arr>target最近的数，要么小于要么等于
		 */
		// 退出循环的时候，交叉小于等于一定关系成立，那么中位数就可以从"边界线"两边的数得到，原因在 PPT 第 2 张、第 3 张

		int i = left;
		int j = totalLeft - left;
		// 边界值的特殊取法的原因在 PPT 第 10 张
		int nums1LeftMax = i == 0 ? Integer.MIN_VALUE : nums1[i - 1];
		int nums1RightMin = i == m ? Integer.MAX_VALUE : nums1[i];

		int nums2LeftMax = j == 0 ? Integer.MIN_VALUE : nums2[j - 1];
		int nums2RightMin = j == n ? Integer.MAX_VALUE : nums2[j];

		// 交叉小于等于关系成立，那么中位数就可以从"边界线"两边的数得到，原因在 PPT 第 2 张、第 3 张
		// 已经找到解了，分数组之和是奇数还是偶数得到不同的结果，原因在 PPT 第 2 张
		if (((m + n) & 1) == 1) {
			return Math.max(nums1LeftMax, nums2LeftMax);
		} else {
			return (double) ((Math.max(nums1LeftMax, nums2LeftMax) + Math.min(nums1RightMin, nums2RightMin))) / 2;
		}
	}
}

// 以下时合并两个有序数组的算法

class Solution_03_5 {

	public void merge(int[] nums1, int m, int[] nums2, int n) {
		int[] nums3 = new int[m];
		// 只需要把 nums1 的有效个元素复制到 nums3 就可以了
		System.arraycopy(nums1, 0, nums3, 0, m);
		// 数组3
		int i = 0;
		// 数组2
		int j = 0;
		int length = m + n;
		// 从前向后归并，比较 nums3 和 nums2 前端的元素哪个小，谁小谁出列，覆盖 nums1
		for (int k = 0; k < length; k++) {
			// 注意：要把 nums3 和 nums2 归并完成的逻辑写在前面，否则会出现数组下标越界异常
			if (i == m) {
				nums1[k] = nums2[j];
				j++;
			} else if (j == n) {
				nums1[k] = nums3[i];
				i++;
			} else if (nums3[i] < nums2[j]) {
				nums1[k] = nums3[i];
				i++;
			} else {
				nums1[k] = nums2[j];
				j++;
			}
		}
	}
}

// 题目中说了，nums1 够用，我可以从后向前归并 
class Solution_03_6 {

	public void merge(int[] nums1, int m, int[] nums2, int n) {
		int len = m + n;
		int i = m - 1;
		int j = n - 1;
		// 从后向前归并，比较 nums1 和 nums2 末尾的元素哪个大，谁大谁出列，覆盖 nums1
		for (int k = len - 1; k >= 0; k--) {
			if (i == -1) {
				// 注意：同样要把 nums1 和 nums2 归并完成的逻辑写在前面，否则会出现数组下标越界异常
				// 此时 j 位于数组 nums2 的末尾索引位置，还未看的数组 nums2 的长度为 j + 1
				// 复制完 break 掉即可
				System.arraycopy(nums2, 0, nums1, 0, j + 1);
				break;
			} else if (j == -1) {
				// 注意：这里直接 break 掉就可以了
				// 因为 nums2 遍历完成以后，nums1 剩下的元素虽然还没有看，但一定是排定以后的那个样子
				break;
			} else if (nums1[i] >= nums2[j]) {
				// 谁大谁出列
				nums1[k] = nums1[i];
				i--;
			} else {
				assert nums1[i] < nums2[j];
				nums1[k] = nums2[j];
				j--;
			}
		}
	}
}
