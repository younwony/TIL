/**
 * @작성자 wony
 * @작성일 2020. 1. 18.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_2;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 18.
 * @사용처
 * @Todo
 */

public class a10987_모음의개수 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 18.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		String data = scanner.nextLine();
		scanner.close();
		int count = 0;
		for(int i = 0; i < data.length(); i++) {
			if(momo(String.valueOf(data.charAt(i)))) {count++;}
		}
		System.out.println(count);
	}
	
	public static boolean momo(String str) {
		if("a".equals(str) || "e".equals(str) || "i".equals(str) || "o".equals(str) || "u".equals(str)) {return true;}
		else {return false;}
	}

}
