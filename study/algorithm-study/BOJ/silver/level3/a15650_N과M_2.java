/**
 * @작성자 wony
 * @작성일 2019. 12. 28.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_3;

import java.util.Scanner;
import java.util.Stack;

/**
 * @작성자 wony
 * @작성일 2019. 12. 28.
 * @사용처
 * @Todo
 */

public class a15650_N과M_2 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 28.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	static boolean[] visited;
	static int n;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		n = scanner.nextInt();
		int m = scanner.nextInt();
		visited = new boolean[n+1];
		scanner.close();
		BackTraking(1,m);
	}
	
	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 28.
	 * @사용처 Combination(조합)
	 * @param i
	 * @param m
	 * @Todo
	 */
	public static void BackTraking(int i , int m) {
		if(m == 0) {
			for(int j = 0; j < visited.length; j++) {
				if(visited[j]) {System.out.print(j+" ");}
			}
			System.out.println();
		}else {
			for(; i <= n; i++) {
				if(!visited[i]) {
				visited[i] = true;
				BackTraking(i+1, m-1);
				visited[i] = false;
				}
			}
		}
	}

}
