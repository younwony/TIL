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
public class a2875_대회or인턴 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 4.
	 * @사용처 그리디 알고리즘
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 Scanner scanner = new Scanner(System.in);
		 
		 int n = scanner.nextInt();
		 int m = scanner.nextInt();
		 int k = scanner.nextInt();
		 
		 scanner.close();
		 
		 while(k != 0){
			 if((double)n/2 >= m) n--;
			 else m--;
			 k--;
		 }
		 
		 System.out.println(n/2 > m ? m : n/2);
	}

}
