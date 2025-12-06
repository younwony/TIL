/**
 * @작성자 wony
 * @작성일 2020. 1. 25.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_5;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 25.
 * @사용처
 * @Todo
 */

public class a1789_수들의합 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 25.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		long s = scanner.nextLong();
		
		scanner.close();
		long sum = 1;
		long count = 1;
		while(sum <= s) {
			sum += ++count;
		}
		count--;
		System.out.println(count);
	}

}
