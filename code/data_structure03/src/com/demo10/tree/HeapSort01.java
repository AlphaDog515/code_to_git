package com.demo10.tree;

import java.util.Arrays;

// �����򣬽��κ�һ�����鿴������ȫ����������һ�������Ժ��ŵڶ��㣬�����
// ���ڵ�����κ��ӽڵ�д󶥶ѣ����򣩣��ӽڵ��Ľ�С���ѣ�����
// �����0�������һ�������󶥶Ѷ������ߣ���������ʣ��������ɴ󶥶�
// ת�ɴ󶥶ѣ����һ����Ҷ�ӽڵ㿪ʼ�������ýڵ����ӽڵ�Աȣ��󣬲�����С������
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
		// ��ʼλ�������һ����Ҷ�ӽڵ㣬�����һ���ڵ�ĸ��ڵ�
		// ˳��洢�Ķ�����ͨ��ֻ������ȫ������
		// ��n��Ԫ�ص����ӽڵ��ǣ�2*n+1���ң�2*n+2�����ڵ㣺��n-1��/2
		int start = (arr.length - 2) / 2;
		// ����Ϊ�󶥶�
		for (int i = start; i >= 0; i--) {
			maxHeap(arr, arr.length, i);
		}
		// �Ȱ������еĵ�0���Ͷ��е����һ��������λ�ã��ٰ�ǰ��Ĵ���Ϊ�󶥶�
		for (int i = arr.length - 1; i > 0; i--) {
			int temp = arr[0];
			arr[0] = arr[i];
			arr[i] = temp;
			maxHeap(arr, i, 0); // ��һ�δ󶥶��Ժ�������Ѿ��Ǵ󶥶���
		}
	}

	// ��Ҫ�����Ľڵ㣬��������Ĵ�С���������±꣨��0��ʼ�����һ���ӽڵ㸸�ڵ㣩
	public static void maxHeap(int[] arr, int size, int index) {
		// ���ӽڵ�
		int left = 2 * index + 1;
		// ���ӽڵ�
		int right = 2 * index + 2;

		int max = index;
		// �������ӽڵ�ֱ�Աȣ��ҳ����Ľڵ�
		if (left < size && arr[left] > arr[max]) {
			max = left;
		}
		if (right < size && arr[right] > arr[max]) {
			max = right;
		}
		// ����λ��
		if (max != index) {
			int temp = arr[index];
			arr[index] = arr[max];
			arr[max] = temp;
			// arr[index]�洢��ǰ���õ�ֵ��arr[max]�洢�����ӽڵ��нϴ�ֵ������
			// ����λ���Ժ󣬿��ܻ��ƻ�֮ǰ�źõĶѣ����ԣ�֮ǰ���źõĶ���Ҫ���µ���
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
