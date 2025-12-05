/**
 * @작성자 wony
 * @작성일 2019. 10. 29.
 * @사용처
 *
 */
package _0_Bronze.Level_2;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 10. 29.
 * @사용처 숫자의 합(String)
 * 
 */
public class a11720_숫자의합 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 10. 29.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int inputDataCount = scanner.nextInt();
		scanner.nextLine();
		String inputData = scanner.nextLine();
		scanner.close();
		
		String[] splitData = inputData.split("");
		
		int sum = 0;
		
		for(String data : splitData)
			sum += Integer.valueOf(data);
		
		System.out.println(sum);
	}

}
