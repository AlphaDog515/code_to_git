package com.demo07;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

public class Test {

	public static void main(String[] args) {

//		String s = "123";
//		System.out.println(s.hashCode()); // 48690

//		Deque<Integer> queue = new ArrayDeque<>();
//		Deque<Integer> queue = new LinkedList<>();
//		System.out.println(queue.size());
//		queue.addFirst(1);
//		queue.addFirst(2);
//		queue.addFirst(3);  // [3, 2]
//		queue.removeLast();  
		
		
//		queue.offer(1);
//		queue.offer(2);
//		queue.offer(3);
//		System.out.println(queue);
//		System.out.println(queue.poll());
//		System.out.println(queue.element());
//		System.out.println(queue.peek());
//		System.out.println(queue);
		
		/*
		 		出队	 ----------- 入队
		 		队头				  队尾
		 		first			 last
		 		
		 		add				addLast
		 		offer			offerLast
		 		remove			removeFirst
		 		poll			pollFirst
		 		element			getFirst
		 		peek			peekFirst
		 		
		 		push			addFirst
		 		pop				removeFirst
		 		peek			peekFirst
		 		
		 */
		
//		queue.addLast(1);
//		queue.addLast(2);
//		queue.addLast(3);
//		queue.removeFirst();
//		System.out.println(queue);   // [2, 3]
//		System.out.println(queue.getLast()); // 3
//		queue.push(1);  // addFirst(e);
//		queue.pop();    // return removeFirst();

//		char a = '0';
//		System.out.println((int) a);
		
		
	//	Byte[] arr = new Byte[10];  // null
		byte[] arr = new byte[10];  // 0 里面只能存 -128 - 127
		arr[1] = 127;
		System.out.println(Arrays.toString(arr));
		
		String res = Integer.toBinaryString(10);
		System.out.println(res);

	}

}
