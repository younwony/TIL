/**
 * @작성자 wony
 * @작성일 2020. 2. 9.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_5;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 2. 9.
 * @사용처
 * @Todo
 */

public class a10610_30 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 2. 9.
	 * @사용처 3의배수 x 10의 배수
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		char[] str = scanner.next().toCharArray();
		
		scanner.close();
		
		int sum = 0;
		
		boolean isZero = false;
		
		
		int num;
		for(int i = 0; i < str.length; i++) {
			num = str[i] - '0';
			
			if(num == 0) {
				isZero = true;
			}
			
			sum += num;
		}
		
		Arrays.sort(str);
		
		StringBuilder builder = new StringBuilder(new String(str));
		
		if(!isZero || sum % 3 != 0) {
			System.out.println(-1);
		}else {
			System.out.println(builder.reverse().toString());
		}
		
	}

}
