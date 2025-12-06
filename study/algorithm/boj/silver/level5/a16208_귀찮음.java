/**
 * @작성자 wony
 * @작성일 2020. 3. 10.
 * @사용처
 *
 */
package _1_Silver.Level_5;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 3. 10.
 * @사용처
 * 
 */
public class a16208_귀찮음 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 3. 10.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int n = scanner.nextInt();
		
		long[] inputDataAraay = new long[n];
		
		long nSum = 0;
		for(int i = 0 ; i < n; i++){
			inputDataAraay[i] = scanner.nextInt();
			nSum += inputDataAraay[i];
		}
		
		Arrays.sort(inputDataAraay);
		long Sum = 0;
		
		for(int i = inputDataAraay.length-1; i > 0; i--){
			Sum += inputDataAraay[i]*(nSum-inputDataAraay[i]);
			nSum -= inputDataAraay[i];
		}
		System.out.println(Sum);
		
		scanner.close();
	}

}
