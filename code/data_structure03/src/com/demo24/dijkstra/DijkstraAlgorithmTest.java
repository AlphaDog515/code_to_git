package com.demo24.dijkstra;

import java.util.Arrays;

public class DijkstraAlgorithmTest {
	public static void main(String[] args) {
		char[] vertex = { 'A', 'B', 'C', 'D', 'E', 'F', 'G' };
		int[][] matrix = new int[vertex.length][vertex.length];

		final int N = 65535;
		matrix[0] = new int[] { N, 5, 7, N, N, N, 2 };
		matrix[1] = new int[] { 5, N, N, 9, N, N, 3 };
		matrix[2] = new int[] { 7, N, N, N, 8, N, N };
		matrix[3] = new int[] { N, 9, N, N, N, 4, N };
		matrix[4] = new int[] { N, N, 8, N, N, 5, 4 };
		matrix[5] = new int[] { N, N, N, 4, 5, N, 6 };
		matrix[6] = new int[] { 2, 3, N, N, 4, 6, N };

		Graph01 graph = new Graph01(vertex, matrix);
		graph.showGraph();

		graph.dsj(2);// C
		graph.showDijkstra();

	}
}

class Graph01 {
	private char[] vertex;
	private int[][] matrix;
	private VisitedVertex01 vv;

	public Graph01(char[] vertex, int[][] matrix) {
		this.vertex = vertex;
		this.matrix = matrix;
	}

	public void showGraph() {
		for (int[] link : matrix) {
			System.out.println(Arrays.toString(link));

		}
	}

	public void showDijkstra() {
		vv.show();
	}

	// 更新index点到周围顶点的关系
	private void update(int index) {
		int len = 0;
		for (int j = 0; j < matrix[index].length; j++) {
			len = vv.getDis(index) + matrix[index][j];
			if (!vv.in(j) && len < vv.getDis(j)) {
				vv.updatePre(j, index);
				vv.updateDis(j, len);
			}
		}
	}

	// 主算法
	public void dsj(int index) {
		vv = new VisitedVertex01(vertex.length, index);
		update(index);
		for (int j = 1; j < vertex.length; j++) {
			index = vv.updateArr();
			update(index);
		}
	}

}

class VisitedVertex01 {
	private int[] already_arr;
	private int[] pre_visited;
	private int[] dis;

	public VisitedVertex01(int length, int index) {
		this.already_arr = new int[length];
		this.pre_visited = new int[length];
		this.dis = new int[length];
		this.already_arr[index] = 1;
		this.pre_visited[index] = index;
		Arrays.fill(dis, 65535);
		this.dis[index] = 0;
	}

	// index是否被访问过
	public boolean in(int index) {
		return already_arr[index] == 1;
	}

	// 更新出发顶点到index顶点的距离
	public void updateDis(int index, int len) {
		dis[index] = len;
	}

	// 更新pre这个节点的前驱节点是index
	public void updatePre(int pre, int index) {
		pre_visited[pre] = index;
	}

	// 获取出发点到index的距离
	public int getDis(int index) {
		return dis[index];
	}

	// 选择新的访问节点，没有访问过的最小的距离
	public int updateArr() {
		int min = 65535, index = 0;
		for (int i = 0; i < already_arr.length; i++) {
			if (already_arr[i] == 0 && dis[i] < min) {
				min = dis[i];
				index = i;
			}
		}
		already_arr[index] = 1;
		return index;
	}

	public void show() {
		System.out.println("============");
		for (int i : already_arr) {
			System.out.println(i + " ");
		}

		System.out.println("-------------");
		for (int i : pre_visited) {
			System.out.println(i + " ");
		}

		System.out.println("++++++++++++++");
		for (int i : dis) {
			System.out.println(i + " ");

		}

		System.out.println("--------------");
		char[] vertex = { 'A', 'B', 'C', 'D', 'E', 'F', 'G' };

		for (int i = 0; i < dis.length; i++) {
			String res = "goal -> " + vertex[i] + " <-> " + dis[i];
			System.out.println(res);
		}

	}

}