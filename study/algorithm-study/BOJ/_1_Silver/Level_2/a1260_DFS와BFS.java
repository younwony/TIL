/**
 * @작성자 wony
 * @작성일 2020. 1. 31.
 * @사용처
 *
 */
package _1_Silver.Level_2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;

public class a1260_DFS와BFS {

	static boolean[][] isNode; // 인접행렬
	static Map<Integer, Boolean> isVisited = new HashMap<>();
	static Stack<Integer> stack = new Stack<>();
	static Queue<Integer> queue = new LinkedList<>();
	static StringBuilder dfsResult = new StringBuilder();
	static StringBuilder bfsResult = new StringBuilder();
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		String[] inputSplitData = bufferedReader.readLine().split(" ");
		
		int n = Integer.valueOf(inputSplitData[0]);
		int m = Integer.valueOf(inputSplitData[1]);
		int v = Integer.valueOf(inputSplitData[2]);

		// 인접행렬로 그래프 구현
		isNode = new boolean[n+1][n+1];
		
		String[] nodeXY;
		int x,y;
		for(int i = 0 ; i < m; i++){
			nodeXY = bufferedReader.readLine().split(" ");
			x = Integer.valueOf(nodeXY[0]);
			y = Integer.valueOf(nodeXY[1]);
			
			isNode[x][y] = true;
			isNode[y][x] = true;
			
			// 필요한 노드 번호만 가지고 있으면 되기에 Map으로 세팅 
			isVisited.put(x, false);
			isVisited.put(y, false);
		}
		
		// 시작점 v 방문 체크
		isVisited.put(v, true);
		
		// 시작점 input
		stack.push(v);
		
		// dfs 시작
		dfs(v);
		
		// isVisited 초기화
		for(int i : isVisited.keySet()){
			isVisited.put(i, false);
		}
		
		
		// 시작점 v 방문 체크 
		isVisited.put(v, true);
		
		// 시작점 input
		queue.offer(v);
		
		// bfs 시작
		bfs(v);
		
		System.out.println(dfsResult.toString());
		System.out.println(bfsResult.toString());
	}
	
	public static void dfs(int start){
		
		// 모두 방문 완료시 stack 에 남은 값 출력 저장
		if(isVisited()){
			while(!stack.isEmpty()){
				dfsResult.append(stack.pop() + " ");
			}
			return ;
		}
		
		// 입력들어 들어온 값 출력 저장
		dfsResult.append(stack.pop() + " ");
		
		// 현재  노드(start)와 연결된 노드 체크
		for(int j = 1; j < isNode.length; j++){
			// 연결된 노드인지 && 방문 안했는지
			if(isNode[start][j] && !isVisited.get(j)){
				// 연결된 노드 방문 체크
				isVisited.put(j,true);
				// stack 입력
				stack.push(j);
				// dfs 재귀
				dfs(j);
			}
		}
			
	}
	
	public static void bfs(int start){
			
		// 모두 방문 완료시 queue 에 저장된 값 출력 저장
		if(isVisited()){
			while(!queue.isEmpty()){
				bfsResult.append(queue.poll() + " ");
			}
			return ;
		}
		
		// 입력되어 들어온 값 출력 저장
		bfsResult.append(queue.poll() + " ");
		
		// 현재  노드(start)와 연결된 노드 체크
		for(int j = 1; j < isNode.length; j++){
			//연결된 노드인지 && 방문 안했는지
			if(isNode[start][j] && !isVisited.get(j)){
				// 연결된 노드 방문 체크
				isVisited.put(j,true);
				// queue 입력
				queue.offer(j);
			}
		}
		
		// dfs와 다르게 현재 노드(start)와 연결된 모든 노드 체크 후 재귀가 시작된다.
		if(!queue.isEmpty()){
			bfs(queue.peek());
		}
	}
	
	// 모든 노드 방문 체크 함수
	public static boolean isVisited(){
		boolean result = true;
		
		for(int i : isVisited.keySet()){
			if(!isVisited.get(i)){
				result = false;
			}
		}
		
		return result;
	}

}
