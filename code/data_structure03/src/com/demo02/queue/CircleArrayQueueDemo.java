package com.demo02.queue;

import java.util.Scanner;

public class CircleArrayQueueDemo {

	public static void main(String[] args) {

		// ����һ��
		System.out.println("��������ģ�⻷�ζ��еİ���~~~");

		// ����һ�����ζ���
		CircleArray queue = new CircleArray(4); // ����4, ����е���Ч���������3��
		// ����Ϊ1�����в�������κ�����
//		System.out.println(queue.rear);
//		System.out.println(queue.front);
//		System.out.println(queue.maxSize);
		
		char key = ' '; // �����û�����
		Scanner scanner = new Scanner(System.in);//
		boolean loop = true;
		// ���һ���˵�
		while (loop) {
			System.out.println("s(show): ��ʾ����");
			System.out.println("e(exit): �˳�����");
			System.out.println("a(add): ������ݵ�����");
			System.out.println("g(get): �Ӷ���ȡ������");
			System.out.println("h(head): �鿴����ͷ������");
			key = scanner.next().charAt(0);// ����һ���ַ�
			switch (key) {
			case 's':
				queue.showQueue();
				break;
			case 'a':
				System.out.println("���һ����");
				int value = scanner.nextInt();
				queue.addQueue(value);
				break;
			case 'g': // ȡ������
				try {
					int res = queue.getQueue();
					System.out.printf("ȡ����������%d\n", res);
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e.getMessage());
				}
				break;
			case 'h': // �鿴����ͷ������
				try {
					int res = queue.headQueue();
					System.out.printf("����ͷ��������%d\n", res);
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e.getMessage());
				}
				break;
			case 'e': // �˳�
				scanner.close();
				loop = false;
				break;
			default:
				break;
			}
		}
		System.out.println("�����˳�~~");
	}

}

class CircleArray {
	/*	���е�������4,��ʼ���front=rear=0			��������front=0,rear=3��ʵ����ֻ������Ԫ��
	 *   0		1		2		3				0		1		2 		3
	 * front									front					rear
	 * rear
	 */	
	public int maxSize; // ��ʾ������������
	
	// front �����ĺ�����һ�������� front ��ָ����еĵ�һ��Ԫ��, Ҳ����˵ arr[front] ���Ƕ��еĵ�һ��Ԫ��
	// front �ĳ�ʼֵ = 0
	public int front;
	
	// rear �����ĺ�����һ��������rear ָ����е����һ��Ԫ�صĺ�һ��λ��. ��Ϊϣ���ճ�һ���ռ���ΪԼ��.
	// rear �ĳ�ʼֵ = 0
	public int rear; // ����β
	private int[] arr; // ���������ڴ������, ģ�����

	public CircleArray(int arrMaxSize) {
		maxSize = arrMaxSize;
		arr = new int[maxSize];
	}

	// �ж϶����Ƿ���
	public boolean isFull() {
		return (rear + 1) % maxSize == front;
	}

	// �ж϶����Ƿ�Ϊ��
	public boolean isEmpty() {
		return rear == front;
	}

	// ������ݵ�����
	public void addQueue(int n) {
		// �ж϶����Ƿ���
		if (isFull()) {
			System.out.println("�����������ܼ�������~");
			return;
		}
		// ֱ�ӽ����ݼ���
		arr[rear] = n;
		// �� rear ����, ������뿼��ȡģ
		rear = (rear + 1) % maxSize;
	}

	// ��ȡ���е�����, ������
	public int getQueue() {
		// �ж϶����Ƿ��
		if (isEmpty()) {
			// ͨ���׳��쳣
			throw new RuntimeException("���пգ�����ȡ����");
		}
		// ������Ҫ������ front��ָ����еĵ�һ��Ԫ��
		// 1. �Ȱ� front ��Ӧ��ֵ������һ����ʱ����
		// 2. �� front ����, ����ȡģ
		// 3. ����ʱ����ı�������
		int value = arr[front];
		front = (front + 1) % maxSize;
		return value;

	}

	// ��ʾ���е���������
	public void showQueue() {
		// ����
		if (isEmpty()) {
			System.out.println("���пյģ�û������~~");
			return;
		}
		// ˼·����front��ʼ�������������ٸ�Ԫ��
		// ���Խ�
		for (int i = front; i < front + size(); i++) {
			System.out.printf("arr[%d]=%d\n", i % maxSize, arr[i % maxSize]);
		}
	}

	// �����ǰ������Ч���ݵĸ���
	public int size() {
		// rear = 2
		// front = 1
		// maxSize = 3
//		return (rear + maxSize - front) % maxSize;
		return (rear - front + maxSize) % maxSize;
	}

	// ��ʾ���е�ͷ���ݣ� ע�ⲻ��ȡ������
	public int headQueue() {
		// �ж�
		if (isEmpty()) {
			throw new RuntimeException("���пյģ�û������~~");
		}
		return arr[front];
	}
}