/**
 * @작성자 wony
 * @작성일 2019. 12. 4.
 * @사용처
 *
 */
package _0_Bronze.Level_3;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 4.
 * @사용처
 * 
 */
public class a1009_분산처리 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 4.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int testCount = scanner.nextInt();
		int a,b,num;
		for(int i = 0; i < testCount; i++){
			a = scanner.nextInt();
			b = scanner.nextInt();
			num = 1;
			for(int j = 0; j < b; j++)
				num = num*a%10;
			num = num == 0 ? 10 : num;
			System.out.println(num);
		}
		scanner.close();
	}

}
