package com.demo04.stack;

public class Calculator01 {
	// 解决老师代码中，最后栈中操作符优先级如果一样，不是从左到右计算的问题
	public static void main(String[] args) {
		String expression = "7*2-4-12/2+8/2";
		ArrayStack01 numStack = new ArrayStack01(10);
		ArrayStack01 operStack = new ArrayStack01(10);
		int index = 0;
		int num1 = 0;
		int num2 = 0;
		int oper = 0;
		int res = 0;
		char ch = ' ';
		String keepNum = "";
		while (true) {
			ch = expression.substring(index, index + 1).charAt(0);
			if (operStack.isOper(ch)) {
				while (!operStack.isEmpty() && operStack.priority(ch) <= operStack.priority(operStack.peek())) {
					num1 = numStack.pop();
					num2 = numStack.pop();
					oper = operStack.pop();
					res = numStack.cal(num1, num2, oper);
					numStack.push(res);
				}
				operStack.push(ch);
			} else {
				keepNum += ch;
				if (index == expression.length() - 1) {
					numStack.push(Integer.parseInt(keepNum));
				} else {
					if (operStack.isOper(expression.substring(index + 1, index + 2).charAt(0))) {
						numStack.push(Integer.parseInt(keepNum));
						keepNum = "";
					}
				}
			}

			index++;
			if (index >= expression.length()) {
				break;
			}
		}

		while (true) {
			if (operStack.isEmpty()) {
				break;
			}
			num1 = numStack.pop();
			num2 = numStack.pop();
			oper = operStack.pop();
			res = numStack.cal(num1, num2, oper);
			numStack.push(res);
		}

		int res2 = numStack.pop();
		System.out.printf("表达式:%s=%d", expression, res2);
	}
}

class ArrayStack01 {
	private int maxSize;
	private int[] stack;
	private int top = -1;

	public ArrayStack01(int maxSize) {
		this.maxSize = maxSize;
		stack = new int[this.maxSize];

	}

	public int peek() {
		return stack[top];
	}

	public boolean isFull() {
		return top == maxSize - 1;
	}

	public boolean isEmpty() {
		return top == -1;
	}

	public void push(int value) {
		if (isFull()) {
			throw new RuntimeException("栈满了");

		}

		top++;
		stack[top] = value;
	}

	public int pop() {
		if (isEmpty()) {
			throw new RuntimeException("没有数据");
		}

		int temp = stack[top];
		top--;
		return temp;
	}

	public void list() {
		if (isEmpty()) {
			throw new RuntimeException("没有数据无法遍历");
		}
		for (int i = 0; i < stack.length; i++) {
			System.out.println(stack[i]);
		}
	}

	public int priority(int oper) {
		if (oper == '*' || oper == '/') {
			return 1;
		} else if (oper == '-' || oper == '+') {
			return 0;
		} else {
			return -1;
		}
	}

	public boolean isOper(int val) {
		return val == '+' || val == '-' || val == '*' || val == '/';
	}

	public int cal(int num1, int num2, int oper) {
		int res = 0;
		switch (oper) {
		case '+':
			res = num1 + num2;
			break;
		case '-':
			res = num2 - num1;
			break;
		case '*':
			res = num1 * num2;
			break;
		case '/':
			res = num2 / num1;
			break;
		default:
			break;
		}
		return res;
	}

}