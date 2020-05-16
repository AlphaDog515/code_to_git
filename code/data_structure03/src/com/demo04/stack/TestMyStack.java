package com.demo04.stack;

public class TestMyStack {

	public static void main(String[] args) {
		// ����һ��ջ
		MyStack ms = new MyStack();
		// ѹ������
		ms.push(9);
		ms.push(8);
		ms.push(7);
		// ���ջ��Ԫ��
		System.out.println(ms.pop());
		System.out.println(ms.pop());
		System.out.println(ms.pop());
		// �鿴ջ��Ԫ��
		// System.out.println(ms.peek());
		System.out.println(ms.isEmpty());
	}

}

class MyStack {

	// ջ�ĵײ�����ʹ���������洢����
	int[] elements;

	public MyStack() {
		elements = new int[0];
	}

	// ѹ��Ԫ��
	public void push(int element) {
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

	// ȡ��ջ��Ԫ��
	public int pop() {
		// ջ��û��Ԫ��
		if (elements.length == 0) {
			throw new RuntimeException("stack is empty");
		}
		// ȡ����������һ��Ԫ��
		int element = elements[elements.length - 1];
		// ����һ���µ�����
		int[] newArr = new int[elements.length - 1];
		// ԭ�����г������һ��Ԫ�ص�����Ԫ�ض������µ�������
		for (int i = 0; i < elements.length - 1; i++) {
			newArr[i] = elements[i];
		}
		// �滻����
		elements = newArr;
		// ����ջ��Ԫ��
		return element;
	}

	// �鿴ջ��Ԫ��
	public int peek() {
		// ջ��û��Ԫ��
		if (elements.length == 0) {
			throw new RuntimeException("stack is empty");
		}
		return elements[elements.length - 1];
	}

	// �ж�ջ�Ƿ�Ϊ��
	public boolean isEmpty() {
		return elements.length == 0;
	}

}
