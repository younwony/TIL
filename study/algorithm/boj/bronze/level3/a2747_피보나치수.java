/**
 * @작성자 wony
 * @작성일 2019. 12. 4.
 * @사용처
 *
 */
package _0_Bronze.Level_3;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 4.
 * @사용처
 * 
 */
public class a2747_피보나치수 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 4.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	static int[] fibo = new int[46];
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int nfibo = scanner.nextInt();
		scanner.close();
		
		fibonacci(nfibo);
		System.out.println(fibo[nfibo]);
	}
	
	public static void fibonacci(int n){
		fibo[0] = 0;
		fibo[1] = 1;
		for(int i = 2; i <= n; i++)
			fibo[i] = fibo[i-1] + fibo[i-2];
	}

}
