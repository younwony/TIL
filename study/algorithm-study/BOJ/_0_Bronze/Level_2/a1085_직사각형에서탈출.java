/**
 * @작성자 wony
 * @작성일 2019. 12. 9.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_2;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 9.
 * @사용처
 * @Todo
 */

public class a1085_직사각형에서탈출 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 9.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int x = scanner.nextInt();
		int y = scanner.nextInt();
		int w = scanner.nextInt();
		int h = scanner.nextInt();
		
		int xResult = x <= w/2 ? x : w-x;
		int yResult = y <= h/2 ? y : h-y;
		
		scanner.close();
		
		System.out.println(xResult > yResult ? yResult : xResult);
		
	}

}
