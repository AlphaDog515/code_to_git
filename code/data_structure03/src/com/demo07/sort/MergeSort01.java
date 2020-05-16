package com.demo07.sort;

import java.util.Arrays;

public class MergeSort01 {

	public static void main(String[] args) {
		int[] arr = new int[] { 1, 15, 5, 20, 4, 60, 8, 10 };
		System.out.println(Arrays.toString(arr));
		mergeSort(arr, 0, arr.length - 1);
		System.out.println(Arrays.toString(arr));
	}

	// 归并排序
	// 原数组递归分拆，两个有序的数组，新建一个数组，依次比较
	public static void mergeSort(int[] arr, int low, int high) {
		int middle = (high + low) / 2;
		// 一直分割到数组中只有一个元素
		if (low < high) { // java.lang.StackOverflowError
			// 处理左边
			mergeSort(arr, low, middle);
			// 处理右边
			mergeSort(arr, middle + 1, high);
			// 归并
			merge(arr, low, middle, high);
		}
	}

	// 调用merge方法合并数组是传入开始点，中间点以及结束点，合并之后放回原数组长度依旧不变
	// 开始点，分割点，结束点
	public static void merge(int[] arr, int low, int middle, int high) {
		// 用于存储归并后的临时数组
		int[] temp = new int[high - low + 1];
		// 开始到中间，中间到结束
		// 记录第一个数组中需要遍历的下标
		int i = low;
		// 记录第二个数组中需要遍历的下标
		int j = middle + 1;
		// 用于记录在临时数组中存放的下标
		int index = 0;
		// 遍历两个数组取出小的数字，放入临时数组中
		while (i <= middle && j <= high) {
			// 第一个数组的数据更小
			if (arr[i] <= arr[j]) {
				// 把小的数据放入临时数组中
				temp[index] = arr[i];
				// 让下标向后移一位；
				i++;
			} else {
				temp[index] = arr[j];
				j++;
			}
			index++;
		}
		// 处理多余的数据
		while (j <= high) {
			temp[index] = arr[j];
			j++;
			index++;
		}
		while (i <= middle) {
			temp[index] = arr[i];
			i++;
			index++;
		}
		// 把临时数组中的数据重新存入原数组
		for (int k = 0; k < temp.length; k++) {
			arr[k + low] = temp[k];
		}
	}

}

class Test1 {
	public static void mergeSort(int[] arr, int low, int high) {
		int middle = (high + low) / 2;
		if (low < high) {
			mergeSort(arr, low, middle);
			mergeSort(arr, middle + 1, high);
			merge(arr, low, middle, high);
		}
	}

	public static void merge(int[] arr, int low, int middle, int high) {
		int[] temp = new int[high - low + 1];
		int i = low;
		int j = middle + 1;
		int index = 0;
		while (i <= middle && j <= high) {
			if (arr[i] <= arr[j]) {
				temp[index] = arr[i];
				i++;
			} else {
				temp[index] = arr[j];
				j++;
			}
			index++;
		}
		// 处理多余的数据
		while (j <= high) {
			temp[index] = arr[j];
			j++;
			index++;
		}
		while (i <= middle) {
			temp[index] = arr[i];
			i++;
			index++;
		}
		for (int k = 0; k < temp.length; k++) {
			arr[k + low] = temp[k];
		}
	}
}
