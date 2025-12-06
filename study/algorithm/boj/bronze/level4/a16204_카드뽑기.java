/**
 * @작성자 wony
 * @작성일 2020. 2. 5.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_4;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 2. 5.
 * @사용처
 * @Todo
 */

public class a16204_카드뽑기 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 2. 5.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int n = scanner.nextInt();
		int nO = scanner.nextInt();
		int nX = n-nO;
		int cO = scanner.nextInt();
		int cX = n-cO;
		
		int o = nO > cO ? cO : nO;
		int x = nX > cX ? cX : nX;
		
		System.out.println(o+x);
	}

}
