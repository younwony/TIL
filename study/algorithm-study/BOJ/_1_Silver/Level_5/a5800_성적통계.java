/**
 * @작성자 wony
 * @작성일 2020. 2. 26.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_5;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 2. 26.
 * @사용처
 * @Todo
 */

public class a5800_성적통계 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 2. 26.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int testCase = scanner.nextInt();
		
		int caseCount;
		int[] scoreArray;
		int[] diffScoreArray;
		for(int i = 0; i < testCase; i++) {
			caseCount = scanner.nextInt();
			scoreArray = new int[caseCount];
			diffScoreArray = new int[caseCount-1];
			for(int j = 0; j < caseCount; j++) {
				scoreArray[j] = scanner.nextInt();
			}
			Arrays.sort(scoreArray);
			for(int j = 0; j < caseCount-1; j++) {
				diffScoreArray[j] = scoreArray[j+1] - scoreArray[j];
			}
			Arrays.sort(diffScoreArray);
			System.out.println("Class " + (i+1));
			System.out.println("Max " + scoreArray[caseCount-1] + ", Min " + scoreArray[0] + ", Largest gap " + diffScoreArray[caseCount-2]);
		}
		
		scanner.close();
	}

}
