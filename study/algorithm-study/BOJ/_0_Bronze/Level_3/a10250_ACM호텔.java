/**
 * @작성자 wony
 * @작성일 2019. 12. 23.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_3;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 23.
 * @사용처
 * @Todo
 */

public class a10250_ACM호텔 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 23.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int testCount = scanner.nextInt();
		int h,w,n,floor;
		String ho;
		for(int i = 0; i < testCount; i++) {
			h = scanner.nextInt();
			w = scanner.nextInt();
			n = scanner.nextInt();
			floor = n%h == 0 ? h : n%h;
			ho = (((n-1)/h)+1) < 10 ? "0" + (((n-1)/h)+1) : String.valueOf((((n-1)/h)+1));
			System.out.println(floor + ho);
		}
	}

}
