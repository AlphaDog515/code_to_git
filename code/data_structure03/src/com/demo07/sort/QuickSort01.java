package com.demo07.sort;

import java.util.Arrays;

public class QuickSort01 {

	public static void main(String[] args) {
		// int[] arr = new int[] { 3, 4, 6, 7, 2, 7, 2, 8, 0, 9, 1 };
//		int[] arr = {9,2,8,10,45,10,18};
//		quickSort(arr, 0, arr.length - 1);
//		System.out.println(Arrays.toString(arr));		
		
		int[] arr = { 9, 2, 8, 10, 45, 10, 18 };
		Test0 t = new Test0();
		System.out.println("使用第一个数作为标准！");
		t.quickSort01(arr, 0, arr.length - 1);
		System.out.println(Arrays.toString(arr));

		System.out.println("使用中间数作为标准！");
		t.quickSort02(arr, 0, arr.length - 1);
		System.out.println(Arrays.toString(arr));
		
	}

	/*
	 * 5,1,4,6,9,10,2
	 * temp=5,头尾各一个指针
	 * 从尾部开始，2小于等于5，替换
	 * 2,1,4,6,9,10,2
	 * 6大于等于5，替换
	 * 2,1,4,6,9,10,6
	 * 10大于6，9大于6，指针一样6=temp
	 * 
	 */
	// 快速排序
	public static void quickSort(int[] arr, int start, int end) {
		if (start < end) {
			// 把数组中的第0个数字做为标准数
			int stard = arr[start];
			// 记录需要排序的下标
			int low = start;
			int high = end;
			// 循环找比标准数大的数和比标准数小的数
			while (low < high) {
				// 右边的数字比标准数大
				while (low < high && stard <= arr[high]) {
					high--;
				}
				// 使用右边的数字替换左边的数
				arr[low] = arr[high];
				// 如果左边的数字比标准数小
				while (low < high && arr[low] <= stard) {
					low++;
				}
				arr[high] = arr[low];
			}
			// 把标准数赋给低所在的位置的元素
			arr[low] = stard;
			// 处理所有的小的数字
			quickSort(arr, start, low);
			// 处理所有的大的数字
			quickSort(arr, low + 1, end);
		}
	}

}


class Test0 {
	public void quickSort01(int[] arr, int left, int right) {
		if (left < right) {
			int standard = arr[left];
			int l = left;
			int r = right;
			while (l < r) {
				while (l < r && arr[r] >= standard) { 
					r--;
				}
				arr[l] = arr[r];

				while (l < r && arr[l] <= standard) {
					l++;
				}
				arr[r] = arr[l];
			}
			arr[l] = standard;
			quickSort01(arr, left, l);
			quickSort01(arr, l + 1, right);
		}
	}

	public void quickSort02(int[] arr, int left, int right) {
		int l = left;
		int r = right;
		int standard = arr[(left + right) / 2];
		int temp = 0;
		while (l < r) {
			while (arr[l] < standard) {
				l += 1;
			}
			while (arr[r] > standard) {
				r -= 1;
			}
			if (l >= r) { // 跳出循环的条件
				break;
			}
			temp = arr[l];
			arr[l] = arr[r];
			arr[r] = temp;
			
			if (arr[l] == standard) { // 这两个判断解决相同数字的情况,也可以是l++,r--
				r -= 1;
			}
			if (arr[r] == standard) {
				l += 1;
			}
		}
		
		// 这个判断解决中间值正好在中间的情况，如：{9,8,10,40,56}
		// 不递归中间数据
		if (l == r) {
			l += 1;
			r -= 1;
		}
		
		if (left < r) { // left r l right
			quickSort02(arr, left, r);
		}
		if (l < right) {
			quickSort02(arr, l, right);
		}
	}

	public void quickSort03(int[] arr, int start, int end) {
		int l = start;
		int r = end;
		int pivot = arr[(start + end) / 2];
		int temp = 0;
		while (l < r) {
			while (arr[l] < pivot) {
				l++;
			}
			while (arr[r] > pivot) {
				r--;
			}

			if (arr[l] == arr[r] && (l < r)) {
				l++;
			} else {
				temp = arr[l];
				arr[l] = arr[r];
				arr[r] = temp;
			}

		}
		if (l - 1 > start) {
			quickSort03(arr, start, l - 1);
		}
		if (r + 1 < end) {
			quickSort03(arr, r + 1, end);
		}
	}
}