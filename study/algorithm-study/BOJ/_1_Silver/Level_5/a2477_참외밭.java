/**
 * @작성자 wony
 * @작성일 2020. 2. 26.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_5;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 2. 26.
 * @사용처
 * @Todo
 */

public class a2477_참외밭 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 2. 26.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int melonCount = scanner.nextInt();
		
		int[] size = new int[6];
		
		int x = 0,y = 0,xx = 0 ,yy = 0;
		
		for(int i = 0; i < 6; i++) {
			scanner.nextInt();
			size[i] = scanner.nextInt();
			if(i % 2 == 0) {
				if(size[i] > x) {
					x = size[i];
				}
			}else {
				if(size[i] > y) {
					y = size[i];
				}
			}
		}
		
		scanner.close();
		
		for(int i = 0; i < 6; i++) {
			if(i % 2 == 0) {
				if((size[(i+5)%6] + size[(i+1)%6]) == y) {
					xx = size[i];
				}
			}else {
				if((size[(i+5)%6] + size[(i+1)%6]) == x) {
					yy = size[i];
				}
			}
		}
		
		System.out.println(melonCount * (x * y - xx* yy));
	}

}
