package com.demo00;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class Test03_PriorityQueue {
	// peek()//���ض���Ԫ��
	// poll()//���ض���Ԫ�أ�����Ԫ�س�����
	// add()//���Ԫ��
	// size()//���ض���Ԫ�ظ���
	// isEmpty()//�ж϶����Ƿ�Ϊ�գ�Ϊ�շ���true,���շ���false
	public static void main(String[] args) {

		// ���ñȽ�����Ĭ����������
		Queue<Integer> q = new PriorityQueue<>();
		q.add(3);
		q.add(2);
		q.add(4);
		while (!q.isEmpty()) {
			System.out.print(q.poll() + " ");
		}

		// �Զ���Ƚ�������������
		Comparator<Integer> cmp = new Comparator<Integer>() {
			public int compare(Integer e1, Integer e2) {
				return e2 - e1;
			}
		};

		Queue<Integer> qq = new PriorityQueue<>(cmp);
		qq.add(3);
		qq.add(2);
		qq.add(4);
		while (!qq.isEmpty()) {
			System.out.print(qq.poll() + " ");
		}
	}
}
