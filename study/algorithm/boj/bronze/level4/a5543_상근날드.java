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
public class a5543_상근날드 {

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
		int hMin = 2000;
		int jMin = 2000;
		for(int i = 0; i < 3; i++){
			hMin = (int)Math.min(hMin, scanner.nextInt());
		}
		for(int i = 0; i < 2; i++){
			jMin = (int)Math.min(jMin, scanner.nextInt());
		}
		scanner.close();
		System.out.println(hMin + jMin - 50);
	}

}
