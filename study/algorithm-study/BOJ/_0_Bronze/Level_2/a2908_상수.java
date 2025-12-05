/**
 * @작성자 wony
 * @작성일 2019. 11. 8.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_2;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 11. 8.
 * @사용처
 * @Todo
 */

public class a2908_상수 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 8.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int i,j;
		
		i = scanner.nextInt();
		j = scanner.nextInt();
		
		String[] iArray = String.valueOf(i).split("");
		String[] jArray = String.valueOf(j).split("");
		
		StringBuilder reverseI = new StringBuilder();
		StringBuilder reverseJ = new StringBuilder();
		
		for(int m = 0; m < iArray.length; m++) {
			reverseI.append(iArray[iArray.length-m-1]);
		}
		for(int n = 0; n < jArray.length; n++) {
			reverseJ.append(jArray[jArray.length-n-1]);
		}
		
		if(Integer.parseInt(reverseI.toString()) > Integer.parseInt(reverseJ.toString()))
			System.out.println(reverseI.toString());
		else
			System.out.println(reverseJ.toString());
		
		scanner.close();
	}

}
