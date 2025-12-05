/**
 * @작성자 wony
 * @작성일 2020. 1. 2.
 * @사용처
 *
 */
package _1_Silver.Level_1;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 2.
 * @사용처
 * 
 */
public class a11047_동전0 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 2.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int[] coinType = new int[scanner.nextInt()];
		int k = scanner.nextInt();
		for(int i = 0; i < coinType.length; i++)
			coinType[i] = scanner.nextInt();
		scanner.close();
		
		int count = 0;
		for(int i = coinType.length-1; i >=0; i--){
			if(k/coinType[i] != 0){
				count += k/coinType[i];
				k %= coinType[i];
			}
		}
		System.out.println(count);
		
	}

}
