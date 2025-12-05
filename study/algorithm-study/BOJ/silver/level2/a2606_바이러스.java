/**
 * @작성자 wony
 * @작성일 2020. 2. 6.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_2;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 2. 6.
 * @사용처
 * @Todo
 */

public class a2606_바이러스 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 2. 6.
	 * @사용처
	 * @param args
	 * @Todo
	 */
	static int testCase;
	static boolean[] virus;
	static boolean[][] inputLink;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		testCase = scanner.nextInt();
		virus = new boolean[testCase];
		inputLink = new boolean[testCase][testCase];
		
		int linkCount = scanner.nextInt();
		
		for(int i = 0; i < linkCount; i++) {
			inputLink[scanner.nextInt()-1][scanner.nextInt()-1] = true;
		}
		
		scanner.close();
		
		dfs(0);
		
		int count = 0;
		
		for(boolean check : virus) {
			if(check) {
				count++;
			}
		}
		
		System.out.println(count);
	}
	
	public static void dfs(int n) {
		for(int i = 0; i < testCase; i++) {
			if(inputLink[n][i]) {
				virus[i] = true;
				dfs(i);
			}
		}
	}

}
