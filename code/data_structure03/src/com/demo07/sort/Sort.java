package com.demo07.sort;

import java.util.Arrays;

public class Sort {
	public static void main(String[] args) {

		int[] arr = { 9, 10, 8, 9, 1 };
		TestSort t = new TestSort();
		t.heapSort03(arr);
		System.out.println(Arrays.toString(arr));
	}
}

class TestSort {

	// 冒泡排序法
	public void bubbleSort(int[] arr) {
		for (int i = 0; i < arr.length - 1; i++) {
			for (int j = 0; j < arr.length - 1 - i; j++) {
				if (arr[j] > arr[j + 1]) {
					int temp = arr[j];
					arr[j] = arr[j + 1];
					arr[j + 1] = temp;
				}
			}
		}
	}

	// 冒泡排序法的改进
	public void bubbleSort01(int[] arr) {
		boolean flag = false;
		for (int i = 0; i < arr.length - 1; i++) {
			for (int j = 0; j < arr.length - 1 - i; j++) {
				if (arr[j] > arr[j + 1]) {
					flag = true;
					int temp = arr[j];
					arr[j] = arr[j + 1];
					arr[j + 1] = temp;
				}
			}
			if (!flag) {
				break;
			} else {
				flag = false;
			}
		}
	}

	// 交换法:1->gap => shell
	public void bubbleSort02(int[] arr) {
		for (int i = 1; i < arr.length; i++) {
			for (int j = i - 1; j >= 0; j -= 1) {
				if (arr[j] > arr[j + 1]) {
					int temp = arr[j];
					arr[j] = arr[j + 1];
					arr[j + 1] = temp;
				}
			}
		}
	}

	// 希尔排序1
	public void shellSort01(int[] arr) {
		for (int gap = arr.length / 2; gap > 0; gap /= 2) {
			for (int i = gap; i < arr.length; i++) {
				for (int j = i - gap; j >= 0; j -= gap) {
					if (arr[j] > arr[j + gap]) {
						int temp = arr[j];
						arr[j] = arr[j + gap];
						arr[j + gap] = temp;
					}
				}
			}
		}
	}

	// 插入排序法:1->gap => shell
	public void insertSort01(int[] arr) {
		for (int i = 1; i < arr.length; i++) {
			int j = i;
			int temp = arr[i];
			while (j - 1 >= 0 && temp < arr[j - 1]) {
				arr[j] = arr[j - 1];
				j -= 1;
			}
			arr[j] = temp;
		}
	}

	// 希尔排序2
	public void shellSort2(int[] arr) {
		for (int gap = arr.length / 2; gap > 0; gap /= 2) {
			for (int i = gap; i < arr.length; i++) {
				int j = i;
				int temp = arr[i];
				while (j - gap >= 0 && temp < arr[j - gap]) {
					arr[j] = arr[j - gap];
					j -= gap;
				}
				arr[j] = temp;
			}
		}
	}

