package com.demo04.stack;

public class TestMyStack {

	public static void main(String[] args) {
		// 创建一个栈
		MyStack ms = new MyStack();
		// 压入数组
		ms.push(9);
		ms.push(8);
		ms.push(7);
		// 最出栈顶元素
		System.out.println(ms.pop());
		System.out.println(ms.pop());
		System.out.println(ms.pop());
		// 查看栈顶元素
		// System.out.println(ms.peek());
		System.out.println(ms.isEmpty());
	}

}

class MyStack {

	// 栈的底层我们使用数组来存储数据
	int[] elements;

	public MyStack() {
		elements = new int[0];
	}

	// 压入元素
	public void push(int element) {
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

	// 取出栈顶元素
	public int pop() {
		// 栈中没有元素
		if (elements.length == 0) {
			throw new RuntimeException("stack is empty");
		}
		// 取出数组的最后一个元素
		int element = elements[elements.length - 1];
		// 创建一个新的数组
		int[] newArr = new int[elements.length - 1];
		// 原数组中除了最后一个元素的其它元素都放入新的数组中
		for (int i = 0; i < elements.length - 1; i++) {
			newArr[i] = elements[i];
		}
		// 替换数组
		elements = newArr;
		// 返回栈顶元素
		return element;
	}

	// 查看栈顶元素
	public int peek() {
		// 栈中没有元素
		if (elements.length == 0) {
			throw new RuntimeException("stack is empty");
		}
		return elements[elements.length - 1];
	}

	// 判断栈是否为空
	public boolean isEmpty() {
		return elements.length == 0;
	}

}
