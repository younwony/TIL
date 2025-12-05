/**
 * @작성자 wony
 * @작성일 2020. 1. 11.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_4;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 11.
 * @사용처
 * @Todo
 */

public class a2525_오븐시계 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 11.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int hour = scanner.nextInt();
		int min = scanner.nextInt();
		int time = scanner.nextInt();
		scanner.close();
		
		if(min + time >= 60) { 
			hour += (min + time)/60;
			min = (min + time)%60;
		}else {
			min = min + time;
		}
		
		if(hour >= 24) {hour -= 24;}
		
		System.out.println(hour + " " + min);
	}

}
