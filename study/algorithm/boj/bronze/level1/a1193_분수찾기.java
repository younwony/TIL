/**
 * @작성자 wony
 * @작성일 2019. 12. 23.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_1;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 23.
 * @사용처
 * @Todo
 */

public class a1193_분수찾기 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 23.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int n = scanner.nextInt();
		scanner.close();
		
		int count = 1;
		
		while(n>(count*(count+1))/2)count++;
		
		int preNumber = (count*(count-1))/2;
		
		if(count % 2 == 0) {
			System.out.println(((n-preNumber))+"/"+((count+1) - (n-preNumber)));
		}else {
			System.out.println(((count+1) - (n-preNumber))+"/"+(n-preNumber));
		}
	}

}
