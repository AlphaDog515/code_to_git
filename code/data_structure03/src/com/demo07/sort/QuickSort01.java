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
		System.out.println("ʹ�õ�һ������Ϊ��׼��");
		t.quickSort01(arr, 0, arr.length - 1);
		System.out.println(Arrays.toString(arr));

		System.out.println("ʹ���м�����Ϊ��׼��");
		t.quickSort02(arr, 0, arr.length - 1);
		System.out.println(Arrays.toString(arr));
		
	}

	/*
	 * 5,1,4,6,9,10,2
	 * temp=5,ͷβ��һ��ָ��
	 * ��β����ʼ��2С�ڵ���5���滻
	 * 2,1,4,6,9,10,2
	 * 6���ڵ���5���滻
	 * 2,1,4,6,9,10,6
	 * 10����6��9����6��ָ��һ��6=temp
	 * 
	 */
	// ��������
	public static void quickSort(int[] arr, int start, int end) {
		if (start < end) {
			// �������еĵ�0��������Ϊ��׼��
			int stard = arr[start];
			// ��¼��Ҫ������±�
			int low = start;
			int high = end;
			// ѭ���ұȱ�׼��������ͱȱ�׼��С����
			while (low < high) {
				// �ұߵ����ֱȱ�׼����
				while (low < high && stard <= arr[high]) {
					high--;
				}
				// ʹ���ұߵ������滻��ߵ���
				arr[low] = arr[high];
				// �����ߵ����ֱȱ�׼��С
				while (low < high && arr[low] <= stard) {
					low++;
				}
				arr[high] = arr[low];
			}
			// �ѱ�׼�����������ڵ�λ�õ�Ԫ��
			arr[low] = stard;
			// �������е�С������
			quickSort(arr, start, low);
			// �������еĴ������
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
			if (l >= r) { // ����ѭ��������
				break;
			}
			temp = arr[l];
			arr[l] = arr[r];
			arr[r] = temp;
			
			if (arr[l] == standard) { // �������жϽ����ͬ���ֵ����,Ҳ������l++,r--
				r -= 1;
			}
			if (arr[r] == standard) {
				l += 1;
			}
		}
		
		// ����жϽ���м�ֵ�������м��������磺{9,8,10,40,56}
		// ���ݹ��м�����
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