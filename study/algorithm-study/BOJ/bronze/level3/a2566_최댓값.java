/**
 * @작성자 wony
 * @작성일 2020. 1. 22.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_3;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 22.
 * @사용처
 * @Todo
 */

public class a2566_최댓값 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 22.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int max = 0;
		int input;
		int x = 0;
		int y = 0;
		
		for(int i = 1; i <= 9; i++) {
			for(int j = 1; j <= 9; j++) {
				input = scanner.nextInt();
				if(max < input) {
					x = i;
					y = j;
					max = input;
				}
			}
		}
		
		scanner.close();
		
		System.out.println(max);
		System.out.println(x + " " + y);
	}

}
