package com.demo16.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

//顶点（Vertex），边（edge），邻接，路径，有向图，带权图
//图的遍历，深度优先搜索算法（栈），一层一层往下找，回退上一节点，继续
//广度优先搜索算法（队列），第一层找完了以后找第二层

public class Graph {
	// 测试方法
	public static void main(String[] args) {
		//测试一把图是否创建ok
		int n = 10;  //结点的个数		
		String Vertexs[] = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" }; // 节点		
		
		//创建图对象
		Graph graph = new Graph(n);
		//循环的添加顶点
		for(String vertex: Vertexs) {
			graph.insertVertex(vertex);
		}
		
		// 添加边
		// A-B A-C A-D
		// B-E B-F C-G C-H D-I D-J
		// 0 1 2 3 4 5 6 7 8 9
		// A B C D E F G H I J
		graph.insertEdge(0, 1, 1);
		graph.insertEdge(0, 2, 1);
		graph.insertEdge(0, 3, 1);
		graph.insertEdge(1, 4, 1);
		graph.insertEdge(1, 5, 1);
		graph.insertEdge(2, 6, 1);
		graph.insertEdge(2, 7, 1);
		graph.insertEdge(3, 8, 1);
		graph.insertEdge(3, 9, 1);
		
		//显示一把邻结矩阵
//		graph.showGraph();
		
		// 测试一把，我们的dfs遍历是否ok
		System.out.print("深度优先遍历:");
		boolean[] boo = new boolean[10];
//		graph.dfs(boo, 0);
//		graph.dfs(); 
		
		System.out.println();
		System.out.print("广度优先遍历:");
		graph.bfs(boo, 1);
//		graph.bfs(); 
		
	}
	
	
	// 定义类
	private ArrayList<String> vertexList; //存储顶点集合
	private int[][] edges; //存储图对应的邻接矩阵
	private int numOfEdges; //表示边的数目
	//定义给数组boolean[], 记录某个结点是否被访问
	private boolean[] isVisited;	
	
	//构造器
	public Graph(int n) {
		//初始化矩阵和vertexList
		edges = new int[n][n];
		vertexList = new ArrayList<String>(n);
		numOfEdges = 0;
		
	}
	
	//得到第一个邻接结点的下标 w 
	/**
	 * 
	 * @param index 
	 * @return 如果存在就返回对应的下标，否则返回-1
	 */
	public int getFirstNeighbor(int index) {
		for(int j = 0; j < vertexList.size(); j++) {
			if(edges[index][j] > 0) {
				return j;
			}
		}
		return -1;
	}
	//根据前一个邻接结点的下标来获取下一个邻接结点
	//比如传入的是1,2，返回的的是在2节点后面第一个与1节点联通的节点
	public int getNextNeighbor(int v1, int v2) {
		for(int j = v2 + 1; j < vertexList.size(); j++) {
			if(edges[v1][j] > 0) {
				return j;
			}
		}
		return -1;
	}
	
	//深度优先遍历算法
	//i 第一次就是 0
	private void dfs(boolean[] isVisited, int i) {
		//首先我们访问该结点,输出
		System.out.print(getValueByIndex(i) + "->");
		//将结点设置为已经访问
		isVisited[i] = true;
		//查找结点i的第一个邻接结点w
		int w = getFirstNeighbor(i);
		while(w != -1) {//说明有
			if(!isVisited[w]) {
				dfs(isVisited, w);
			}
			//如果w结点已经被访问过
			w = getNextNeighbor(i, w);
		}
		
	}
	
	//对dfs 进行一个重载, 遍历我们所有的结点，并进行 dfs
	public void dfs() {
		isVisited = new boolean[vertexList.size()];
		//遍历所有的结点，进行dfs[回溯]
		for(int i = 0; i < getNumOfVertex(); i++) {
			if(!isVisited[i]) {
				dfs(isVisited, i);
			}
		}
	}
	
	
	/*
	   LinkedList集合的底层是链表结构实现的，所以可以模拟栈（先进后出）和队列（先进先出）。
	      方法：
　　　　addFirst()		//添加元素到列表的起始位置
　　　　addLast()		//添加元素到列表的结束位置
　　　　removeFirst()	//移除列表起始位置的元素
　　　　removeLast()		//移除列表结束位置的元素
　　　　getFirst()		//获取列表起始位置的元素
　　　　getLast()		//获取列表结束位置的元素
	 */
	
	//对一个结点进行广度优先遍历的方法
	private void bfs(boolean[] isVisited, int i) {
		int u ; // 表示队列的头结点对应下标
		int w ; // 邻接结点w
		//队列，记录结点访问的顺序
		LinkedList queue = new LinkedList();
		//访问结点，输出结点信息
		System.out.print(getValueByIndex(i) + "=>");
		//标记为已访问
		isVisited[i] = true;
		//将结点加入队列
		queue.addLast(i);
		
		while( !queue.isEmpty()) {
			//取出队列的头结点下标
			u = (Integer)queue.removeFirst();	// 返回被移除的那个元素
			//得到第一个邻接结点的下标 w 
			w = getFirstNeighbor(u);
			while(w != -1) {//找到
				//是否访问过
				if(!isVisited[w]) {
					System.out.print(getValueByIndex(w) + "=>");
					//标记已经访问
					isVisited[w] = true;
					//入队
					queue.addLast(w);
				}
				//以u为前驱点，找w后面的下一个邻结点
				w = getNextNeighbor(u, w); //体现出我们的广度优先
			}
		}
		
	} 
	
	//遍历所有的结点，都进行广度优先搜索
	public void bfs() {
		isVisited = new boolean[vertexList.size()];
		for(int i = 0; i < getNumOfVertex(); i++) {
			if(!isVisited[i]) {
				bfs(isVisited, i);
			}
		}
	}
	
	//图中常用的方法
	//返回结点的个数
	public int getNumOfVertex() {
		return vertexList.size();
	}
	//显示图对应的矩阵
	public void showGraph() {
		for(int[] link : edges) {
			System.err.println(Arrays.toString(link));
		}
	}
	//得到边的数目
	public int getNumOfEdges() {
		return numOfEdges;
	}
	//返回结点i(下标)对应的数据 0->"A" 1->"B" 2->"C"
	public String getValueByIndex(int i) {
		return vertexList.get(i);
	}
	//返回v1和v2的权值
	public int getWeight(int v1, int v2) {
		return edges[v1][v2];
	}
	//插入结点
	public void insertVertex(String vertex) {
		vertexList.add(vertex);
	}
	
	//添加边
	/**
	 * 
	 * @param v1 表示点的下标即使第几个顶点  "A"-"B" "A"->0 "B"->1
	 * @param v2 第二个顶点对应的下标
	 * @param weight 表示 
	 */
	public void insertEdge(int v1, int v2, int weight) {
		edges[v1][v2] = weight;
		edges[v2][v1] = weight;
		numOfEdges++;
	}
}
