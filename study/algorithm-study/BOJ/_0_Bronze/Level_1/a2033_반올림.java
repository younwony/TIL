/**
 * @작성자 wony
 * @작성일 2020. 3. 1.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_1;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 3. 1.
 * @사용처
 * @Todo
 */

public class a2033_반올림 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 3. 1.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int number = scanner.nextInt();
		
		scanner.close();
		
		int mod = 10;
		
		int modNum;
		while(number/mod > 0) {
			modNum = number%mod;
			if(modNum >= mod/2) {
				number += mod;
			}
			number -= modNum;
			mod *= 10;
		}
		
		System.out.println(number);
		
	}

}
