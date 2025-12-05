/**
 * @작성자 wony
 * @작성일 2019. 10. 19.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_4;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 10. 19.
 * @사용처 한수 구하기
 * @Todo
 */

public class a1065_한수 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 10. 19.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int testCount = scanner.nextInt();
		
		int resultCount = 0;
		
		int num;
		int[] numFormat;
		int[] numFormatDif;
		boolean numCK = false;
		
		for(int i = 1; i <= testCount; i++) {
			if(i<100) {
				resultCount++;
			}else{
				num = i;
				
				numFormat = new int[String.valueOf(i).length()];
				numFormatDif = new int[String.valueOf(i).length() - 1];
				for (int j = 0; j < numFormat.length; j++) {
					numFormat[j] = num % 10;
					num /= 10;
				}

				for (int k = 0; k < numFormatDif.length; k++) {
					numFormatDif[k] = numFormat[k] - numFormat[k + 1];
				}

				for (int l = 0; l < numFormatDif.length - 1; l++) {
					if (numFormatDif[l] == numFormatDif[l + 1])
						numCK = true;
					else
						numCK = false;
				}
				if (numCK)
					resultCount++;
			}
		}
		
		System.out.println(resultCount);
	}
	

}
