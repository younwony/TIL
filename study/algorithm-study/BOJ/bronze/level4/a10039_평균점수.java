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
public class a10039_평균점수 {

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
		
		int sum = 0;
		int score;
		for(int i = 0; i < 5; i++){
			score = scanner.nextInt();
			sum += score < 40 ? 40 : score ;
		}
		
		scanner.close();
		
		System.out.println(sum/5);
		
	}

}
