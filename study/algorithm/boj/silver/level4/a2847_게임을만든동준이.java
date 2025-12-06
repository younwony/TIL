/**
 * @작성자 wony
 * @작성일 2020. 2. 23.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_4;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 2. 23.
 * @사용처
 * @Todo
 */

public class a2847_게임을만든동준이 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 2. 23.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int testCase = scanner.nextInt();
		
		int[] dataArray = new int[testCase];
		int count = 0;
		
		for(int i = 0; i < testCase; i++) {
			dataArray[i] = scanner.nextInt();
		}
		
		scanner.close();
		
		boolean result = true;
		
		while(result) {
			result = false;
			for(int i = 1; i < testCase; i++) {
				if(dataArray[i-1] >= dataArray[i]) {
					count += dataArray[i-1] - dataArray[i] + 1;
					dataArray[i-1] -= dataArray[i-1] - dataArray[i] + 1;
					result = true;
				}
			}
		}
		
		System.out.println(count);
	}

}