	// 选择排序1
	public void selectSort01(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = i + 1; j < arr.length; j++) {
				if (arr[i] > arr[j]) {
					int temp = arr[i];
					arr[i] = arr[j];
					arr[j] = temp;
				}
			}
		}
	}

	// 选择排序2
	public void selectSort02(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			int minIndex = i;
			for (int j = i + 1; j < arr.length; j++) {
				if (arr[minIndex] > arr[j]) {
					minIndex = j;
				}
			}
			if (i != minIndex) {
				int temp = arr[i];
				arr[i] = arr[minIndex];
				arr[minIndex] = temp;
			}
		}
	}

	// 归并排序
	public void mergeSort(int[] arr, int left, int right) {
		int middle = (left + right) / 2;
		if (left < right) {
			mergeSort(arr, left, middle);
			mergeSort(arr, middle + 1, right);
			merge(arr, left, middle, right);
		}
	}

	public void merge(int[] arr, int left, int middle, int right) {
		int[] temp = new int[right - left + 1];
		int i = left;
		int j = middle + 1;
		int index = 0;
		while (i <= middle && j <= right) {
			if (arr[i] <= arr[j]) {
				temp[index] = arr[i];
				i++;
			} else {
				temp[index] = arr[j];
				j++;
			}
			index++;
		}
		while (i <= middle) {
			temp[index] = arr[i];
			i++;
			index++;
		}
		while (j <= right) {
			temp[index] = arr[j];
			j++;
			index++;
		}
		for (int k = 0; k < temp.length; k++) {
			arr[left + k] = temp[k];
		}
	}

	// 快速排序1
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
	
	// 快速排序
	public void quickSort02(int[] arr, int left, int right) {
		if (left < right) {
			int l = left;
			int r = right;
			while (l < r) {
				while (l <= r && arr[l] <= arr[left]) {
					l++;
				}
				while (l <= r && arr[r] >= arr[left]) {
					r--;
				}

				if (l < r) {
					int temp = arr[l];
					arr[l] = arr[r];
					arr[r] = temp;
				}
			}

			int temp = arr[left];
			arr[left] = arr[r];
			arr[r] = temp;

			quickSort02(arr, left, r - 1);
			quickSort02(arr, r + 1, right);

		}
	}

	// 快速排序2
	public void quickSort03(int[] arr, int left, int right) {
		int l = left;
		int r = right;
		int pivot = arr[(left + right) / 2];
		while (l < r) {
			while (arr[l] < pivot) {
				l++;
			}
			while (arr[r] > pivot) {
				r--;
			}
			if (arr[l] == arr[r] && l < r) {
				l++;
			} else {
				int temp = arr[l];
				arr[l] = arr[r];
				arr[r] = temp;
			}
		}
		if (l - 1 > left) {
			quickSort03(arr, left, l - 1);
		}
		if (r + 1 < right) {
			quickSort03(arr, r + 1, right);
		}
	}

	// 基数排序
	public void radixSort(int[] arr) {
		int max = arr[0];
		for (int i = 1; i < arr.length; i++) {
			if (max < arr[i]) {
				max = arr[i];
			}
		}
		int maxLength = (max + "").length();
		int[][] bucket = new int[10][arr.length];
		int[] count = new int[10];
		for (int i = 0, n = 1; i < maxLength; i++, n *= 10) {
			for (int j = 0; j < arr.length; j++) {
				int element = arr[j] / n % 10;
				bucket[element][count[element]] = arr[j];
				count[element]++;
			}

			int index = 0;
			for (int k = 0; k < count.length; k++) {
				if (count[k] != 0) {
					for (int l = 0; l < count[k]; l++) {
						arr[index] = bucket[k][l];
						index++;
					}
				}
				count[k] = 0;
			}
		}
	}

	// 堆排序1
	public void heapSort01(int arr[]) {
		int temp = 0;
		for (int i = arr.length / 2 - 1; i >= 0; i--) {
			adjustHeap01(arr, i, arr.length);
		}

		for (int j = arr.length - 1; j > 0; j--) {
			temp = arr[j];
			arr[j] = arr[0];
			arr[0] = temp;
			adjustHeap01(arr, 0, j);
		}
	}

	public void adjustHeap01(int arr[], int i, int length) {
		int temp = arr[i];
		for (int k = i * 2 + 1; k < length; k = k * 2 + 1) {
			if (k + 1 < length && arr[k] < arr[k + 1]) {
				k++;
			}
			if (arr[k] > temp) {
				arr[i] = arr[k];
				i = k;
			} else {
				break;
			}
		}
		arr[i] = temp;
	}

	// 堆排序2
	public void heapSort02(int[] arr) {
		for (int i = arr.length / 2 - 1; i >= 0; i--) {
			adjustHeap02(arr, i, arr.length);
		}
		for (int j = arr.length - 1; j > 0; j--) {
			int temp = arr[j];
			arr[j] = arr[0];
			arr[0] = temp;
			adjustHeap02(arr, 0, j);
		}
	}

	public void adjustHeap02(int[] arr, int i, int length) {
		int left = 2 * i + 1;
		int right = 2 * i + 2;
		int max = i;
		if (left < length && arr[left] > arr[max]) {
			max = left;
		}
		if (right < length && arr[right] > arr[max]) {
			max = right;
		}
		if (max != i) {
			int temp = arr[i];
			arr[i] = arr[max];
			arr[max] = temp;
			adjustHeap02(arr, max, length);
		}
	}

	// 堆排序3
	public void heapSort03(int arr[]) {
		for (int i = arr.length / 2 - 1; i >= 0; i--) {
			adjustHeap03(arr, i, arr.length);
		}

		for (int j = arr.length - 1; j > 0; j--) {
			int temp = arr[j];
			arr[j] = arr[0];
			arr[0] = temp;
			adjustHeap03(arr, 0, j);
		}
	}

	private void adjustHeap03(int[] arr, int i, int length) {
		// 临时变量 minPos 用于存储最小值的下标，先假设父节点最小
		int minPos = i;
		while (true) {
			if (2 * i + 1 < length && arr[2 * i + 1] < arr[minPos]) {
				minPos = 2 * i + 1;
			}
			if (2 * i + 2 < length && arr[2 * i + 2] < arr[minPos]) {
				minPos = 2 * i + 2;
			}
			if (minPos == i) {
				break;
			}
			swap(arr, i, minPos);
			i = minPos;
		}
	}

	private void swap(int[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

}