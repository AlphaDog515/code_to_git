package com.demo07.sort;

import java.util.Arrays;

public class RadixSort01 {

	public static void main(String[] args) {
		int[] arr = new int[] { 23, 6, 189, 45, 9, 287, 56, 1, 798, 34, 65, 652, 5 };
		System.out.println(Arrays.toString(arr));
	}

}

class RadixSort0 {

	// ���αȽϸ�λ����ʮλ�ȣ��Ž�ȥ��Ȼ��ȡ����
	// �Ƚ�����������֮������Ƚ�0-9��Ͱ�ӣ�Ȼ���ո�λ��ʮλ����λ����
	public void radixSort01(int[] arr) {
		// ������������������
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > max) {
				max = arr[i];
			}
		}
		// ������������Ǽ�λ��
		int maxLength = (max + "").length();
		// ������ʱ�洢���ݵ�����
		int[][] temp = new int[10][arr.length];
		// ���ڼ�¼��temp����Ӧ�������д�ŵ����ֵ�����
		int[] counts = new int[10];
		// ������󳤶ȵ��������ȽϵĴ���
		for (int i = 0, n = 1; i < maxLength; i++, n *= 10) {
			// ��ÿһ�����ֱַ��������
			for (int j = 0; j < arr.length; j++) {
				// ��������
				int ys = arr[j] / n % 10;
				// �ѵ�ǰ���������ݷ���ָ����������
				temp[ys][counts[ys]] = arr[j];
				// ��¼����
				counts[ys]++;
			}
			// ��¼ȡ��Ԫ����Ҫ�ŵ�λ��
			int index = 0;
			// ������ȡ����
			for (int k = 0; k < counts.length; k++) {
				// ��¼�����������е�ǰ������¼��������Ϊ0
				if (counts[k] != 0) {
					// ѭ��ȡ��Ԫ��
					for (int l = 0; l < counts[k]; l++) {
						// ȡ��Ԫ��
						arr[index] = temp[k][l];
						// ��¼��һ��λ��
						index++;
					}
					// ��������Ϊ0
					counts[k] = 0;
				}
			}
		}
	}

	public void radixSort02(int[] arr) {
		// ������������������
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > max) {
				max = arr[i];
			}
		}
		// ������������Ǽ�λ��
		int maxLength = (max + "").length();
		// ������ʱ�洢���ݵĶ��е�����
		MyQueue[] temp = new MyQueue[10];
		// Ϊ�������鸳ֵ
		for (int i = 0; i < temp.length; i++) {
			temp[i] = new MyQueue();
		}
		// ������󳤶ȵ��������ȽϵĴ���
		for (int i = 0, n = 1; i < maxLength; i++, n *= 10) {
			// ��ÿһ�����ֱַ��������
			for (int j = 0; j < arr.length; j++) {
				// ��������
				int ys = arr[j] / n % 10;
				// �ѵ�ǰ���������ݷ���ָ���Ķ�����
				temp[ys].add(arr[j]);
			}
			// ��¼ȡ��Ԫ����Ҫ�ŵ�λ��
			int index = 0;
			// �����ж����е�����ȡ����
			for (int k = 0; k < temp.length; k++) {
				// ѭ��ȡ��Ԫ��
				while (!temp[k].isEmpty()) {
					// ȡ��Ԫ��
					arr[index] = temp[k].poll();
					// ��¼��һ��λ��
					index++;
				}
			}
		}
	}

}

// �Ƚ��ȳ�
class MyQueue {

	int[] elements;

	public MyQueue() {
		elements = new int[0];
	}

	// ���
	public void add(int element) {
		// ����һ���µ�����
		int[] newArr = new int[elements.length + 1];
		// ��ԭ�����е�Ԫ�ظ��Ƶ���������
		for (int i = 0; i < elements.length; i++) {
			newArr[i] = elements[i];
		}
		// ����ӵ�Ԫ�ط�����������
		newArr[elements.length] = element;
		// ʹ���������滻������
		elements = newArr;
	}

	// ����
	public int poll() {
		// �������еĵ�0��Ԫ��ȡ����
		int element = elements[0];
		// ����һ���µ�����
		int[] newArr = new int[elements.length - 1];
		// ����ԭ�����е�Ԫ�ص���������
		for (int i = 0; i < newArr.length; i++) {
			newArr[i] = elements[i + 1];
		}
		// �滻����
		elements = newArr;
		return element;
	}

	// �ж϶����Ƿ�Ϊ��
	public boolean isEmpty() {
		return elements.length == 0;
	}

}
