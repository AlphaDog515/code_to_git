package com.demo23.kruskal;

public class KruskalCaseTest {

	public static void main(String[] args) {
		int INF = Integer.MAX_VALUE;
		char[] vertexs = { 'A', 'B', 'C', 'D', 'E', 'F', 'G' };
		int matrix[][] = {
						/* A *//* B *//* C *//* D *//* E *//* F *//* G */
				/* A */ { 0, 12, INF, INF, INF, 16, 14 }, 
				/* B */ { 12, 0, 10, INF, INF, 7, INF },
				/* C */ { INF, 10, 0, 3, 5, 6, INF }, 
				/* D */ { INF, INF, 3, 0, 4, INF, INF },
				/* E */ { INF, INF, 5, 4, 0, 2, 8 }, 
				/* F */ { 16, 7, 6, INF, 2, 0, 9 },
				/* G */ { 14, INF, INF, INF, 8, 9, 0 } };

		KruskalTree kruskalTree = new KruskalTree(vertexs, matrix);
//		kruskalTree.print();
		kruskalTree.kruskal();
	}
}

class KruskalTree {
	private int edgeNum;
	private char[] vertexs;
	private int[][] matrix;

	// 构造方法
	public KruskalTree(char[] vertexs, int[][] matrix) {
		int vlen = vertexs.length;
		this.vertexs = new char[vlen];
		for (int i = 0; i < vertexs.length; i++) {
			this.vertexs[i] = vertexs[i];
		}

		this.matrix = new int[vlen][vlen];
		for (int i = 0; i < vlen; i++) {
			for (int j = 0; j < vlen; j++) {
				this.matrix[i][j] = matrix[i][j];
			}
		}

		for (int i = 0; i < vlen; i++) {
			for (int j = i + 1; j < vlen; j++) {  // 注意
				if (this.matrix[i][j] != Integer.MAX_VALUE) {
					edgeNum++;
				}
			}
		}

	}

	// 打印邻接矩阵
	public void print() {
		System.out.println("邻接矩阵为: \n");
		for (int i = 0; i < vertexs.length; i++) {
			for (int j = 0; j < vertexs.length; j++) {
				System.out.printf("%12d", matrix[i][j]);
			}
			System.out.println();// 换行
		}
	}

	// 主算法
	public void kruskal() {
		int index = 0;
		int[] ends = new int[edgeNum];
		EData01[] rets = new EData01[edgeNum];
		EData01[] edges = getEdges();
//		System.out.println("图的边的集合=" + Arrays.toString(edges) + " 共" + edges.length);
		
		sortEdges(edges);
		
		for (int i = 0; i < edgeNum; i++) {
			int p1 = getPosition(edges[i].start);
			int p2 = getPosition(edges[i].end);
			int m = getEnd(ends, p1);
			int n = getEnd(ends, p2);
			if (m != n) { 
				ends[m] = n;
				rets[index++] = edges[i];
			}
		}
		// <E,F> <C,D> <D,E> <B,F> <E,G> <A,B>
		System.out.println("最小生成树为");
		for (int i = 0; i < index; i++) {
			System.out.println(rets[i]);
		}
	}

	
	// 冒泡排序边
	private void sortEdges(EData01[] edges) {
		for (int i = 0; i < edges.length - 1; i++) {
			for (int j = 0; j < edges.length - 1 - i; j++) {
				if (edges[j].weight > edges[j + 1].weight) {
					EData01 tmp = edges[j];
					edges[j] = edges[j + 1];
					edges[j + 1] = tmp;
				}
			}
		}
	}

	// 返回顶点对应下标
	private int getPosition(char ch) {
		for (int i = 0; i < vertexs.length; i++) {
			if (vertexs[i] == ch) {
				return i;
			}
		}
		return -1;
	}

	// 获取图中边
	private EData01[] getEdges() {
		int index = 0;
		EData01[] edges = new EData01[edgeNum];
		for (int i = 0; i < vertexs.length; i++) {
			for (int j = i + 1; j < vertexs.length; j++) {
				if (matrix[i][j] != Integer.MAX_VALUE) {
					edges[index++] = new EData01(vertexs[i], vertexs[j], matrix[i][j]);
				}
			}
		}
		return edges;
	}

	// 返回对应终点下标
	private int getEnd(int[] ends, int i) {
		while (ends[i] != 0) {
			i = ends[i];
		}
		return i;
	}

}

class EData01 {
	char start;
	char end;
	int weight;

	public EData01(char start, char end, int weight) {
		this.start = start;
		this.end = end;
		this.weight = weight;
	}

	@Override
	public String toString() {
		return "EData [<" + start + ", " + end + ">= " + weight + "]";
	}
}
