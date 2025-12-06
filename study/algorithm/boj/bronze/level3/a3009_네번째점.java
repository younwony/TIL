/**
 * @작성자 wony
 * @작성일 2019. 12. 26.
 * @사용처
 *
 */
package _0_Bronze.Level_3;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 26.
 * @사용처
 * 
 */
public class a3009_네번째점 {

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
		int x1,x2,x3,y1,y2,y3, resultX,resultY;
		x1 = scanner.nextInt();
		y1 = scanner.nextInt();
		x2 = scanner.nextInt();
		y2 = scanner.nextInt();
		x3 = scanner.nextInt();
		y3 = scanner.nextInt();
		resultX = x1 == x2 ? x3 : x1 == x3 ? x2 : x1;
		resultY = y1 == y2 ? y3 : y1 == y3 ? y2 : y1;
		scanner.close();
		System.out.println(resultX + " " + resultY);
	}

}
