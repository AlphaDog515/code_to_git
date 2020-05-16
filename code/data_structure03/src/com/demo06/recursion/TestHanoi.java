package com.demo06.recursion;

public class TestHanoi {
	// ��Ա�����ֽ�ȫ�ֱ�����������Ϊ�����࣬��Ĭ�ϳ�ʼ��ֵ�����ȼ��ͣ�
	// �ֲ��������������ڷ�����Ĭ��û�г�ʼ��ֵ�����ȼ��ߣ�
	public static int c = 0; // ͳ�Ƶݹ���ôӴ���

	public static void main(String[] args) {
		int c = hanoi(3, 'A', 'B', 'C');
		System.out.println("�ݹ���ô���Ϊ:" + c);
	}

	/**
	 * @param n    ����N������
	 * @param from ��ʼ������
	 * @param in   �м������
	 * @param to   Ŀ������ �����ж��ٸ����ӣ�����Ϊֻ��������������������Ӻ�������һ�����ӡ�
	 */
//	public static void hanoi(int n, char from, char in, char to) {
//
//		// ֻ��һ�����ӡ�
//		if (n == 1) {
//			System.out.println("��1�����Ӵ�" + from + "�Ƶ�" + to);
//
//			// �����ж��ٸ����ӣ�����Ϊֻ��������������������Ӻ�������һ�����ӡ�
//		} else {
//			// �ƶ��������е����ӵ��м�λ��
//			hanoi(n - 1, from, to, in);
//			// �ƶ����������
//			System.out.println("��" + n + "�����Ӵ�" + from + "�Ƶ�" + to);
//			// ��������������Ӵ��м�λ���Ƶ�Ŀ��λ��
//			hanoi(n - 1, in, from, to);
//		}
//
//	}

	public static int hanoi(int n, char from, char in, char to) {
		c++;
		// ֻ��һ�����ӡ�
		if (n == 1) {
			System.out.println("��1�����Ӵ�" + from + "�Ƶ�" + to);
			// �����ж��ٸ����ӣ�����Ϊֻ��������������������Ӻ�������һ�����ӡ�
		} else {
			// �ƶ��������е����ӵ��м�λ��
			hanoi(n - 1, from, to, in);
			// �ƶ����������
			System.out.println("��" + n + "�����Ӵ�" + from + "�Ƶ�" + to);
			// ��������������Ӵ��м�λ���Ƶ�Ŀ��λ��
			hanoi(n - 1, in, from, to);
		}
		return c;
	}

}
