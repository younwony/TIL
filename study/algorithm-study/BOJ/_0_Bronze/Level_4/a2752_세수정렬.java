/**
 * @작성자 wony
 * @작성일 2020. 1. 11.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_4;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 11.
 * @사용처
 * @Todo
 */

public class a2752_세수정렬 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 11.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int[] array = {scanner.nextInt(), scanner.nextInt(), scanner.nextInt()};
		scanner.close();
		Arrays.sort(array);
		for(int i : array) {System.out.print(i + " ");}
	}

}
