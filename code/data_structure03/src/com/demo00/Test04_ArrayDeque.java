package com.demo00;

import java.util.ArrayDeque;

public class Test04_ArrayDeque {
	/*
	  	get、peek、element方法都是获取元素，但是不会将它移除， 
	  	而pop、poll、remove都会将元素移除并返回，
	  	add和push、offer都是插入元素，它们的不同点在于插入元素的位置以及插入失败后的结果。
	  
	  	 		          从队列首部插入/取出	 		                          从队列尾部插入/取出
	 		  失败抛出异常	                        失败返回特殊值	    失败抛出异常	     失败返回特殊值
		插入  addFirst(e),push()	    offerFirst(e)	  addLast(e)	   offerLast(e)
		移除	 removeFirst(),pop()	pollFirst()	      removeLast()	   pollLast()
		获取	 getFirst(),            peekFirst()	      getLast()	       peekLast()
	  
	    // calculateSize方法用于计算比numElements大的最小2的幂次方
	  	private static int calculateSize(int numElements) {
        int initialCapacity = MIN_INITIAL_CAPACITY;
        // Find the best power of two to hold elements.
        // Tests "<=" because arrays aren't kept full.
        if (numElements >= initialCapacity) {
            initialCapacity = numElements;
            initialCapacity |= (initialCapacity >>>  1);
            initialCapacity |= (initialCapacity >>>  2);
            initialCapacity |= (initialCapacity >>>  4);
            initialCapacity |= (initialCapacity >>>  8);
            initialCapacity |= (initialCapacity >>> 16);
            initialCapacity++;

            if (initialCapacity < 0)   // Too many elements, must back off
                initialCapacity >>>= 1;// Good luck allocating 2 ^ 30 elements
        }
        return initialCapacity;
    }
	  
	 */

	public static void main(String[] args) {
		// 初始化容量为4 MIN_INITIAL_CAPACITY=8
		ArrayDeque<String> adq = new ArrayDeque<>(4);
		System.out.println(adq.size()); // 0

		// 添加元素
		adq.add("A");
		adq.add("B");
		adq.add("C");
		System.out.println(adq.size()); // 3
		System.out.println(adq); // [A, B, C]

		// 获取元素
		adq.getFirst(); // A
		adq.pop(); // A
		System.out.println(adq); // [B, C]

		System.out.println(adq.element()); // B
		System.out.println(adq); // [B, C]

		System.out.println(adq.removeFirst()); // B
		System.out.println(adq); // [C]

		adq.add("A");
		adq.add("B");
		System.out.println(adq); // [C, A, B]
		System.out.println(adq.peek()); // C
		System.out.println(adq.poll()); // C
		System.out.println(adq); // [A, B]

		String a = adq.pollFirst();
		String b = adq.pollLast();
		String c = adq.peekFirst();
		String d = adq.peekLast();
		String e = adq.removeLast();

		// 添加元素
		adq.push(a);
		adq.add(a);
		adq.offer(a);
		adq.offerFirst(a);
		adq.offerLast(a);
		adq.offerLast(a);
		adq.offerLast(a);
		adq.offerLast(a);
		adq.offerLast(a);
		adq.offerLast(a);
		System.out.println(adq);

		// 移除第一次出现的C
		adq.removeFirstOccurrence(a);
		System.out.println(adq);

		// 移除最后一次出现的C
		adq.removeLastOccurrence(a);
		System.out.println(adq);

	}
}