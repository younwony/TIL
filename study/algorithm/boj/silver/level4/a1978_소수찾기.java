/**
 * @작성자 wony
 * @작성일 2019. 12. 26.
 * @사용처
 *
 */
package _1_Silver.Level_4;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 26.
 * @사용처
 * 
 */
public class a1978_소수찾기 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 26.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int testCount = scanner.nextInt();
		
		int[] memDecimal = new int[1001];
		
		memDecimal[1] = 1;
		
		for(int i = 2; i < (int)Math.sqrt(memDecimal.length); i++){
			for(int j = i*i; j < memDecimal.length; j += i){
				memDecimal[j] = 1;
			}
		}
		
		int count = 0;
		for(int i = 0; i < testCount; i++){
			if(memDecimal[scanner.nextInt()] == 0) count++;
		}
		
		scanner.close();
		
		System.out.println(count);
	}

}
