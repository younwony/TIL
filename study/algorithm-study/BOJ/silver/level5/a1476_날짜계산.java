/**
 * @작성자 wony
 * @작성일 2020. 2. 13.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_5;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 2. 13.
 * @사용처
 * @Todo
 */

public class a1476_날짜계산 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 2. 13.
	 * @사용처 중국인의 나머지정리 공부
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int e = scanner.nextInt();
		int s = scanner.nextInt();
		int m = scanner.nextInt();

		int result = 0;

		int eNum,sNum,mNum;
		while(true){
			result++;
			eNum = result % 15 == 0 ? 15 : result % 15;
			sNum = result % 28 == 0 ? 28 : result % 28;
			mNum = result % 19 == 0 ? 19 : result % 19;
			if(eNum == e && sNum == s && mNum == m){
				break;
			}
		}
		System.out.println(result);
		scanner.close();
	}

}
