/**
 * @작성자 wony
 * @작성일 2020. 5. 10.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_2;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 5. 10.
 * @사용처
 * @Todo
 */

public class a1225_이상한곱셈 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 5. 10.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		String[] ab = scanner.nextLine().split(" ");
		String[] a = ab[0].split("");
		String[] b = ab[1].split("");
		
		scanner.close();
		
		long aSum = 0;
		long bSum = 0;
		for(int i = 0 ; i< a.length; i++) {
			aSum += Integer.parseInt(a[i]);
		}
		for(int i = 0 ; i< b.length; i++) {
			bSum += Integer.parseInt(b[i]);
		}
		
		System.out.println(aSum*bSum);
	}

}
