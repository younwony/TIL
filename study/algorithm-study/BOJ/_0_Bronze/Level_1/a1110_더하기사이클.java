/**
 * @작성자 wony
 * @작성일 2019. 10. 2.
 * @사용처
 * TODO
 */
package _0_Bronze.Level_1;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 10. 2.
 * @사용처
 * TODO
 */
public class a1110_더하기사이클 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 10. 2.
	 * @사용처 더하기 싸이클
	 * @Todo
	 * @param args
	 * TODO
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int n = scanner.nextInt();
		int result = n;
		int count = 0;
		do{	
			result = (result%10 * 10) + ((result/10 + result%10)%10); 
			count++;
		}while(result != n);
		System.out.println(count);
	}

}
