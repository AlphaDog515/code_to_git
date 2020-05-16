package com.demo03.linkedlist01;

public class HashTabTest {
	public static void main(String[] args) {
		HashTab hashTab = new HashTab(3);
		Student s1 = new Student(1, "aaa");
		Student s2 = new Student(2, "bbb");
		Student s3 = new Student(3, "ccc");
		Student s4 = new Student(4, "ddd");
		Student s5 = new Student(5, "eee");
		hashTab.add(s1);
		hashTab.add(s2);
		hashTab.add(s3);
		hashTab.add(s4);
		hashTab.add(s5);
		hashTab.show();
		hashTab.findStudentById(3);
	}
}

class HashTab {

	private StudentLinkedList[] studentListArray;
	private int size;

	public HashTab(int size) {
		this.size = size;
		studentListArray = new StudentLinkedList[size];
		for (int i = 0; i < size; i++) {
			studentListArray[i] = new StudentLinkedList();
		}
	}

	// ���
	public void add(Student student) {
		int studentListNum = hashFun(student.id);
		studentListArray[studentListNum].add(student);
	}

	// ����HashTab
	public void show() {
		for (int i = 0; i < size; i++) {
			studentListArray[i].show(i);
		}
	}

	public int hashFun(int id) {
		return id % size;
	}

	// ����id���ҹ�Ա
	public void findStudentById(int id) {
		int studentListNum = hashFun(id);
		Student student = studentListArray[studentListNum].findStudentById(id);
		if (student != null) {
			System.out.printf("�ڵ�%d���������ҵ�ѧ����id = %d\n", (studentListNum + 1), id);
		} else {
			System.out.println("û���ҵ���ѧ����");
		}
	}

}

class StudentLinkedList {
	private Student head;

	// ����
	public void add(Student student) {
		if (head == null) {
			head = student;
			return;
		}
		Student curr = head;
		while (true) {
			if (curr.next == null) {
				break;
			}
			curr = curr.next;
		}
		curr.next = student;
	}

	// ����
	public void show(int num) {
		if (head == null) {
			System.out.println("�� " + (num + 1) + " ����Ϊ�գ�");
			return;
		}
		System.out.print("�� " + (num + 1) + " �������ϢΪ��");
		Student curr = head;
		while (true) {
			System.out.printf("[id=%d,name=%s]->", curr.id, curr.name);
			if (curr.next == null) {
				break;
			}
			curr = curr.next;
		}
		System.out.println();
	}

	// ����
	public Student findStudentById(int id) {
		if (head == null) {
			System.out.println("����Ϊ�գ�");
			return null;
		}
		Student curr = head;
		while (true) {
			if (curr.id == id) {
				break;
			}
			if (curr.next == null) {
				curr = null;
				break;
			}
			curr = curr.next;
		}
		return curr;
	}
}

class Student {
	public int id;
	public String name;
	public Student next;

	public Student(int id, String name) {
		this.id = id;
		this.name = name;
	}
}
