/**
 * @작성자 wony
 * @작성일 2020. 2. 12.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_5;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 2. 12.
 * @사용처
 * @Todo
 */

public class a1010_다리놓기 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 2. 12.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int testCase = scanner.nextInt();
		
		for(int i = 0; i < testCase; i++) {
			Combination(scanner.nextInt(), scanner.nextInt());
		}
		
		scanner.close();
	}
	
	public static void Combination(int n, int m) {
		
		long top = 1;
		long bottom = 1;
		for(int i = 0; i < n; i++) {
			top *= m-i;
			bottom *= i+1;
			if(top % bottom == 0) {
				top /= bottom;
				bottom = 1;
			}
		}
		
		System.out.println(top/bottom);
		
		
		
		
	}

}
