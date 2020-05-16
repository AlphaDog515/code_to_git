package com.demo22.prim;

import java.util.Arrays;

public class PrimAlgorithmTest {
	public static void main(String[] args) {

		char[] data = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G' };
		int vertexs = data.length;

		int[][] weight = new int[][] { 
			{ 10000, 5, 7, 10000, 10000, 10000, 2 }, 
			{ 5, 10000, 10000, 9, 10000, 10000, 3 },
			{ 7, 10000, 10000, 10000, 8, 10000, 10000 }, 
			{ 10000, 9, 10000, 10000, 10000, 4, 10000 },
			{ 10000, 10000, 8, 10000, 10000, 5, 4 }, 
			{ 10000, 10000, 10000, 4, 5, 10000, 6 },
			{ 2, 3, 10000, 10000, 4, 6, 10000 }, };

		MGraph01 graph = new MGraph01(vertexs);
		MinTree01 minTree = new MinTree01();
		minTree.createGraph(graph, vertexs, data, weight);
//		minTree.showGraph(graph);
		minTree.prim(graph, 0);
	}
}

class MinTree01 {

	// 创建图的邻接矩阵
	public void createGraph(MGraph01 graph, int vertexs, char[] data, int[][] weight) {
		int i, j;
		for (i = 0; i < vertexs; i++) {
			graph.data[i] = data[i];
			for (j = 0; j < vertexs; j++) {
				graph.weight[i][j] = weight[i][j];
			}
		}
	}

	// 显示图的邻接矩阵
	public void showGraph(MGraph01 graph) {
		for (int[] link : graph.weight) {
			System.out.println(Arrays.toString(link));
		}
	}

	// prim算法
	public void prim(MGraph01 graph, int vertex) {
		int visited[] = new int[graph.vertexs];
		visited[vertex] = 1;
		int h1 = -1;
		int h2 = -1;

		int minWeight = Integer.MAX_VALUE;
		for (int k = 1; k < graph.vertexs; k++) {
			for (int i = 0; i < graph.vertexs; i++) {
				for (int j = 0; j < graph.vertexs; j++) {
					if (visited[i] == 1 && visited[j] == 0 && graph.weight[i][j] < minWeight) {
						minWeight = graph.weight[i][j];
						h1 = i;
						h2 = j;
					}
				}
			}
			System.out.println("边<" + graph.data[h1] + "," + graph.data[h2] + ">权值:" + minWeight);
			visited[h2] = 1;
			minWeight = Integer.MAX_VALUE;
		}

	}

}

class MGraph01 {
	int vertexs;
	char[] data;
	int[][] weight;

	public MGraph01(int vertexs) {
		this.vertexs = vertexs;
		data = new char[vertexs];
		weight = new int[vertexs][vertexs];
	}
}