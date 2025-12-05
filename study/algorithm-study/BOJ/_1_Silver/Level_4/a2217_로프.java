/**
 * @작성자 wony
 * @작성일 2020. 3. 9.
 * @사용처
 *
 */
package _1_Silver.Level_4;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 3. 9.
 * @사용처
 * 
 */
public class a2217_로프 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 3. 9.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int testCount = scanner.nextInt();
		
		int[] nArray = new int[testCount];
		for(int i = 0 ; i < testCount; i++){
			nArray[i] = scanner.nextInt();
		}
		
		Arrays.sort(nArray);
		
		int max = 0;
		
		for(int i = 0 ; i < testCount; i++){
			if(nArray[i]*(testCount-i) >= max){
				max = nArray[i]*(testCount-i);
			}
		}
		
		scanner.close();
		
		System.out.println(max);
	}

}
