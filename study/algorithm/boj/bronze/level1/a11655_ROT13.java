/**
 * @작성자 wony
 * @작성일 2020. 2. 16.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_1;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 2. 16.
 * @사용처
 * @Todo
 */

public class a11655_ROT13 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 2. 16.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		String str = scanner.nextLine();
		scanner.close();
		int strC;
		
		for(int i = 0; i < str.length(); i++) {
			strC = (int)str.charAt(i);
			if(65 <= strC && strC <= 90) {
				strC = (strC-65+13)%26 + 65;
			}else if(97 <=strC && strC <=122) {
				strC = (strC-97+13)%26 + 97;
			}
			System.out.print((char)strC);
		}
	}

}
