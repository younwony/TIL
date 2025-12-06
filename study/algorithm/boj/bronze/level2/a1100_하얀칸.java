/**
 * @작성자 wony
 * @작성일 2019. 12. 12.
 * @사용처
 *
 */
package _0_Bronze.Level_2;

import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * @작성자 wony
 * @작성일 2019. 12. 12.
 * @사용처
 * 
 */
public class a1100_하얀칸 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 12.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		String xFiled;
		int count = 0;
		for(int i = 0; i < 8; i++){
			xFiled = scanner.nextLine();
			for(int j = 0; j < 8; j++){
				if((i+j)%2 == 0 && "F".equals(String.valueOf(xFiled.charAt(j))))
					count++;
			}
		}
		scanner.close();
		
		System.out.println(count);
	}

}
