package com.demo04.stack;

import java.util.Stack;

//��ʾջStack�Ļ���ʹ��
public class TestStack {

	public static void main(String[] args) {
		Stack<String> stack = new Stack<String>();
		// ��ջ
		stack.add("jack");
		stack.add("tom");
		stack.add("smith");

		// ��ջ
		// smith, tom , jack
		while (stack.size() > 0) {
			System.out.println(stack.pop());// pop���ǽ�ջ��������ȡ��
		}
	}

}
