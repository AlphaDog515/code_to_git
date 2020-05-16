package com.demo01.sparsearray;

import java.util.Arrays;

// 罗召勇，自定义数组
public class UserDefinedArray {
	public static void main(String[] args) {
		// demo();

		System.out.println("-----------------------------");
		System.out.println("面向对象的数组");
		MyArray ma = new MyArray();

		// 获取长度
//		int size = ma.size();
//		System.out.println(size);
//		ma.show();

		ma.add(9);
		ma.add(18);
		ma.add(27);
		ma.add(39);
		ma.add(90);
		ma.add(117);

		ma.show();

		// 删除某个元素
//		ma.delete(1);
//		ma.delete(-1);
//		ma.show();

//		int element = ma.get(1);
//		System.out.println(element);
//		System.out.println("=====================");
//		ma.add(96);
//		ma.add(95);
//		ma.add(94);
//		ma.show();

//		ma.insert(3, 33);
//		ma.show();
//		System.out.println("=====================");

//		ma.set(-1, 100);
//		ma.show();
//		System.out.println(ma.size());

		int index = ma.search(117);
		System.out.println("index:"+index);

//		int index2 = ma.binarySearch(9);
//		System.out.println("index2:" + index2);
	}

	private static void demo() {
		int total = 0;
		int end = 100;
		// 使用for循环计算
		for (int i = 1; i <= end; i++) {
			total += i;
		}

		// 直接计算
		total = (1 + end) * end / 2;
		System.out.println(total);
	}

}

// 创建一个新的数组，遍历原数组，判断要操作的下标采取相应的策略
class MyArray {
	private int[] elements;

	public MyArray() {
		elements = new int[0];
	}

	public int size() {
		return elements.length;
	}

	public void add(int element) {
		int[] newArr = new int[elements.length + 1];
		for (int i = 0; i < elements.length; i++) {
			newArr[i] = elements[i];
		}
		newArr[elements.length] = element;
		elements = newArr;
	}

	public void show() {
		System.out.println(Arrays.toString(elements));
	}

	// 删除数组中的元素
	public void delete(int index) {
		// 判断下标是否越界
		if (index < 0 || index > elements.length - 1) {
			throw new RuntimeException("下标越界");
		}

		int[] newArr = new int[elements.length - 1];
		// 复制原有数据到新数组
		for (int i = 0; i < newArr.length; i++) {
			// 想要删除的元素前面的元素
			if (i < index) {
				newArr[i] = elements[i];
				// 想要删除的元素后面的元素
			} else {
				newArr[i] = elements[i + 1];
			}
		}
		// 新数组替换旧数组
		elements = newArr;
	}

	public int get(int index) {
		// 判断下标是否越界
		if (index < 0 || index > elements.length - 1) {
			throw new RuntimeException("下标越界");
		}
		return elements[index];
	}

	public void insert(int index, int element) {
		int[] newArr = new int[elements.length + 1];
		for (int i = 0; i < elements.length; i++) {
			if (i < index) {
				newArr[i] = elements[i];
			} else {
				newArr[i + 1] = elements[i];
			}
		}
		// 插入新的元素
		newArr[index] = element;
		// 新数组替换旧数组
		elements = newArr;
	}

	public void set(int index, int element) {
		// 判断下标是否越界
		if (index < 0 || index > elements.length - 1) {
			throw new RuntimeException("下标越界");
		}
		elements[index] = element;
	}

	public int search(int target) {
		// 遍历数组
		for (int i = 0; i < elements.length; i++) {
			if (elements[i] == target) {
				return i;
			}
		}
		return -1;
	}

	public int binarySearch(int target) {
		int begin = 0;
		int end = elements.length - 1;
		while (begin <= end) {
			int mid = (begin + end) / 2;
			if (elements[mid] == target) {
				return mid;
			} else if (elements[mid] > target) {
				end = mid - 1;
			} else {
				begin = mid + 1;
			}
		}
		return -1;
	}

}
