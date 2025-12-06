/**
 * @작성자 wony
 * @작성일 2020. 1. 20.
 * @사용처
 *
 */
package _0_Bronze.Level_1;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 20.
 * @사용처
 * 
 */
public class a2851_슈퍼마리오 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 20.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int sum = 0;
		int n;
		for(int i = 0; i < 10; i++){
			n = scanner.nextInt();
			if((int)Math.abs(sum-100) >= (int)Math.abs(sum+n-100)){
				sum+=n;
			}else{
				break;
			}
		}
		scanner.close();
		
		System.out.println(sum);
	}
}
