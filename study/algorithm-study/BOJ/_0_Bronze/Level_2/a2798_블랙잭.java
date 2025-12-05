/**
 * @작성자 wony
 * @작성일 2019. 12. 19.
 * @사용처
 *
 */
package _0_Bronze.Level_2;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 19.
 * @사용처
 * 
 */
public class a2798_블랙잭 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 19.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	
	static int minNum = 0;
	static int n = 0;
	static int m = 0;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		n = scanner.nextInt();
		m = scanner.nextInt();
		int[] cardArray = new int[n];
		boolean[] visited = new boolean[n];
		for(int i = 0; i < n; i++){
			cardArray[i] = scanner.nextInt();
			visited[i] = false;
		}
		scanner.close();
		DFS(0, 0, cardArray, visited);
		System.out.println(minNum);
	}
	
	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 19.
	 * @사용처  백트래킹
	 * @Todo
	 * @param start : 시작 Index
	 * @param depth : 뽑을 개수 
	 * @param cardArray : 주어진 DataArray
	 * @param visited : 방문여부
	 * 
	 */
	public static void DFS(int start, int depth, int[] cardArray, boolean[] visited){
		if(depth == 3){ // 다뽑을 경우
			int sum = 0;
			for(int i = 0; i < visited.length; i++){
				if(visited[i]) sum += cardArray[i];
			}
			if(sum <= m && (minNum == 0 || sum > minNum)) minNum = sum;
		}else{
			for(int i = start; i < n; i++){
				if(visited[i])continue; else visited[i] = true;
				DFS(i+1, depth+1, cardArray, visited);
				visited[i] = false;
			}
		}
	}
}

