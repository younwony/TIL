/**
 * @작성자 wony
 * @작성일 2020. 1. 5.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_3;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 5.
 * @사용처
 * @Todo
 */

public class a9095_1_2_3_더하기 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 5.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	static int count = 0;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int testCase = scanner.nextInt();

		for(int i = 0 ; i < testCase; i++) {
			BackTracking(scanner.nextInt());
			System.out.println(count);
			count=0;
		}
		scanner.close();
	}
	
	
	public static void BackTracking(int n) {
		if(n == 0) {
			count++;
		}else if(n > 0){
			BackTracking(n-1);
			BackTracking(n-2);
			BackTracking(n-3);
		}
	}

}
