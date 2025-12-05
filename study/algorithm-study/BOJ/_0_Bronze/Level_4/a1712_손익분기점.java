/**
 * @작성자 wony
 * @작성일 2019. 11. 28.
 * @사용처
 *
 */
package _0_Bronze.Level_4;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 11. 28.
 * @사용처
 * 
 */
public class a1712_손익분기점 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 28.
	 * @사용처 Bronze4
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int fixMoney = scanner.nextInt();
		int varMoney = scanner.nextInt();
		int noteBookPrice = scanner.nextInt();
		
		if(noteBookPrice > varMoney){
			System.out.println(fixMoney/(noteBookPrice - varMoney) + 1);
		}else
			System.out.println(-1);
		scanner.close();
	}

}
