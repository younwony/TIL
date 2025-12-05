/**
 * @작성자 wony
 * @작성일 2020. 3. 6.
 * @사용처
 *
 */
package _1_Silver.Level_4;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 3. 6.
 * @사용처
 * 
 */
public class a1120_문자열 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 3. 6.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		String[] inputAB = scanner.nextLine().split(" ");
		
		scanner.close();
		
		String A = inputAB[0];
		String B = inputAB[1];
		
		int minusLength = B.length() - A.length();
		
		int count;
		
		int minCount = B.length();
		
		for(int i = 0; i<= minusLength; i++){
			count = 0;
			for(int j = 0; j < A.length(); j++){
				if(B.charAt(i+j) != A.charAt(j)){
					count++;
				}
			}
			
			if(minCount >= count){minCount = count;}
		}
		
		System.out.println(minCount);
	}

}
