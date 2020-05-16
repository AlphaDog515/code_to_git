package com.demo00;

import java.util.ArrayDeque;

public class Test04_ArrayDeque {
	/*
	  	get��peek��element�������ǻ�ȡԪ�أ����ǲ��Ὣ���Ƴ��� 
	  	��pop��poll��remove���ὫԪ���Ƴ������أ�
	  	add��push��offer���ǲ���Ԫ�أ����ǵĲ�ͬ�����ڲ���Ԫ�ص�λ���Լ�����ʧ�ܺ�Ľ����
	  
	  	 		          �Ӷ����ײ�����/ȡ��	 		                          �Ӷ���β������/ȡ��
	 		  ʧ���׳��쳣	                        ʧ�ܷ�������ֵ	    ʧ���׳��쳣	     ʧ�ܷ�������ֵ
		����  addFirst(e),push()	    offerFirst(e)	  addLast(e)	   offerLast(e)
		�Ƴ�	 removeFirst(),pop()	pollFirst()	      removeLast()	   pollLast()
		��ȡ	 getFirst(),            peekFirst()	      getLast()	       peekLast()
	  
	    // calculateSize�������ڼ����numElements�����С2���ݴη�
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
		// ��ʼ������Ϊ4 MIN_INITIAL_CAPACITY=8
		ArrayDeque<String> adq = new ArrayDeque<>(4);
		System.out.println(adq.size()); // 0

		// ���Ԫ��
		adq.add("A");
		adq.add("B");
		adq.add("C");
		System.out.println(adq.size()); // 3
		System.out.println(adq); // [A, B, C]

		// ��ȡԪ��
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

		// ���Ԫ��
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

		// �Ƴ���һ�γ��ֵ�C
		adq.removeFirstOccurrence(a);
		System.out.println(adq);

		// �Ƴ����һ�γ��ֵ�C
		adq.removeLastOccurrence(a);
		System.out.println(adq);

	}
}