package com.demo16.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class TestGraph01 {

	public static void main(String[] args) {

		int n = 10;
		String Vertexs[] = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" }; // 节点

		Graph01 graph = new Graph01(n);
		for (String vertex : Vertexs) {
			graph.insertVertex(vertex);
		}

		// 添加边
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

		System.out.print("深度优先遍历:");
		boolean[] boo = new boolean[10];
//		graph.dfs(boo, 0);
//		graph.dfs(); 

		System.out.println();
		System.out.print("广度优先遍历:");
		graph.bfs(boo, 0);
//		graph.bfs(); 
	}

}

class Graph01 {
	private ArrayList<String> vertexList; // 顶点集合
	private int[][] edges;
	private int numOfEdges;
	private boolean[] isVisited;

	public Graph01(int n) {
		edges = new int[n][n];
		vertexList = new ArrayList<String>(n); // size = 0,初始容量 = n
		numOfEdges = 0;
	}

	// 得到第一个邻接点的下标
	public int getFirstNeighbor(int index) {
		for (int j = 0; j < vertexList.size(); j++) {
			if (edges[index][j] > 0) {
				return j;
			}
		}
		return -1;
	}

	// 根据前一个邻接结点的下标来获取下一个邻接结点
	// 比如传入的是1,2，返回的的是在2节点后面第一个与1节点联通的节点
	public int getNextNeighbor(int v1, int v2) {
		for (int j = v2 + 1; j < vertexList.size(); j++) {
			if (edges[v1][j] > 0) {
				return j;
			}
		}
		return -1;
	}

	// 深度优先遍历算法
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

	// 循环可以解决不连通的情况
	public void dfs() {
		isVisited = new boolean[vertexList.size()];
		for (int i = 0; i < getNumOfVertex(); i++) {
			if (!isVisited[i]) {
				dfs(isVisited, i);
			}
		}
	}

	// 广度优先遍历
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

	// 常用方法
	// 返回节点个数
	public int getNumOfVertex() {
		return vertexList.size();
	}

	// 得到边的数目
	public int getNumOfEdges() {
		return numOfEdges;
	}

	// 显示图对应的矩阵
	public void showGraph() {
		for (int[] link : edges) {
			System.err.println(Arrays.toString(link));
		}
	}

	// 返回结点i(下标)对应的数据 0->"A" 1->"B" 2->"C"
	public String getValueByIndex(int i) {
		return vertexList.get(i);
	}

	// 返回v1和v2的权值
	public int getWeight(int v1, int v2) {
		return edges[v1][v2];
	}

	// 插入结点
	public void insertVertex(String vertex) {
		vertexList.add(vertex);
	}

	// 添加边
	public void insertEdge(int v1, int v2, int weight) {
		edges[v1][v2] = weight;
		edges[v2][v1] = weight;
		numOfEdges++;
	}
}
