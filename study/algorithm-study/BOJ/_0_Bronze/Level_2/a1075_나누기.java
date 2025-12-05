/**
 * @작성자 wony
 * @작성일 2019. 12. 9.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_2;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 9.
 * @사용처
 * @Todo
 */

public class a1075_나누기 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 9.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int n = scanner.nextInt();
		int f = scanner.nextInt();
		
		int result = n/100*100;
		
		if(result == 0) {
			if(f<10)
				System.out.println("0"+f);
			else
				System.out.println(f);
		}
		else {
			for(int i = 0; i < 100; i++) {
				if((result+i) % f == 0) {
					if(i<10)
						System.out.println("0"+i);
					else
						System.out.println(i);
					break;
				}
			}
		}
		scanner.close();
	}

}
