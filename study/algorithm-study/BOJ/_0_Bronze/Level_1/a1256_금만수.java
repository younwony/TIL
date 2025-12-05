/**
 * @작성자 wony
 * @작성일 2020. 5. 9.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_1;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 5. 9.
 * @사용처
 * @Todo
 */

public class a1256_금만수 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 5. 9.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int n = scanner.nextInt();
		
		while(n>=4) {
			
			if(mod(n)) {
				break;
			}
			n--;
		}
		
		System.out.println(n);
	}
	
	public static boolean mod(int n) {
		boolean result = false;
		
		while(n > 0) {
			int mod = n%10;
			if(mod == 4 || mod == 7) {
				result = true;
			}else {
				result = false;
				break;
			}
			n /= 10;
		}
		
		return result;
	}

}
