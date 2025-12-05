/**
 * @작성자 wony
 * @작성일 2020. 1. 23.
 * @사용처
 *
 */
package _1_Silver.Level_5;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 23.
 * @사용처
 * 
 */
public class a8979_올림픽 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 23.
	 * @사용처 
	 * @Todo 정렬로 풀어보다 실패함.. 비교문에서 실패한듯
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int k = scanner.nextInt();
		
		Olympic[] olympics = new Olympic[n+1];
		for(int i = 0; i < n; i++){
			int order = scanner.nextInt();
			olympics[order] = new Olympic(scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
		}
		
		int count = 1;
		
		Olympic resultOl = olympics[k];
		for(int i = 1; i <= n; i++){
			Olympic isEqualsOl = olympics[i];
			if (isEqualsOl.gold > resultOl.gold || (isEqualsOl.gold == resultOl.gold && isEqualsOl.silver > resultOl.silver) || (isEqualsOl.gold == resultOl.gold && isEqualsOl.silver == resultOl.silver && isEqualsOl.bronze > resultOl.bronze)) count++;
		}
		
		System.out.println(count);
	}
	
	static class Olympic{
		int order;
		int gold;
		int silver;
		int bronze;
		public Olympic() {
			super();
			// TODO Auto-generated constructor stub
		}

		public Olympic(int gold, int silver, int bronze) {
			super();
			this.gold = gold;
			this.silver = silver;
			this.bronze = bronze;
		}
	}
}


