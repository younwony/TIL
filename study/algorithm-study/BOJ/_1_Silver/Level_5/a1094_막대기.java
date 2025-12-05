/**
 * @작성자 wony
 * @작성일 2020. 1. 19.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_5;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 19.
 * @사용처
 * @Todo
 */

public class a1094_막대기 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 19.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		String n = Integer.toBinaryString(scanner.nextInt());
		scanner.close();
		int count = 0;
		for(int i = 0; i< n.length(); i++) {
			if(n.charAt(i) == '1') {count++;}
		}
		System.out.println(count);
	}

}
