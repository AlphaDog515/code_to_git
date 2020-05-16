package com.demo07.sort;

import java.util.Arrays;

public class MergeSort01 {

	public static void main(String[] args) {
		int[] arr = new int[] { 1, 15, 5, 20, 4, 60, 8, 10 };
		System.out.println(Arrays.toString(arr));
		mergeSort(arr, 0, arr.length - 1);
		System.out.println(Arrays.toString(arr));
	}

	// �鲢����
	// ԭ����ݹ�ֲ�������������飬�½�һ�����飬���αȽ�
	public static void mergeSort(int[] arr, int low, int high) {
		int middle = (high + low) / 2;
		// һֱ�ָ������ֻ��һ��Ԫ��
		if (low < high) { // java.lang.StackOverflowError
			// �������
			mergeSort(arr, low, middle);
			// �����ұ�
			mergeSort(arr, middle + 1, high);
			// �鲢
			merge(arr, low, middle, high);
		}
	}

	// ����merge�����ϲ������Ǵ��뿪ʼ�㣬�м���Լ������㣬�ϲ�֮��Ż�ԭ���鳤�����ɲ���
	// ��ʼ�㣬�ָ�㣬������
	public static void merge(int[] arr, int low, int middle, int high) {
		// ���ڴ洢�鲢�����ʱ����
		int[] temp = new int[high - low + 1];
		// ��ʼ���м䣬�м䵽����
		// ��¼��һ����������Ҫ�������±�
		int i = low;
		// ��¼�ڶ�����������Ҫ�������±�
		int j = middle + 1;
		// ���ڼ�¼����ʱ�����д�ŵ��±�
		int index = 0;
		// ������������ȡ��С�����֣�������ʱ������
		while (i <= middle && j <= high) {
			// ��һ����������ݸ�С
			if (arr[i] <= arr[j]) {
				// ��С�����ݷ�����ʱ������
				temp[index] = arr[i];
				// ���±������һλ��
				i++;
			} else {
				temp[index] = arr[j];
				j++;
			}
			index++;
		}
		// ������������
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
		// ����ʱ�����е��������´���ԭ����
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
		// ������������
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
