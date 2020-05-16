package com.demo10.tree;

import java.util.Arrays;

// 堆排序，将任何一个数组看成是完全二叉树，第一层排完以后排第二层，三层等
// 父节点大于任何子节点叫大顶堆（升序），子节点大的叫小顶堆（降序）
// 数组第0个和最后一个换，大顶堆顶点移走，重新排列剩余数字组成大顶堆
// 转成大顶堆，最后一个非叶子节点开始调整，该节点与子节点对比，大，不动，小，交换
public class HeapSort01 {

	public static void main(String[] args) {
		int[] arr = new int[] { 9, 6, 8, 7, 0, 1, 10, 4, 2 };
		// int[] arr = new int[] { 6, 8, 9, 10 };

//		int start = (arr.length - 2) / 2;
//		for (int i = start; i >= 0; i--) {
//			maxHeap(arr, arr.length, i);
//		}
		heapSort(arr);
		System.out.println(Arrays.toString(arr));
	}

	public static void heapSort(int[] arr) {
		// 开始位置是最后一个非叶子节点，即最后一个节点的父节点
		// 顺序存储的二叉树通常只考虑完全二叉树
		// 第n个元素的左子节点是：2*n+1，右：2*n+2，父节点：（n-1）/2
		int start = (arr.length - 2) / 2;
		// 调整为大顶堆
		for (int i = start; i >= 0; i--) {
			maxHeap(arr, arr.length, i);
		}
		// 先把数组中的第0个和堆中的最后一个数交换位置，再把前面的处理为大顶堆
		for (int i = arr.length - 1; i > 0; i--) {
			int temp = arr[0];
			arr[0] = arr[i];
			arr[i] = temp;
			maxHeap(arr, i, 0); // 第一次大顶堆以后，下面的已经是大顶堆了
		}
	}

	// 需要调整的节点，调整数组的大小，调整的下标（从0开始，最后一个子节点父节点）
	public static void maxHeap(int[] arr, int size, int index) {
		// 左子节点
		int left = 2 * index + 1;
		// 右子节点
		int right = 2 * index + 2;

		int max = index;
		// 和两个子节点分别对比，找出最大的节点
		if (left < size && arr[left] > arr[max]) {
			max = left;
		}
		if (right < size && arr[right] > arr[max]) {
			max = right;
		}
		// 交换位置
		if (max != index) {
			int temp = arr[index];
			arr[index] = arr[max];
			arr[max] = temp;
			// arr[index]存储当前调用的值，arr[max]存储左右子节点中较大值，交换
			// 交换位置以后，可能会破坏之前排好的堆，所以，之前的排好的堆需要重新调整
			maxHeap(arr, size, max);
		}
	}

}



class Test01 {
//	public static void main(String[] args) {
//		int[] arr1 = new int[] { 9, 6, 8, 7, 0, 1, 10, 4, 2 };
//		heapSort01(arr1);
//		System.out.println(Arrays.toString(arr1));
//
//		System.out.println("----------------------");
//		int[] arr2 = new int[] { 9, 6, 8, 7, 0, 1, 10, 4, 2 };
//		heapSort02(arr2);
//		System.out.println(Arrays.toString(arr2));
//
//	}

	private static void heapSort01(int[] arr) {
		for (int i = arr.length / 2 - 1; i >= 0; i--) {
			adjustHeap01(arr, i, arr.length);
		}
		for (int j = arr.length - 1; j > 0; j--) {
			int temp = arr[j];
			arr[j] = arr[0];
			arr[0] = temp;
			adjustHeap01(arr, 0, j);
		}

	}

	public static void adjustHeap01(int arr[], int i, int length) {
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

	private static void heapSort02(int[] arr) {
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

	public static void adjustHeap02(int[] arr, int i, int length) {
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
}
