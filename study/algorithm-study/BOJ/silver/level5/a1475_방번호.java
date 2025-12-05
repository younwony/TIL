/**
 * @작성자 wony
 * @작성일 2020. 1. 28.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_5;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 28.
 * @사용처
 * @Todo
 */

public class a1475_방번호 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 28.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		String n = scanner.nextLine();
		scanner.close();
		double[] numArray = new double[10];
		int inputData;
		for(int i = 0; i<n.length(); i++) {
			inputData = Character.getNumericValue(n.charAt(i));
			if (inputData == 6 || inputData == 9) {
				numArray[inputData] += 0.5;
			} else {
				numArray[inputData] += 1;
			}

		}
		
		int max = 0;
		for(double num : numArray) {
			max = (int)Math.max(max, Math.round(num));
		}
		
		System.out.println(max);
	}

}
