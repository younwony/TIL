/**
 * @작성자 wony
 * @작성일 2020. 3. 8.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_5;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 3. 8.
 * @사용처
 * @Todo
 */

public class a11576_BaseConversion {

	/**
	 * @작성자 wony
	 * @작성일 2020. 3. 8.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int aBase = scanner.nextInt();
		int bBase = scanner.nextInt();
		
		int testCount = scanner.nextInt();
		
		int aNum = 0;
		
		for(int i = 1; i <= testCount; i++) {
			aNum += Math.pow(aBase, testCount-i)*scanner.nextInt();
		}
		
		int bBaseNumber = 0;
		
		while(aNum > Math.pow(bBase, bBaseNumber)) {
			bBaseNumber++;
		}
		
		int bNumber;
		
		bBaseNumber--;
		while(bBaseNumber >= 0) {
			bNumber = (int) Math.pow(bBase, bBaseNumber);
			System.out.print(aNum/bNumber + " ");
			aNum %= bNumber;
			bBaseNumber--;
		}
		
		scanner.close();
		
	}

}
