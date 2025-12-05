/**
 * @작성자 wony
 * @작성일 2019. 9. 18.
 * @사용처
 * TODO
 */
package _0_Bronze.Level_5;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 9. 18.
 * @사용처
 * TODO
 */
public class a1001_A빼기B {

	/**
	 * @작성자 wony
	 * @작성일 2019. 9. 18.
	 * @사용처  입력 a,b 출력 a-b
	 * @Todo
	 * @param args
	 * TODO
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int a,b = 0;
		
		Scanner scanner = new Scanner(System.in); //문자 입력을 인자로 Scanner 생성한다
		
		a = scanner.nextInt();
		
		b = scanner.nextInt();
		
		System.out.println(a-b);
	}

}
