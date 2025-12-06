/**
 * @작성자 wony
 * @작성일 2019. 12. 19.
 * @사용처
 *
 */
package _1_Silver.Level_5;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 19.
 * @사용처
 * 
 */
public class a7568_덩치 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 19.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int testCount = scanner.nextInt();
		int[][] person = new int[testCount][2];
		for(int i = 0; i < testCount; i++){
			person[i][0] = scanner.nextInt();
			person[i][1] = scanner.nextInt();
		}
		scanner.close();
		
		int height;
		int weight;
		int rank;
		for(int i = 0; i < testCount; i++){
			rank = 1;
			height = person[i][0];
			weight = person[i][1];
			for(int j = 0; j < testCount; j++){
				if(i==j) continue;
				if(height < person[j][0] && weight < person[j][1] )	rank++;
			}
			System.out.print(rank + " ");
		}
	}

}
