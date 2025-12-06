/**
 * @작성자 wony
 * @작성일 2019. 10. 23.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_1;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 10. 23.
 * @사용처
 * @Todo
 */

public class a1074_Z {

	/**
	 * @작성자 wony
	 * @작성일 2019. 10. 23.
	 * @사용처 Z (재귀)
	 * @param args
	 * @Todo 
	 * 1. 구역을 나누어 해당 구역이 아닐경우 구역의 사이즈만큼 카운트
	 * 2. 재귀 도중에 나오기 위해 System.exit(0);
	 */

	static int count = 0;
	static int stopR = 0;
	static int stopC = 0;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int testCount = scanner.nextInt();
		
		stopR = scanner.nextInt();
		stopC = scanner.nextInt();
		
		
		zCount((int)Math.pow(2,testCount), 0, 0);
	} 
	
	public static void zCount(int testCountSquared, int r, int c) {

		if(r == stopR && c == stopC){
			System.out.println(count);
			System.exit(0);
		}
		
		
		if(!(r <= stopR && stopR < r + testCountSquared && c <= stopC && stopC < c + testCountSquared)){
			count += testCountSquared*testCountSquared;
			return;
		}
		
		if(testCountSquared == 1){
			count++;
			return ;
		}
		
		zCount(testCountSquared/2, r, c);
		zCount(testCountSquared/2, r, c + testCountSquared/2);
		zCount(testCountSquared/2, r + testCountSquared/2, c);
		zCount(testCountSquared/2, r + testCountSquared/2, c + testCountSquared/2);
		
	}

}
