package com.demo16.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class TestGraph01 {

	public static void main(String[] args) {

		int n = 10;
		String Vertexs[] = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" }; // �ڵ�

		Graph01 graph = new Graph01(n);
		for (String vertex : Vertexs) {
			graph.insertVertex(vertex);
		}

		// ��ӱ�
		// A-B A-C A-D B-E B-F C-G C-H D-I D-J
		// 0 1 2 3 4 5 6 7 8 9
		// A B C D E F G H I J
//		graph.insertEdge(0, 1, 1);
		graph.insertEdge(0, 2, 1);
		graph.insertEdge(0, 3, 1);
		graph.insertEdge(1, 4, 1);
		graph.insertEdge(1, 5, 1);
		graph.insertEdge(2, 6, 1);
		graph.insertEdge(2, 7, 1);
		graph.insertEdge(3, 8, 1);
		graph.insertEdge(3, 9, 1);

		graph.showGraph();

		System.out.print("������ȱ���:");
		boolean[] boo = new boolean[10];
//		graph.dfs(boo, 0);
//		graph.dfs(); 

		System.out.println();
		System.out.print("������ȱ���:");
		graph.bfs(boo, 0);
//		graph.bfs(); 
	}

}

class Graph01 {
	private ArrayList<String> vertexList; // ���㼯��
	private int[][] edges;
	private int numOfEdges;
	private boolean[] isVisited;

	public Graph01(int n) {
		edges = new int[n][n];
		vertexList = new ArrayList<String>(n); // size = 0,��ʼ���� = n
		numOfEdges = 0;
	}

	// �õ���һ���ڽӵ���±�
	public int getFirstNeighbor(int index) {
		for (int j = 0; j < vertexList.size(); j++) {
			if (edges[index][j] > 0) {
				return j;
			}
		}
		return -1;
	}

	// ����ǰһ���ڽӽ����±�����ȡ��һ���ڽӽ��
	// ���紫�����1,2�����صĵ�����2�ڵ�����һ����1�ڵ���ͨ�Ľڵ�
	public int getNextNeighbor(int v1, int v2) {
		for (int j = v2 + 1; j < vertexList.size(); j++) {
			if (edges[v1][j] > 0) {
				return j;
			}
		}
		return -1;
	}

	// ������ȱ����㷨
	public void dfs(boolean[] isVisited, int i) {
		System.out.print(getValueByIndex(i) + "->");
		isVisited[i] = true;
		int w = getFirstNeighbor(i);
		while (w != -1) {
			if (!isVisited[w]) {
				dfs(isVisited, w);
			}
			w = getNextNeighbor(i, w);
		}
	}

	// ѭ�����Խ������ͨ�����
	public void dfs() {
		isVisited = new boolean[vertexList.size()];
		for (int i = 0; i < getNumOfVertex(); i++) {
			if (!isVisited[i]) {
				dfs(isVisited, i);
			}
		}
	}

	// ������ȱ���
	public void bfs(boolean[] isVisited, int i) {
		int u, w;
		LinkedList<Integer> queue = new LinkedList<Integer>();
		System.out.print(getValueByIndex(i) + "=>");
		isVisited[i] = true;
		queue.addLast(i);

		while (!queue.isEmpty()) {
			u = queue.removeFirst();
			w = getFirstNeighbor(u);
			while (w != -1) {
				if (!isVisited[w]) {
					System.out.print(getValueByIndex(w) + "=>");
					isVisited[w] = true;
					queue.addLast(w);
				}
				w = getNextNeighbor(u, w);
			}
		}
	}

	public void bfs() {
		isVisited = new boolean[vertexList.size()];
		for (int i = 0; i < getNumOfVertex(); i++) {
			if (!isVisited[i]) {
				bfs(isVisited, i);
			}
		}
	}

	// ���÷���
	// ���ؽڵ����
	public int getNumOfVertex() {
		return vertexList.size();
	}

	// �õ��ߵ���Ŀ
	public int getNumOfEdges() {
		return numOfEdges;
	}

	// ��ʾͼ��Ӧ�ľ���
	public void showGraph() {
		for (int[] link : edges) {
			System.err.println(Arrays.toString(link));
		}
	}

	// ���ؽ��i(�±�)��Ӧ������ 0->"A" 1->"B" 2->"C"
	public String getValueByIndex(int i) {
		return vertexList.get(i);
	}

	// ����v1��v2��Ȩֵ
	public int getWeight(int v1, int v2) {
		return edges[v1][v2];
	}

	// ������
	public void insertVertex(String vertex) {
		vertexList.add(vertex);
	}

	// ��ӱ�
	public void insertEdge(int v1, int v2, int weight) {
		edges[v1][v2] = weight;
		edges[v2][v1] = weight;
		numOfEdges++;
	}
}
