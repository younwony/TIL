/**
 * @작성자 wony
 * @작성일 2020. 2. 20.
 * @사용처
 *
 */
package _1_Silver.Level_3;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 2. 20.
 * @사용처
 * 
 */
public class a2193_이친수 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 2. 20.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	static long[][] zeroOne = new long[91][2];
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		scanner.close();

		zeroOne[1][0] = 1;
		zeroOne[1][1] = 0;
		zeroOne[2][0] = 0;
		zeroOne[2][1] = 1;
		
		dp();
		
		System.out.println(zeroOne[n][0] + zeroOne[n][1]);
		
	}
	
	public static void dp(){
		
		for(int i = 3; i < 91; i++){
			zeroOne[i][0] = zeroOne[i-1][0] + zeroOne[i-2][0];
			zeroOne[i][1] = zeroOne[i-1][1] + zeroOne[i-2][1];
		}
	}
}
