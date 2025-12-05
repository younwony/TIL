/**
 * @작성자 wony
 * @작성일 2020. 1. 16.
 * @사용처
 *
 */
package _0_Bronze.Level_3;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 16.
 * @사용처
 * 
 */
public class a2490_윷놀이 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 16.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int zeroCount;
		for(int i = 0; i < 3; i++){
			zeroCount = 0;
			for(int j = 0; j < 4; j++){
				if(scanner.nextInt() == 0){zeroCount++;}
			}
			switch (zeroCount) {
			case 0:System.out.println("E");
				break;
			case 1:System.out.println("A");
			break;
			case 2:System.out.println("B");
			break;
			case 3:System.out.println("C");
			break;
			case 4:System.out.println("D");
			break;
			}
		}
		scanner.close();
	}

}
