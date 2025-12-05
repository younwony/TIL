/**
 * @작성자 wony
 * @작성일 2020. 1. 3.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_4;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 3.
 * @사용처
 * @Todo
 */

public class a9498_시험성적 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 3.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		scanner.close();
		if(100 >= n && n >= 90) {System.out.println("A");}
		else if(89 >= n && n >= 80) {System.out.println("B");}
		else if(79 >= n && n >= 70) {System.out.println("C");}
		else if(69 >= n && n >= 60) {System.out.println("D");}
		else {System.out.println("F");}
	}

}
