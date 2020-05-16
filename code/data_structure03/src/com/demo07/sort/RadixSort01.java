package com.demo07.sort;

import java.util.Arrays;

public class RadixSort01 {

	public static void main(String[] args) {
		int[] arr = new int[] { 23, 6, 189, 45, 9, 287, 56, 1, 798, 34, 65, 652, 5 };
		System.out.println(Arrays.toString(arr));
	}

}

class RadixSort0 {

	// 依次比较个位数，十位等，放进去，然后取出来
	// 比较适用于数字之间差别大，先建0-9个桶子，然后按照个位，十位，百位排序
	public void radixSort01(int[] arr) {
		// 存最数组中最大的数字
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > max) {
				max = arr[i];
			}
		}
		// 计算最大数字是几位数
		int maxLength = (max + "").length();
		// 用于临时存储数据的数组
		int[][] temp = new int[10][arr.length];
		// 用于记录在temp中相应的数组中存放的数字的数量
		int[] counts = new int[10];
		// 根据最大长度的数决定比较的次数
		for (int i = 0, n = 1; i < maxLength; i++, n *= 10) {
			// 把每一个数字分别计算余数
			for (int j = 0; j < arr.length; j++) {
				// 计算余数
				int ys = arr[j] / n % 10;
				// 把当前遍历的数据放入指定的数组中
				temp[ys][counts[ys]] = arr[j];
				// 记录数量
				counts[ys]++;
			}
			// 记录取的元素需要放的位置
			int index = 0;
			// 把数字取出来
			for (int k = 0; k < counts.length; k++) {
				// 记录数量的数组中当前余数记录的数量不为0
				if (counts[k] != 0) {
					// 循环取出元素
					for (int l = 0; l < counts[k]; l++) {
						// 取出元素
						arr[index] = temp[k][l];
						// 记录下一个位置
						index++;
					}
					// 把数量置为0
					counts[k] = 0;
				}
			}
		}
	}

	public void radixSort02(int[] arr) {
		// 存最数组中最大的数字
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > max) {
				max = arr[i];
			}
		}
		// 计算最大数字是几位数
		int maxLength = (max + "").length();
		// 用于临时存储数据的队列的数组
		MyQueue[] temp = new MyQueue[10];
		// 为队列数组赋值
		for (int i = 0; i < temp.length; i++) {
			temp[i] = new MyQueue();
		}
		// 根据最大长度的数决定比较的次数
		for (int i = 0, n = 1; i < maxLength; i++, n *= 10) {
			// 把每一个数字分别计算余数
			for (int j = 0; j < arr.length; j++) {
				// 计算余数
				int ys = arr[j] / n % 10;
				// 把当前遍历的数据放入指定的队列中
				temp[ys].add(arr[j]);
			}
			// 记录取的元素需要放的位置
			int index = 0;
			// 把所有队列中的数字取出来
			for (int k = 0; k < temp.length; k++) {
				// 循环取出元素
				while (!temp[k].isEmpty()) {
					// 取出元素
					arr[index] = temp[k].poll();
					// 记录下一个位置
					index++;
				}
			}
		}
	}

}

// 先进先出
class MyQueue {

	int[] elements;

	public MyQueue() {
		elements = new int[0];
	}

	// 入队
	public void add(int element) {
		// 创建一个新的数组
		int[] newArr = new int[elements.length + 1];
		// 把原数组中的元素复制到新数组中
		for (int i = 0; i < elements.length; i++) {
			newArr[i] = elements[i];
		}
		// 把添加的元素放入新数组中
		newArr[elements.length] = element;
		// 使用新数组替换旧数组
		elements = newArr;
	}

	// 出队
	public int poll() {
		// 把数组中的第0个元素取出来
		int element = elements[0];
		// 创建一个新的数组
		int[] newArr = new int[elements.length - 1];
		// 复制原数组中的元素到新数组中
		for (int i = 0; i < newArr.length; i++) {
			newArr[i] = elements[i + 1];
		}
		// 替换数组
		elements = newArr;
		return element;
	}

	// 判断队列是否为空
	public boolean isEmpty() {
		return elements.length == 0;
	}

}
