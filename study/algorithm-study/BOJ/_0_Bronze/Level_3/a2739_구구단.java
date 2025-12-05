/**
 * @작성자 wony
 * @작성일 2019. 10. 2.
 * @사용처
 * TODO
 */
package _0_Bronze.Level_3;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 10. 2.
 * @사용처
 * TODO
 */
public class a2739_구구단 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 10. 2.
	 * @사용처 구구단
	 * @Todo
	 * @param args
	 * TODO
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int a;
		a = scanner.nextInt();
		
		for(int i =1; i<=9; i++)
			System.out.println(a + " * " + i + " = " + a*i);	
	} 

}
