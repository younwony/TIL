/**
 * @작성자 wony
 * @작성일 2020. 1. 10.
 * @사용처
 *
 */
package _0_Bronze.Level_4;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 10.
 * @사용처
 * 
 */
public class a10797_10부제 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 10.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int checkNum = scanner.nextInt();
		int count = 0;
		for(int i = 0; i< 5; i++){
			if(checkNum == scanner.nextInt()){count++;}
		}
		scanner.close();
		System.out.println(count);
	}

}
