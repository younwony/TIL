/**
 * @작성자 wony
 * @작성일 2020. 5. 3.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_2;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 5. 3.
 * @사용처
 * @Todo
 */

public class a5597_과제안내신분 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 5. 3.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		boolean[] isCheck = new boolean[31];
		
		isCheck[0] = true;
		
		for(int i = 0 ; i < 28; i++) {
			isCheck[scanner.nextInt()] = true;
		}
		
		for(int i = 0; i < isCheck.length; i++) {
			if(!isCheck[i]){System.out.println(i);}
		}
	}

}
