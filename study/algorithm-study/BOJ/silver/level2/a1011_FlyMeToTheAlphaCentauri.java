/**
 * @작성자 wony
 * @작성일 2019. 12. 25.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_2;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 25.
 * @사용처
 * @Todo
 */

public class a1011_FlyMeToTheAlphaCentauri {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 25.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int testCount = scanner.nextInt();
		int x,y;
		for(int i = 0; i < testCount; i++) {
			x = scanner.nextInt();
			y = scanner.nextInt();
			SpaceMove(y - x);
		}
	}
	
	public static void SpaceMove(int n) {
		for(int i = 1; ; i++) {
			if(i > Math.sqrt(n)) {
				if(n <= (Math.pow(i-1,2) + i-1)) {
					System.out.println(2*(i-1));
				}else {
					System.out.println(2*i - 1);
				}
				break;
			}else if(i == Math.sqrt(n)) {
				System.out.println((int)(2*Math.sqrt(n) - 1));
				break;
			}
		}
	}
	
	
	
}
